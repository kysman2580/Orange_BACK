package com.kh.dotogether.profile.model.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.dotogether.profile.model.dao.ProfileMapper;
import com.kh.dotogether.profile.model.dto.ProfileDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
	
	private final ProfileMapper profileMapper;
	private final S3Service s3Service;

	/**
	 * 등록
	 */
	@Override
	public String setProfile(Long userNo, MultipartFile file) {
		String imageUrl = s3Service.uploadFile(file);
		ProfileDTO dto = ProfileDTO.builder()
									.userNo(userNo)
									.profileImg(imageUrl)
									.build();
		profileMapper.setProfile(dto);
		return imageUrl;
	}

	/**
	 * 조회
	 */
	@Override
	public String findProfile(Long userNo) {
		ProfileDTO dto = profileMapper.findByUserNo(userNo);
		// dto가 null이 아니면 getProfileImg() 결과를 반환
		if (dto != null) {
		    return dto.getProfileImg();
		} else {
		    return null;
		}
	}

	/**
	 * 수정
	 */
	@Override
	public String updateProfile(Long userNo, MultipartFile file) {
		// 기존 프로필 삭제
		ProfileDTO existing = profileMapper.findByUserNo(userNo);
		if(existing != null && existing.getProfileImg() != null) {
			s3Service.deleteFile(existing.getProfileImg());
			profileMapper.deleteProfile(userNo);
		}
		// 새 프로필 업로드
		String imgUrl = s3Service.uploadFile(file);
		ProfileDTO dto = ProfileDTO.builder()
									.userNo(userNo)
									.profileImg(imgUrl)
									.build();
		profileMapper.setProfile(dto);
		return imgUrl;
	}

	/**
	 * 삭제
	 */
	@Override
	public void deleteProfile(Long userNo) {
		ProfileDTO existing = profileMapper.findByUserNo(userNo);
		if(existing != null && existing.getProfileImg() != null) {
			s3Service.deleteFile(existing.getProfileImg());
			profileMapper.deleteProfile(userNo);
		}
	}

}
