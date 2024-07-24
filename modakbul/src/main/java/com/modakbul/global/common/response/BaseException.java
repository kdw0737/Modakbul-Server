package com.modakbul.global.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
	private BaseResponseStatus status;

	public BaseException(BaseResponseStatus status) {
		this.status = status;
	}
}
