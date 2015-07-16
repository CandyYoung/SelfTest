package com.example.selftest.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.selftest.R;
import com.example.selftest.activities.RoomActivity;
import com.example.selftest.entity.RoomInfo;
import com.example.selftest.utils.EnphasizeUtil;
import com.example.selftest.utils.image_utils.ImageUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoItemAdapter extends BaseAdapter {

	public interface LoadMoreCallback {
		void loadMore();
	}

	private Context mContext;
	private List<RoomInfo> mSource;
	private LoadMoreCallback loadMoreCallback;

	public void setCallback(LoadMoreCallback callback) {
		this.loadMoreCallback = callback;
	}

	public VideoItemAdapter(Context context, RoomInfo[] items) {
		mContext = context;
		mSource = new ArrayList<RoomInfo>();
		if (items != null) {
			for (RoomInfo roomInfo : items) {
				mSource.add(roomInfo);
			}
		}
	}

	@Override
	public int getCount() {
		if (mSource != null) {
			return mSource.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (position >= mSource.size() - 2 && loadMoreCallback != null) {
			loadMoreCallback.loadMore();
		}

		ViewHolder holder = null;
		RoomInfo room = mSource.get(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_room_gridview, parent, false);

			holder = new ViewHolder();
			holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_nickname = (TextView) convertView
					.findViewById(R.id.tv_nickname);
			holder.tv_onlineCount = (TextView) convertView
					.findViewById(R.id.tv_onlineCount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.iv_pic.setImageBitmap(null);
		String picUrl = room.getBpic();
		if (picUrl == null) {
			picUrl = room.getSpic();
		}
		if (picUrl != null) {
			ImageUtil.get(mContext).loadImage(holder.iv_pic, picUrl);
		}
		holder.tv_title.setText(EnphasizeUtil.transformText(room.getTitle(),
				"<em>", "</em>", 0xff00ff00));
		String nick = room.getNickname();
		if (room.getGender() != null) {
			if (room.getGender().equals("2")) {
				nick = "♂" + nick;
			} else if (room.getGender().equals("1")) {
				nick = "♀" + nick;
			}
		}
		holder.tv_nickname.setText(EnphasizeUtil.transformText(nick, "<em>",
				"</em>", 0xff00ff00));
		if (room.getOnline() != null) {
			holder.tv_onlineCount.setText("♞" + room.getOnline());
		}
		String roomId = room.getId();
		if (roomId != null && roomId.contains("-")) {
			roomId = roomId.substring(roomId.indexOf("-") + 1);
		}
		convertView.setOnClickListener(new RoomItemClickListener(roomId));

		return convertView;
	}
	
	public List<RoomInfo> getData() {
		return mSource;
	}

	public void setData(RoomInfo[] rooms) {
		if (mSource == null) {
			mSource = new ArrayList<RoomInfo>();
		} else {
			mSource.clear();
		}

		for (RoomInfo room : rooms) {
			mSource.add(room);
		}

		notifyDataSetChanged();
	}

	public void appendData(RoomInfo[] rooms) {
		if (mSource == null) {
			mSource = new ArrayList<RoomInfo>();
		}

		for (RoomInfo room : rooms) {
			mSource.add(room);
		}

		notifyDataSetChanged();
	}

	class ViewHolder {

		public ImageView iv_pic;
		public TextView tv_title;
		public TextView tv_nickname;
		public TextView tv_onlineCount;
	}

	class RoomItemClickListener implements OnClickListener {
		private String roomId;

		public RoomItemClickListener(String roomId) {
			this.roomId = roomId;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, RoomActivity.class);
			intent.putExtra(RoomActivity.ROOM_ID, roomId);
			mContext.startActivity(intent);

		}

	}
}
