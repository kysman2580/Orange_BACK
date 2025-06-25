package com.kh.dotogether.auth.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.dotogether.auth.model.vo.CustomUserDetails;
import com.kh.dotogether.exception.UserNotFoundException;
import com.kh.dotogether.member.model.dao.MemberMapper;
import com.kh.dotogether.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	private final MemberMapper memberMapper;
	
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		MemberDTO user = memberMapper.findByUserId(userId);
		
		if(user == null || "N".equals(user.getUserStatus())) {
			throw new UserNotFoundException("존재하지 않는 회원입니다.");
		}
		log.info("로그인 시도: userId = {}, userPw = {}", userId, user.getUserPw());
		
		return CustomUserDetails.builder()
				.userNo(user.getUserNo())
				.userId(user.getUserId())
				.password(user.getUserPw())
				.userName(user.getUserName())
				.role(user.getUserRole())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole())))
				.build();
	}
	
}
