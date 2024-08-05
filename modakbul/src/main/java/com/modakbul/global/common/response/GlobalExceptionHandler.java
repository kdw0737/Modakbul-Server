package com.modakbul.global.common.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	protected BaseResponse<BaseResponseStatus> baseException(BaseException e) {
		log.error("Handle CommonException: {}", e.getMessage());
		return new BaseResponse<>(e.getStatus());
	}

	/***
	 *
	 * @NotBlank 로 인해 발생하는 오류 핸들러
	 * 추후에 리팩토링 필요 -> BaseException 으로 통일
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError)error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}
}
