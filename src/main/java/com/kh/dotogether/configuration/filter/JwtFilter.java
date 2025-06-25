package com.kh.dotogether.configuration.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.kh.dotogether.auth.util.JWTUtil;
import com.kh.dotogether.member.model.dao.MemberMapper;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.token.model.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final MemberMapper memberMapper;
	private final TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		final String requestPath = request.getServletPath();
		
		// 인증 없이 허용하는 경로
		if(requestPath.startsWith("/api/auth/login") || 
		   requestPath.startsWith("/api/auth/refresh") || 
		   requestPath.startsWith("/api/auth/logout") || 
		   requestPath.startsWith("/api/members/")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = authHeader.substring(7);
		log.info("JWT 토큰: {}", token);
		
		try {
			Claims claims = jwtUtil.parseJwt(token);
			String userId = claims.getSubject();
			String userRole = jwtUtil.getRoleFromToken(token);

			// DB에서 회원 상태 확인
			MemberDTO member = memberMapper.findByUserId(userId);
			if(member == null) {
				log.warn("존재하지 않는 회원 접근 시도: {}", userId);
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().write("존재하지 않는 회원입니다.");
				tokenService.deleteUserToken(member.getUserNo());
				return;
			}
			
			if("N".equals(member.getUserStatus())) {
				log.warn("탈퇴한 회원 접근 시도: {}", userId);
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().write("탈퇴한 회원입니다.");
				tokenService.deleteUserToken(member.getUserNo());
				return;
			}
			
			if(SecurityContextHolder.getContext().getAuthentication() == null) {
				List<SimpleGrantedAuthority> authorities = Collections.singletonList(
					new SimpleGrantedAuthority(userRole));
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(member.getUserId());
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				log.debug("인증 성공: userId={}, userNo={}, role={}", 
						member.getUserId(), member.getUserNo(), userRole);
			}
			
		} catch(ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰 : {}", e.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("토큰이 만료되었습니다. 다시 로그인해주세요.");
			return;
			
		} catch(Exception e) {
			log.error("JWT 처리 오류 발생: {}", e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("유효하지 않은 토큰입니다.");
			return;
		}
		
		// 다음 필터 진행
		filterChain.doFilter(request, response);
	}
}
