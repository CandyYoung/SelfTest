package com.example.selftest.entity;

public class RoomSearchResponse extends BaseResponseEntity {
	private RoomInfo[] data;

	public RoomInfo[] getData() {
		return data;
	}

	public void setData(RoomInfo[] data) {
		this.data = data;
	}
}
