package com.modakbul.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"status","code","message","result"})
public class BaseResponse<T> {
    @JsonProperty("status")
    private boolean isSuccess;
    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(T result) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus responseStatus) {
        this.isSuccess = responseStatus.isSuccess();
        this.message = responseStatus.getMessage();
        this.code = responseStatus.getCode();
    }

}
