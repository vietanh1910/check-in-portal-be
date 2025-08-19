package com.example.hunter_point.utils.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author HOALTT6
 * @created 5/11/2021
 */
@Data
public class GetResponseData {
    private int code;
    private Object data;
    private String message;

    public GetResponseData() {
    }

    public GetResponseData(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static GetResponseData buildSuccessResponse(Object data){
        HttpStatus httpStatus = HttpStatus.OK;
        return new GetResponseData(httpStatus.value(), data, httpStatus.getReasonPhrase());
    }

    public static GetResponseData buildServerErrorMessageResponse(String message){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new GetResponseData(httpStatus.value(), null, message);
    }

    public static GetResponseData buildUnauthorizedResponse(String message){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return new GetResponseData(httpStatus.value(), null, message);
    }

    public static GetResponseData buildBadRequestResponse(String message){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new GetResponseData(httpStatus.value(), null, message);
    }

    public static GetResponseData buildForbiddenMessageResponse(String message){
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return new GetResponseData(httpStatus.value(), null, message);
    }
}
