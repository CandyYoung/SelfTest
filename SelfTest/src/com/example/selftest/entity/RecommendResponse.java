package com.example.selftest.entity;

public class RecommendResponse extends BaseResponseEntity {
	private RecommendItem[] data;

	public RecommendItem[] getData() {
		return data;
	}

	public void setData(RecommendItem[] data) {
		this.data = data;
	}
}
