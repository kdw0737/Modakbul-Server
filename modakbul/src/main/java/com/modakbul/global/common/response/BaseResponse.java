package com.modakbul.global.common.response;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"httpHeaders", "status", "code", "message", "result"})
public class BaseResponse<T> {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private HttpHeaders httpHeaders;
	private boolean status;
	private int code;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	public BaseResponse(BaseResponseStatus responseStatus) {
		this.status = responseStatus.isStatus();
		this.message = responseStatus.getMessage();
		this.code = responseStatus.getCode();
	}

	public BaseResponse(BaseResponseStatus responseStatus, T result) {
		this.status = responseStatus.isStatus();
		this.message = responseStatus.getMessage();
		this.code = responseStatus.getCode();
		this.result = result;
	}

	public BaseResponse(HttpHeaders httpHeaders, BaseResponseStatus responseStatus) {
		this.status = responseStatus.isStatus();
		this.message = responseStatus.getMessage();
		this.code = responseStatus.getCode();
		this.httpHeaders = httpHeaders;
	}

}
