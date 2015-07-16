package com.example.selftest.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.selftest.R;
import com.example.selftest.activities.RoomActivity;
import com.example.selftest.entity.AnchorInfo;
import com.example.selftest.utils.EnphasizeUtil;
import com.example.selftest.utils.image_utils.ImageUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AnchorItemAdapter extends BaseAdapter {

	private Context mContext;
	private List<AnchorInfo> anchors = new ArrayList<AnchorInfo>();

	public AnchorItemAdapter(Context context, AnchorInfo[] anchors) {
		this.mContext = context;
		if (anchors != null) {
			for (AnchorInfo anchorInfo : anchors) {
				this.anchors.add(anchorInfo);
			}
		}
	}

	@Override
	public int getCount() {
		return anchors.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_anchor, parent, false);

			viewHolder.textView_Name = (TextView) convertView
					.findViewById(R.id.tv_anchor_name);
			viewHolder.textView_Follows = (TextView) convertView
					.findViewById(R.id.tv_anchor_follows);
			viewHolder.textView_Status = (TextView) convertView
					.findViewById(R.id.tv_status);
			viewHolder.imageView_Avatar = (ImageView) convertView
					.findViewById(R.id.iv_avatar);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		AnchorInfo anchor = this.anchors.get(position);

		viewHolder.textView_Name.setText(EnphasizeUtil.transformText(
				anchor.getName(), "<em>", "</em>", 0xff00ff00));
		viewHolder.textView_Follows
				.setText(anchor.getDocTag().getFollow() + "");
		viewHolder.textView_Status.setText(anchor.getStatus());

		viewHolder.imageView_Avatar.setImageBitmap(null);
		ImageUtil.get(mContext).loadImage(viewHolder.imageView_Avatar, anchor
				.getAvatar() + "-big");

		convertView
				.setOnClickListener(new ListItemClickListener(anchor.getId()));

		return convertView;
	}

	class ViewHolder {
		ImageView imageView_Avatar;
		TextView textView_Name;
		TextView textView_Follows;
		TextView textView_Status;
	}

	class ListItemClickListener implements OnClickListener {
		private String id;

		ListItemClickListener(String id) {
			this.id = id;
			if (this.id.contains("-")) {
				this.id = this.id.substring(this.id.indexOf("-") + 1);
			}
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, RoomActivity.class);
			intent.putExtra(RoomActivity.ROOM_ID, id);
			mContext.startActivity(intent);
		}
	}

	public void setData(AnchorInfo[] anchors) {
		this.anchors.clear();
		if (anchors != null) {
			for (AnchorInfo anchorInfo : anchors) {
				this.anchors.add(anchorInfo);
			}
		}

		notifyDataSetChanged();
	}

	public void appendData(AnchorInfo[] anchors) {
		if (anchors != null) {
			for (AnchorInfo anchorInfo : anchors) {
				this.anchors.add(anchorInfo);
			}
		}
		notifyDataSetChanged();
	}
}
