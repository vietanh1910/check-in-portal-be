package com.example.hunter_point.utils.response;


import com.example.hunter_point.utils.response.data.ResponseData;

public class ListResponse<T> {
	
	private int code;
	private ResponseData<T> data;
	private String message;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public ResponseData<T> getData() {
		return data;
	}
	
	public void setData(ResponseData<T> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
