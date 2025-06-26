package com.kh.dotogether.util;

import java.util.List;

import lombok.Builder;

@Builder
public class ResponseData {
	private String code;
	private String message;
	private List<Object> items;
}
