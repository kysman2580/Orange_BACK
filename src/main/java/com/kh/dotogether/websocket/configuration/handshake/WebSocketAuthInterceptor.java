package com.kh.dotogether.websocket.configuration.handshake;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.kh.dotogether.auth.util.JWTUtil;
import com.kh.dotogether.member.model.dao.MemberMapper;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.token.model.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor{
	
	private final JWTUtil jwtUtil;
	private final MemberMapper memberMapper;
	private final TokenService tokenService;
	private final UserDetailsService userDetailsService;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		String query = request.getURI().getQuery();
		
		if(query == null || !query.contains("token=")) {
			log.info("요청시 token을 찾을 수 없습니다.");
			return false;
		}
		
		String token = query.split("token=")[1];
		
		
		try {
			Claims claims = jwtUtil.parseJwt(token);
			String userId = claims.getSubject();
			String userRole = jwtUtil.getRoleFromToken(token);

			// DB에서 회원 상태 확인
			MemberDTO member = memberMapper.findByUserId(userId);
			if(member == null) {
				log.info("존재하지 않는 회원 접근 시도: {}", userId);
				tokenService.deleteUserToken(member.getUserNo());
				return false;
			}
			
			if("N".equals(member.getUserStatus())) {
				log.warn("탈퇴한 회원 접근 시도: {}", userId);
				tokenService.deleteUserToken(member.getUserNo());
				return false;
			}
			
//			if(SecurityContextHolder.getContext().getAuthentication() == null) {
//				List<SimpleGrantedAuthority> authorities = Collections.singletonList(
//					new SimpleGrantedAuthority(userRole));
//				
//				UserDetails userDetails = userDetailsService.loadUserByUsername(member.getUserId());
//				UsernamePasswordAuthenticationToken authentication = 
//						new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//				
//				log.debug("인증 성공: userId={}, userNo={}, role={}", 
//						member.getUserId(), member.getUserNo(), userRole);
//			}
			
		} catch(ExpiredJwtException e) {
			log.info("만료된 JWT 토큰 : {}", e.getMessage());
			return false;
			
		} catch(Exception e) {
			log.info("JWT 처리 오류 발생: {}", e.getMessage());
			return false;
		} 
		
		
		return true;

	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}

}
