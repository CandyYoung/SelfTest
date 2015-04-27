package com.example.selftest.entity;

public class AnchorSearchResponse extends BaseResponseEntity {
		
	private AnchorInfo[] data;


	public AnchorInfo[] getData() {
		return data;
	}

	public void setData(AnchorInfo[] data) {
		this.data = data;
	}
}
