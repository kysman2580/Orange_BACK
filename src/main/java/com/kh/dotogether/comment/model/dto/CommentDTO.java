package com.kh.dotogether.comment.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	
	private Long commentNo;
	@NotBlank(message="댓글을 작성해주세요")
	private String commentContent;
	private String commentWriter;
	private Date createDate;
	private Long refBoardNo;
	private String commentFileUrl;
	
}
