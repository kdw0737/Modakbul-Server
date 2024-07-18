package com.modakbul.global.common.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공했습니다."),

    //user
    UPDATE_PROFILE_SUCCESS(true,1001,"프로필 수정에 성공했습니다."),
    REGISTER_SUCCESS(true,1002,"회원가입에 성공했습니다."),
    LOGIN_SUCCESS(true,1003,"소셜로그인에 성공했습니다."),
    REPORT_SUCCESS(true,1004,"회원 신고에 성공했습니다."),
    BLOCK_SUCCESS(true,1005,"회원 차단에 성공했습니다."),
    SEARCH_BLOCKLIST_SUCCESS(true,1006,"차단된 회원 조회에 성공했습니다."),
    UPDATE_BLOCK_SUCCESS(true,1007,"회원 차단 해제에 성공했습니다."),
    WITHDRAWAL_SUCCESS(true,1008,"회원 탈퇴에 성공했습니다."),
    INQUIRY_REGISTRATION_SUCCESS(true,1009,"문의 등록에 성공했습니다."),

    //cafe
    SEARCH_CAFE_SUCCESS(true, 1100, "신규 카페 정보 저장에 성공했습니다."),
    CAFE_DUPLICATED(true, 1101, "이미 등록되어 있는 카페 입니다."),
    REGISTRATION_WAITING_SUCCESS(true, 1102, "매칭 대기 등록에 성공했습니다."),
    DELETE_WAITING_SUCCESS(true, 1103, "매칭 대기 취소가 완료되었습니다."),
    SEARCH_WAITING_SUCCESS(true, 1104, "매칭 대기 목록 조회에 성공했습니다."),
    APPLICATION_MATCHING_SUCCESS(true, 1105,"매칭 신청에 성공했습니다."),
    DELETE_MATCHING_SUCCESS(true, 1106,"매칭 신청 취소에 성공했습니다."),
    REFUSE_MATCHING_SUCCESS(true, 1107,"매칭 신청을 거절했습니다."),
    CREATE_CHAT_SUCCESS(true, 1108,"채팅방 생성에 성공했습니다"),
    SEARCH_MATCHING_SUCCESS(true,1109,"보낸/받은 매칭 목록 조회에 성공했습니다."),
    WAITING_PROFILE_SUCCESS(true,1110,"대기 등록 유저 프로필 조회에 성공했습니다."),
    MATCHING_PROFILE_SUCCESS(true,1111,"매칭 신청 유저 프로필 조회에 성공했습니다."),

    /**
     * 2000 : request 오류
     */
    INVALID_INPUT_VALUE(false, 2001, "요청하신 데이터 값을 확인하시기 바랍니다."),
    INVALID_DATA_VALUE(false, 2002, "요청하신 데이터 형식을 확인하시기 바랍니다."),

    //user
    PASSWORD_FAILED(false, 2003, "비밀번호 인증에 실패했습니다."),
    ID_NOT_EXIST(false, 2004, "존재하지 않는 아이디입니다."),
    JWT_EXPIRED(false, 2005, "유효하지 않은 Refresh Token입니다."),
    CODE_NOT_EXIST(false, 2006, "로그인 코드를 받는데 실패했습니다."),
    LOAD_USERINFO_FAILED(false, 2007, "로그인 과정에서 정보를 받아오는데 실패했습니다."),
    NICKNAME_DUPLICATED(false, 2008, "닉네임 중복 확인에 실패했습니다."),
    REGISTER_FAILED(false, 2009, "회원가입에 실패했습니다."),
    UPDATE_PROFILE_FAILED(false,2010,"프로필 수정에 실패했습니다."),
    ACCESS_DENIED(false, 2011, "API 접근 권한이 없습니다."),
    REPORT_FAILED(false,2012,"회원 신고에 실패했습니다."),
    BLOCK_FAILED(false,2013,"회원 차단에 실패했습니다."),
    UPDATE_BLOCK_FAILED(false,2014,"회원 차단 해제에 실패했습니다."),
    WITHDRAWAL_FAILED(false,2015,"회원 탈퇴에 실패했습니다."),
    INQUIRY_REGISTRATION_FAILED(false,2016,"문의 등록에 실패했습니다."),

    //cafe
    REGISTRATION_WAITING_FAILED(false, 2100, "매칭 대기 등록에 실패했습니다."),
    APPLICATION_MATCHING_FAILED(false, 2101,"매칭 신청에 실패했습니다."),
    DELETE_MATCHING_FAILED(false, 2102,"매칭 신청 취소에 실패했습니다."),
    REFUSE_MATCHING_FAILED(false, 2103,"매칭 신청 거절에 실패했습니다."),
    CREATE_CHAT_FAILED(false, 2104,"채팅방 생성에 실패했습니다."),

    /**
     * 3000 : response 오류
     */
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패했습니다."),

    //user
    SEARCH_BLOCKLIST_FAILED(false,3001,"차단된 회원 조회에 실패했습니다."),

    //cafe
    SEARCH_CAFE_FAILED(false, 3100, "카페 조회에 오류가 발생했습니다."),
    SEARCH_WAITING_FAILED(false, 3101, "매칭 대기 목록 조회에 실패했습니다."),
    SEARCH_MATCHING_FAILED(false,3102,"보낸/받은 매칭 조회에 실패했습니다."),
    WAITING_PROFILE_FAILED(false,3103,"대기 등록 유저 프로필 조회에 실패했습니다."),
    MATCHING_PROFILE_FAILED(false,3104,"매칭 신청 유저 프로필 조회에 실패했습니다."),

    /**
     * 4000 : database, server 오류
     */
    DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패했습니다."),
    SERVER_ERROR(false, 4002, "서버와의 연결에 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        //BaseResponseStatus에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
