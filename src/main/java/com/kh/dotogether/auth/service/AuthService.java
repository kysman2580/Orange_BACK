package com.kh.dotogether.auth.service;

import java.util.Map;

import com.kh.dotogether.auth.model.dto.LoginDTO;
import com.kh.dotogether.auth.model.vo.CustomUserDetails;

public interface AuthService {

	Map<String, String> login(LoginDTO loginDTO);
	
	CustomUserDetails getUserDetails();
	
	void logout(String authorizationHeader);
}
