package com.example.selftest.entity;

public class RecommendItem {
	private String id;
	
	private String nums;
	
	private String title;
	
	private RoomInfo[] lists;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNums() {
		return nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public RoomInfo[] getLists() {
		return lists;
	}

	public void setLists(RoomInfo[] lists) {
		this.lists = lists;
	} 
}
