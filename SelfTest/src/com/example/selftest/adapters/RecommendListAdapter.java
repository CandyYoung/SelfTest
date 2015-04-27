package com.example.selftest.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.selftest.R;
import com.example.selftest.entity.RecommendItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendListAdapter extends BaseAdapter {

	private List<RecommendItem> mSource = new ArrayList<RecommendItem>();
	private Context mContext;

	public RecommendListAdapter(RecommendItem[] items, Context context) {
		if (items != null) {
			for (RecommendItem recommendItem : items) {
				mSource.add(recommendItem);
			}
		}
		mContext = context;
	}

	public void setData(RecommendItem[] items) {
		if (mSource != null && items != null) {
			mSource.clear();
			for (RecommendItem recommendItem : items) {
				mSource.add(recommendItem);
			}
			
			notifyDataSetChanged();
		}
	}

	public void appendData(RecommendItem[] items) {
		if (mSource != null && items != null) {
			for (RecommendItem recommendItem : items) {
				mSource.add(recommendItem);
			}
			
			notifyDataSetChanged();
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

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_recommend_list, null);

			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.gv_videos = (GridView) convertView
					.findViewById(R.id.gv_videos);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RecommendItem item = mSource.get(position);
		holder.tv_title.setText(item.getTitle());
		holder.gv_videos.setAdapter(new VideoItemAdapter(mContext, item
				.getLists()));

		return convertView;
	}

	class ViewHolder {

		public TextView tv_title;

		public GridView gv_videos;
	}
}
