package com.example.selftest.entity;

public class GameListResponse extends BaseResponseEntity {
	public class Data{
		private GameInfo[] games;
		
		private int cnt;

		public GameInfo[] getGames() {
			return games;
		}

		public void setGames(GameInfo[] games) {
			this.games = games;
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
