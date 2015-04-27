package com.example.selftest.entity;

public class MyWebResponse {
	private String responseString;
	private HttpAction action;

	public MyWebResponse(String resString, HttpAction action) {
		this.responseString = resString;
		this.action = action;
	}

	public String getResponseString() {
		return responseString;
	}

	public HttpAction getAction() {
		return action;
	}
}
