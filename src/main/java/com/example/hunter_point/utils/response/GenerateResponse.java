package com.example.hunter_point.utils.response;

import com.example.hunter_point.utils.response.data.ResponseData;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GenerateResponse {

	private static int SUCCESS_CODE = 20000;
	private static int ERROR_CODE = 10000;
	public static final int INVALID_TOKEN_ERROR_CODE = 400000;
	public static final int PERMISSION_DENIED_ERROR_CODE = 50001;

	public static <T> ListResponse<T> generateSuccessListResponse(Page<T> items) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items.getContent());
		data.setTotal(items.getTotalElements());
		res.setData(data);
		return res;
	}

	public static <T> ListResponse<T> generateErrorListResponse() {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(ERROR_CODE);
		res.setData(null);
		return res;
	}
	
	public static <T> ListResponse<T> generateErrorListResponse(String message) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(ERROR_CODE);
		res.setData(null);
		res.setMessage(message);
		return res;
	}

	public static <T> ListResponse<T> generateSuccessListResponse(List<T> items, long total) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items);
		data.setTotal(total);
		res.setData(data);
		return res;
	}

	public static <T> ListResponse<T> generateSuccessListResponse(List<T> items, long total, int currentPage, int totalPages, int numberOfElements, int size ) {
		ListResponse<T> listResponse = new ListResponse<T>();
		listResponse.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items);
		data.setTotal(total);
		data.setCurrentPage(currentPage);
		data.setTotalPages(totalPages);
		data.setNumberOfElements(numberOfElements);
		data.setSize(size);
		listResponse.setData(data);
		return listResponse;
	}

	public static <T> DetailResponse<T> generateSuccessDetailResponse(T item, String message) {
		DetailResponse<T> res = new DetailResponse<T>();
		res.setCode(200);
		res.setData(item);
		res.setMessage(message);
		return res;
	}

	public static <T> DetailResponse<T> generateErrorDetailResponse(int statuscode, String message) {
		DetailResponse<T> res = new DetailResponse<T>();
		res.setCode(statuscode);
		res.setMessage(message);
		return res;
	}

	public static <T> GetDetailResponse<T> generateSuccessGetDetailResponse(T item) {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(SUCCESS_CODE);
		res.setData(item);
		return res;
	}

	public static <T> GetDetailResponse<T> generateSuccessGetDetailResponse(T item, String message) {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(SUCCESS_CODE);
		res.setData(item);
		res.setMessage(message);
		return res;
	}

	public static <T> GetDetailResponse<T> generateErrorGetDetailResponse(T item) {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(SUCCESS_CODE);
		res.setData(item);
		return res;
	}
	
	public static <T> GetDetailResponse<T> generateErrorGetDetailResponse() {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(ERROR_CODE);
		return res;
	}

	public static <T> GetDetailResponse<T> generateErrorGetDetailResponse(String errors) {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(ERROR_CODE);
		res.setErrors(errors);
		res.setMessage(errors);
		return res;
	}

	public static SimpleResponse generateSuccessSimpleResponse() {
		SimpleResponse res = new SimpleResponse();
		res.setCode(SUCCESS_CODE);
		res.setData("success");
		return res;
	}

	public static SimpleResponse generateErrorSimpleResponse(String message) {
		SimpleResponse res = new SimpleResponse();
		res.setCode(ERROR_CODE);
		res.setData(message);
		res.setMessage(message);
		return res;
	}

	public static <T> ListResponse<T> generateInvalidTokenErrorListResponse() {
		ListResponse<T> res = new ListResponse<>();
		res.setCode(INVALID_TOKEN_ERROR_CODE);
		res.setData(null);
		return res;
	}

	public static <T> GetDetailResponse<T> generateInvalidTokenErrorGetDetailResponse(String errors) {
		GetDetailResponse<T> res = new GetDetailResponse<>();
		res.setCode(INVALID_TOKEN_ERROR_CODE);
		if (errors.equals("")) {
			res.setErrors("Invalid Token Error.");
		} else {
			res.setErrors(errors);
		}
		return res;
	}

	public static SimpleResponse generateInvalidTokenErrorSimpleResponse(String message) {
		SimpleResponse res = new SimpleResponse();
		res.setCode(INVALID_TOKEN_ERROR_CODE);
		if (message.equals("")) {
			res.setData("Invalid Token Error.");
		} else {
			res.setData(message);
		}
		return res;
	}

	public static <T> ListResponse<T> generatePermissionDeniedErrorListResponse() {
		ListResponse<T> res = new ListResponse<>();
		res.setCode(PERMISSION_DENIED_ERROR_CODE);
		res.setData(null);
		return res;
	}

	public static <T> GetDetailResponse<T> generatePermissionDeniedErrorGetDetailResponse(String errors) {
		GetDetailResponse<T> res = new GetDetailResponse<>();
		res.setCode(PERMISSION_DENIED_ERROR_CODE);
		if (errors.equals("")) {
			res.setErrors("Permission Denied Error.");
		} else {
			res.setErrors(errors);
		}
		return res;
	}

	public static SimpleResponse generatePermissionDeniedErrorSimpleResponse(String message) {
		SimpleResponse res = new SimpleResponse();
		res.setCode(PERMISSION_DENIED_ERROR_CODE);
		if (message.equals("")) {
			res.setData("Permission Denied Error.");
		} else {
			res.setData(message);
		}
		return res;
	}

	public static long getDifferenceDays(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static int getSUCCESS_CODE() {
		return SUCCESS_CODE;
	}

	public static int getERROR_CODE() {
		return ERROR_CODE;
	}

	
	
	
}
