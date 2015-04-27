package com.example.selftest.entity;

public class LiveListResponse extends BaseResponseEntity {
	
	public class Data{
		private RoomInfo[] rooms;
		
		private int cnt;

		public RoomInfo[] getRooms() {
			return rooms;
		}

		public void setRooms(RoomInfo[] rooms) {
			this.rooms = rooms;
		}

		public int getCnt() {
			return cnt;
		}

		public void setCnt(int cnt) {
			this.cnt = cnt;
		}
	}
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
