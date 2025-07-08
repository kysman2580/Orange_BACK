package com.kh.dotogether.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.dotogether.comment.model.dto.CommentDTO;
import com.kh.dotogether.comment.model.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenge/comment")
public class CommentController {
	
	private final CommentService commentService;
	
	@PostMapping
	public ResponseEntity<?> insertComment(@Valid @ModelAttribute CommentDTO comment,
            @RequestParam(name = "file", required = false) MultipartFile file) {
		commentService.insertComment(comment, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
		
	}
	
	@GetMapping
	public ResponseEntity<List<CommentDTO>> selectCommentList(@RequestParam("postId") Long postId) {
	    return ResponseEntity.ok(commentService.selectCommentList(postId));
	}
	
}