package com.powerlogix.models;

import java.util.List;

import org.springframework.validation.ObjectError;

public class ReturnMessage 
{
	private String message;
	private String error;
	private List<ObjectError> errors;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<ObjectError> getErrors() {
		return errors;
	}
	public void setErrors(List<ObjectError> errors) {
		this.errors = errors;
	}

	
	
	}
