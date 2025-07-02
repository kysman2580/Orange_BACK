package com.kh.dotogether.profile.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.dotogether.profile.model.dto.ProfileDTO;

@Mapper
public interface ProfileMapper {

	void setProfile(ProfileDTO profileDto);

	ProfileDTO findByUserNo(Long userNo);

	void deleteProfile(Long userNo);
	
}
