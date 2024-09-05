package com.modakbul.global.common.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

	/**
	 * 1000 : 요청 성공
	 */
	SUCCESS(true, 1000, "요청에 성공했습니다."),

	//user
	UPDATE_PROFILE_SUCCESS(true, 1001, "프로필 수정에 성공했습니다."),
	REGISTER_SUCCESS(true, 1002, "회원가입에 성공했습니다."),
	LOGIN_SUCCESS(true, 1003, "소셜로그인에 성공했습니다."),
	REPORT_SUCCESS(true, 1004, "회원 신고에 성공했습니다."),
	BLOCK_SUCCESS(true, 1005, "회원 차단에 성공했습니다."),
	SEARCH_BLOCKLIST_SUCCESS(true, 1006, "차단된 회원 조회에 성공했습니다."),
	UNBLOCK_SUCCESS(true, 1007, "회원 차단 해제에 성공했습니다."),
	WITHDRAWAL_SUCCESS(true, 1008, "회원 탈퇴에 성공했습니다."),
	INQUIRY_REGISTRATION_SUCCESS(true, 1009, "문의 등록에 성공했습니다."),
	LOGOUT_SUCCESS(true, 1010, "로그아웃에 성공했습니다."),
	REISSUE_TOKEN_SUCCESS(true, 1011, "Token 재발행에 성공했습니다."),
	SEARCH_PROFILE_SUCCESS(true, 1012, "프로필 조회에 성공했습니다."),
	NICKNAME_NOT_DUPLICATED(true, 1013, "사용할 수 있는 닉네임입니다."),
	GET_MEETINGS_HISTORY_SUCCESS(true, 1014, "참여 모임 내역 조회에 성공했습니다"),
	SEARCH_USER_CAFE_SUCCESS(true, 1015, "모임에 참여했던 카페 목록 조회에 성공했습니다."),
	CREATE_REVIEW(true, 1016, "참여한 모임의 카페 리뷰 작성에 성공했습니다"),
	CREATE_INFORMATION(true, 1017, "카페 제보 작성에 성공했습니다"),
	GET_MATCHES_HISTORY_SUCCESS(true, 1018, "참여 모임 내역 조회에 성공했습니다"),
	GET_MY_BOARD_HISTORY_SUCCESS(true, 1019, "나의 모집글 조회에 성공했습니다"),
	GET_MATCHES_REQUEST_HISTORY_SUCCESS(true, 1020, "나의 참여 요청 목록 조회에 성공했습니다"),
	GET_USER_PROFILE_SUCCESS(true, 1021, "해당 사용자 프로필 정보 조회에 성공했습니다"),
	REPORT_PROFILE_SUCCESS(true, 1022, "사용자 프로필 신고에 성공했습니다."),
	REPORT_CHATROOM_SUCCESS(true, 1023, "채팅방 신고에 성공했습니다"),
	GET_BLOCK_LIST_SUCCESS(true, 1022, "차단 목록 조회에 성공했습니다"),

	//cafe
	SEARCH_CAFE_SUCCESS(true, 1100, "카페 검색에 성공했습니다."),
	CAFE_DUPLICATED(true, 1101, "이미 등록되어 있는 카페 입니다."),
	REGISTRATION_WAITING_SUCCESS(true, 1102, "매칭 대기 등록에 성공했습니다."),
	DELETE_WAITING_SUCCESS(true, 1103, "매칭 대기 취소가 완료되었습니다."),
	SEARCH_WAITING_SUCCESS(true, 1104, "매칭 대기 목록 조회에 성공했습니다."),
	APPLICATION_MATCHING_SUCCESS(true, 1105, "매칭 신청에 성공했습니다."),
	CANCEL_MATCHING_SUCCESS(true, 1106, "매칭 신청 취소에 성공했습니다."),
	REFUSE_MATCHING_SUCCESS(true, 1107, "매칭 신청을 거절했습니다."),
	SEARCH_MATCHING_SUCCESS(true, 1108, "보낸/받은 매칭 목록 조회에 성공했습니다."),
	WAITING_PROFILE_SUCCESS(true, 1109, "대기 등록 유저 프로필 조회에 성공했습니다."),
	MATCHING_PROFILE_SUCCESS(true, 1110, "매칭 신청 유저 프로필 조회에 성공했습니다."),
	SEARCH_SORT_BY_DISTANCE_SUCCESS(true, 1111, "거리순으로 정렬한 카페 목록 조회에 성공했습니다."),
	SEARCH_SORT_BY_MEETING_COUNT_SUCCESS(true, 1112, "모임 많은 순으로 정렬한 카페 목록 조회에 성공했습니다."),
	SEARCH_MEETING_LIST_SUCCESS(true, 1113, "해당 카페의 모임 목록 조회에 성공했습니다."),
	CREATE_MATCHING_SUCCESS(true, 1114, "해당 모임의 매칭 요청에 성공했습니다."),
	SEARCH_MATCH_SUCCESS(true, 1115, "모임 참여 요청 목록 조회에 성공했습니다."),
	EXIT_MATCHING_SUCCESS(true, 1116, "모임 나가기에 성공했습니다."),

	//chat
	CREATE_CHATROOM_SUCCESS(true, 1200, "채팅방 생성에 성공했습니다."),
	EXIT_CHATROOM_SUCCESS(true, 1201, "채팅방 나가기에 성공했습니다."),
	GET_CHATROOM_LIST_SUCCESS(true, 1202, "채팅방 목록 불러오기에 성공했습니다."),
	GET_NEW_MESSAGE_SUCCESS(true, 1203, "새로운 메세지 불러오기에 성공했습니다."),

	//board
	CREATE_BOARD_SUCCESS(true, 1311, "모집글 작성에 성공했습니다."),
	SEARCH_UPDATE_BOARD_SUCCESS(true, 1312, "모집글 수정 정보 조회에 성공했습니다."),
	UPDATE_BOARD_SUCCESS(true, 1313, "모집글 수정에 성공했습니다."),
	SEARCH_BOARD_LIST_SUCCESS(true, 1314, "모집글 목록 조회에 성공했습니다."),
	SEARCH_BOARD_SUCCESS(true, 1315, "모집글 상세 조회에 성공했습니다."),
	DELETE_BOARD_SUCCESS(true, 1316, "모집글 삭제에 성공했습니다."),

	//notification
	SEND_NOTIFICATION_SUCCESS(true, 1401, "알림 전송에 성공했습니다"),
	GET_NOTIFICATION_LIST_SUCCESS(true, 1402, "알림 목록 조회에 성공했습니다"),
	DELETE_NOTIFICATION_SUCCESS(true, 1403, "알림 삭제에 성공했습니다."),
	READ_NOTIFICATION_SUCCESS(true, 1404, "알림 읽음 처리에 성공했습니다"),


	/**
	 * 2000 : request 오류
	 */
	INVALID_INPUT_VALUE(false, 2001, "요청하신 데이터 값을 확인하시기 바랍니다."),
	INVALID_DATA_VALUE(false, 2002, "요청하신 데이터 형식을 확인하시기 바랍니다."),

	//user
	PASSWORD_FAILED(false, 2003, "비밀번호 인증에 실패했습니다."),
	USER_NOT_EXIST(false, 2004, "존재하지 않는 회원입니다."),
	JWT_EXPIRED(false, 2005, "재로그인이 필요합니다."),
	CODE_NOT_EXIST(false, 2006, "로그인 코드를 받는데 실패했습니다."),
	LOAD_USERINFO_FAILED(false, 2007, "로그인 과정에서 정보를 받아오는데 실패했습니다."),
	NICKNAME_DUPLICATED(false, 2008, "중복된 닉네임입니다."),
	REGISTER_FAILED(false, 2009, "회원가입에 실패했습니다."),
	UPDATE_PROFILE_FAILED(false, 2010, "프로필 수정에 실패했습니다."),
	ACCESS_DENIED(false, 2011, "API 접근 권한이 없습니다."),
	REPORT_FAILED(false, 2012, "회원 신고에 실패했습니다."),
	BLOCK_FAILED(false, 2013, "회원 차단에 실패했습니다."),
	UPDATE_BLOCK_FAILED(false, 2014, "회원 차단 해제에 실패했습니다."),
	WITHDRAWAL_FAILED(false, 2015, "회원 탈퇴에 실패했습니다."),
	INQUIRY_REGISTRATION_FAILED(false, 2016, "문의 등록에 실패했습니다."),
	TOKEN_NOT_FOUND(false, 2017, "오류가 발생했습니다. 다시 시도해주세요."),
	CATEGORY_NOT_EXIST(false, 2018, "존재하지 않는 카테고리입니다."),
	BLOCK_NOT_EXIST(false, 2019, "차단 해제 과정에 오류가 발생하였습니다"),

	//cafe
	REGISTRATION_WAITING_FAILED(false, 2100, "매칭 대기 등록에 실패했습니다."),
	APPLICATION_MATCHING_FAILED(false, 2101, "매칭 신청에 실패했습니다."),
	DELETE_MATCHING_FAILED(false, 2102, "매칭 신청 취소에 실패했습니다."),
	REFUSE_MATCHING_FAILED(false, 2103, "매칭 신청 거절에 실패했습니다."),
	CHAT_MESSAGE_NOT_FOUND(false, 2104, "채팅 메세지를 찾을 수 없습니다."),
	CAFE_NOT_FOUND(false, 2105, "해당 카페가 존재하지 않습니다."),
	MATCH_NOT_EXIST(false, 2106, "존재하지 않는 매칭입니다."),

	//chat
	MESSAGE_NOT_FOUND(false, 2200, "메세지가 존재하지 않습니다."),
	CHATROOM_NOT_FOUND(false, 2201, "해당 채팅방이 존재하지 않습니다."),
	DUPLICATE_CHAT_ROOM_USER(false, 2202, "사용자가 이미 다른 채팅방에 포함되어 있습니다."),
	CREATE_CHAT_FAILED(false, 2203, "채팅방 생성에 실패했습니다."),
	USER_CANNOT_MAKE_CHATROOM_ALONE(false, 2204, "내 게시글에는 채팅방을 생성할 수 없습니다."),
	CONNECTED_USER_EMPTY(false, 2205, "채팅방에 접속중인 인원이 없습니다."),

	//board
	BOARD_NOT_FOUND(false, 2300, "해당 게시글이 존재하지 않습니다."),
	PARTICIPANT_EXIST(false, 2301, "모임 참여자가 존재합니다."),
	BOARD_NOT_OWNED_BY_USER(false, 2302, "본인이 작성한 게시글만 삭제할 수 있습니다"),

	//token
	ACCESSTOKEN_EXPIRED(false, 2401, "AccessToken이 만료되었습니다."),
	BLACKLIST_TOKEN(false, 2402, "블랙리스트에 있는 토큰입니다."),
	REFRESHTOKEN_EXPIRED(false, 2403, "유효하지 않은 Refresh Token입니다."),
	UNSUPPORTED_TOKEN(false, 2404, "지원하지 않는 Token 입니다."),
	WRONG_TYPE_TOKEN(false, 2405, "시그니처 검증에 실패한 Token 입니다."),
	DAMAGED_TOKEN(false, 2406, "손상된 Token 입니다."),

	//category
	CATEGORY_NOT_FOUND(false, 2500, "해당 카테고리가 존재하지 않습니다."),

	//image
	EMPTY_IMAGE_FILE(false, 2501, "이미지가 없습니다."),
	IO_EXCEPTION_ON_IMAGE_UPLOAD(false, 2502, "파일 확장자가 올바르지 않습니다."),
	NO_FILE_EXTENTION(false, 2503, "파일 확장자가 없습니다."),
	INVALID_FILE_EXTENTION(false, 2504, "잘못된 파일 확장자 입니다."),
	PUT_OBJECT_EXCEPTION(false, 2505, "이미지 저장에 실패했습니다."),
	IO_EXCEPTION_ON_IMAGE_DELETE(false, 2506, "이미지 삭제에 실패했습니다."),

	//notification
	NOTIFICATION_FAILED(false, 2601, "알림 전송에 실패하였습니다"),
	NOTIFICATION_NOT_EXIST(false, 2602, "해당 알림이 존재하지 않습니다."),

	/**
	 * 3000 : response 오류
	 */
	RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패했습니다."),

	//user
	SEARCH_BLOCKLIST_FAILED(false, 3001, "차단된 회원 조회에 실패했습니다."),

	//cafe
	SEARCH_CAFE_FAILED(false, 3100, "카페 조회에 오류가 발생했습니다."),
	SEARCH_WAITING_FAILED(false, 3101, "매칭 대기 목록 조회에 실패했습니다."),
	SEARCH_MATCHING_FAILED(false, 3102, "보낸/받은 매칭 조회에 실패했습니다."),
	WAITING_PROFILE_FAILED(false, 3103, "대기 등록 유저 프로필 조회에 실패했습니다."),
	MATCHING_PROFILE_FAILED(false, 3104, "매칭 신청 유저 프로필 조회에 실패했습니다."),

	/**
	 * 4000 : database, server 오류
	 */
	DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패했습니다."),
	SERVER_ERROR(false, 4002, "서버와의 연결에 실패했습니다.");

	private final boolean status;
	private final int code;
	private final String message;

	private BaseResponseStatus(boolean status, int code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
