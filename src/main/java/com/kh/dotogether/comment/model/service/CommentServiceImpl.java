package com.kh.dotogether.comment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.dotogether.auth.model.vo.CustomUserDetails;
import com.kh.dotogether.auth.service.AuthService;
import com.kh.dotogether.challenge.model.service.ChallengeService;
import com.kh.dotogether.comment.model.dao.CommentMapper;
import com.kh.dotogether.comment.model.dto.CommentDTO;
import com.kh.dotogether.comment.model.vo.Comment;
import com.kh.dotogether.exception.exceptions.InvalidUserRequestException;
import com.kh.dotogether.file.service.FileService;
import com.kh.dotogether.profile.model.service.S3Service;
import com.kh.dotogether.auth.model.vo.CustomUserDetails;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	
	private final CommentMapper commentMapper;
	private final ChallengeService challengeService;
	private final AuthService authService;
	private final FileService fileService;
	private final S3Service s3Service;

	@Override
	public void insertComment(CommentDTO comment, MultipartFile file) {
		challengeService.findById(comment.getRefBoardNo());
		String tokenUserNo =
			String.valueOf(((CustomUserDetails)authService.getUserDetails()).getUserNo());
		
		if(!tokenUserNo.equals(comment.getCommentWriter())) {
			throw new InvalidUserRequestException("이름을 알 수 없습니다");
		}
		
		String filePath = s3Service.uploadFile(file);
		
		Comment requestData =
			Comment.builder()
					.commentWriter(Long.parseLong(tokenUserNo))
					.commentContent(comment.getCommentContent())
					.refBoardNo(comment.getRefBoardNo())
					.commentFileUrl(filePath)
					.build();
		commentMapper.insertComment(requestData);
	}

	@Override
	public List<CommentDTO> selectCommentList(Long boardNo) {
		challengeService.findById(boardNo);
		return commentMapper.selectCommentList(boardNo);
	}

	@Override
	public CommentDTO update(CommentDTO comment, MultipartFile file) {
		if(file != null && !file.isEmpty()) {
			String filePath = fileService.store(file);
			comment.setCommentFileUrl(filePath);
		}
		commentMapper.update(comment);
		return comment;
	}

}
