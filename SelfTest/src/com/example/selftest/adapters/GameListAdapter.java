package com.example.selftest.adapters;

import java.util.ArrayList;

import com.example.selftest.R;
import com.example.selftest.activities.GameLiveActivity;
import com.example.selftest.entity.GameInfo;
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

public class GameListAdapter extends BaseAdapter {
	
	public interface LoadMoreCallback {
		void loadMore();
	}

	private Context mContext;
	private ArrayList<GameInfo> mSource = new ArrayList<GameInfo>();
	private LoadMoreCallback loadMoreCallback;

	public void setCallback(LoadMoreCallback callback) {
		this.loadMoreCallback = callback;
	}

	public GameListAdapter(Context context) {
		mContext = context;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSource.size();
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
		GameInfo item = mSource.get(position);

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_game, parent, false);

			holder.iv_game_icon = (ImageView) convertView
					.findViewById(R.id.iv_game_icon);
			holder.tv_game_name = (TextView) convertView
					.findViewById(R.id.tv_game_name);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}

		holder.iv_game_icon.setImageBitmap(null);
		ImageUtil.get(mContext).loadImage(holder.iv_game_icon, item.getSpic());
		holder.tv_game_name.setText(item.getName());
		convertView.setOnClickListener(new GameIconClickListener(item.getId()));

		return convertView;
	}
	
	public void setData(GameInfo[] games){
		mSource.clear();
		for (GameInfo gameInfo : games) {
			mSource.add(gameInfo);
		}
		
		notifyDataSetChanged();
	}
	
	public void appendData(GameInfo[] games) {
		for (GameInfo gameInfo : games) {
			mSource.add(gameInfo);
		}
		
		notifyDataSetChanged();
	}

	class ViewHolder {
		ImageView iv_game_icon;
		TextView tv_game_name;
	}

	class GameIconClickListener implements OnClickListener {
		private String gameId;

		GameIconClickListener(String gameId) {
			this.gameId = gameId;
		}

		@Override
		public void onClick(View v) {
			 Intent intent = new Intent(mContext, GameLiveActivity.class);
			 intent.putExtra(GameLiveActivity.GAME_ID, gameId);
			 mContext.startActivity(intent);
		}

	}
}
