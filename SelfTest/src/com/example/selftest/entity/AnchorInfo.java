package com.example.selftest.entity;

import android.net.Uri;
import android.provider.BaseColumns;

public class AnchorInfo implements BaseColumns {
	private String nickname;
	private String id;
	private String status;
	private DocTag docTag;
	private String avatar;
	
	public static final String AUTHORITY = "com.example.selftest.Anchors";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/anchor");
	public static final String DEFAULT_SORT_ORDER = "name DESC";// 按姓名排序
	
	public static String NICKNAME = "NickName";
	public static String AVATAR = "Avatar";

	public String getName() {
		return nickname;
	}

	public void setName(String name) {
		this.nickname = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DocTag getDocTag() {
		return docTag;
	}

	public void setDocTag(DocTag docTag) {
		this.docTag = docTag;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public class DocTag {
		private String online;
		private int follows;
		private String gender;
		private String duration;

		public String getOnline() {
			return online;
		}

		public void setOnline(String online) {
			this.online = online;
		}

		public int getFollow() {
			return follows;
		}

		public void setFollow(int follow) {
			this.follows = follow;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getDuration() {
			return duration;
		}

		public void setDuration(String duration) {
			this.duration = duration;
		}
	}
}
