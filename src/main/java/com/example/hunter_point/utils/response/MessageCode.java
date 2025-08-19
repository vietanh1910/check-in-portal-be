package com.example.hunter_point.utils.response;

public class MessageCode {
    public interface ErrorCode {
        String ERROR_NOT_FOUND = "NOT_FOUND";
        String HOST_USED = "HOST_USED";
        String NOT_EXIST = "NOT_EXIST";
        String IS_USED = "IS_USED";
        String NOT_USED = "NOT_USED";
        String FAILED = "FAILED";
        String BAD_REQUEST = "BAD_REQUEST";
        String FILE_NOT_FOUND = "FILE_NOT_FOUND";
        String MAP_REL = "MAP_REL";
    }

    public interface SuccessCode {
        String SUCCESS = "SUCCESS";
        String IS_EMPTY = "IS_EMPTY";
    }
}
