package com.kh.dotogether.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// 공통 Error
	INSERT_FAILED("E100", "데이터 저장시 예기치 못한 예외가 발생하였습니다."),
	UPDATE_FAILED("E101", "데이터 수정시 예기치 못한 예외가 발생하였습니다."),
	DELETE_FAILED("E102", "데이터 삭제시 예기치 못한 예외가 발생하였습니다."),
	LOGIN_REQUIRED("E103", "로그인한 사용자만 접근 가능합니다."),
	NOT_FOUND_USER("E104", "존재하지 않는 사용자입니다."),
	DECRYPTION_FAILED("E105", "복호화에 실패했습니다."),
	
	
	// 로그인, 회원가입, 마이페이지 관련 ErrorCode
	DUPLICATE_USER_ID("E200", "이미 존재하는 아이디입니다."),
	DUPLICATE_EMAIL("E201", "이미 존재하는 이메일입니다."),
	DUPLICATE_PHONE("E202", "이미 존재하는 연락처입니다."),
	INVALID_REFRESH_TOKEN("E203", "유효하지 않은 리프레시 토큰입니다."),
	EXPIRED_REFRESH_TOKEN("E204", "만료된 리프레시 토큰입니다."),
	REFRESH_TOKEN_NOT_FOUND("E205", "존재하지 않는 리프레시 토큰입니다."),
	INVALID_VERIFICATION_CODE("E206", "이메일 인증 코드가 일치하지 않습니다."),
	USER_UPDATE_FAILED("E207", "사용자 정보 수정에 실패했습니다."),
	INVALID_PROFILE_FILE("E208", "유효하지 않은 프로필 사진 파일입니다."),
	INVALID_AUTH_INFO("E209", "유효한 인증 정보가 없습니다."),
	ONLY_SELF_DELETE("E210", "본인 계정만 탈퇴할 수 있습니다."),
	INVALID_LOGIN_INFO("E211", "아이디 또는 비밀번호를 잘못 입력하셨습니다."),
	EMAIL_NOT_FOUND("E212", "일치하는 이메일이 없습니다."),
	EMAIL_NOT_MATCH("E213", "이메일이 일치하지 않습니다."),
	
	// 팀관련 ErrorCode
	MAX_USER_TEAMS_EXCEEDED("E300", "속한 팀이 5팀 이상입니다."),
	TEAM_NOT_FOUND("E301", "요청한 팀이 존재하지 않습니다."),
	TEAM_UNAUTHORIZED_USER("E302", "요청 권한이 없는 사용자입니다."),
	TEAM_FULL("E303", "팀의 정원이 꽉차 신청이 불가합니다."),
	ALREADY_TEAM_MEMBER("E304", "이미 가입된 사용자입니다."),
	ALREADY_APPLIED("E305", "이미 팀신청을 보낸 팀입니다."),
	TEAM_LEADER_CANNOT_LEAVE("E306", "팀장은 팀탈퇴가 불가능합니다."),
	
	// 개인일정관리 관련 ErrorCode
	DUPLICATE_SECTION_TITLE("E400", "이미 존재하는 제목입니다."),
	BASE_SECTION_NOT_FOUND("E401", "기준 섹션이 존재하지 않습니다."),
	SECTION_NOT_FOUND("E402", "섹션이 존재하지 않습니다."),
	CANNOT_DELETE_LAST_SECTION("E403", "마지막 섹션은 삭제할 수 없습니다."),
	DUE_DATE_PASSED("E404", "과거 날짜는 선택할 수 없습니다."),
	SCHEDULE_NOT_FOUND("E405", "일정이 존재하지 않습니다."),
	EMPTY_TITLE("E406", "섹션 제목을 입력해주세요.");
	
	private final String code;
    private final String message;
}
