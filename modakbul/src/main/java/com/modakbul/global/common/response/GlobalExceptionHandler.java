package com.modakbul.global.common.response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected BaseResponse<BaseResponseStatus> baseException(BaseException e){
        return new BaseResponse<>(e.getStatus());
    }
}
