package com.example.selftest.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import com.example.selftest.R;
import com.example.selftest.adapters.AnchorItemAdapter;
import com.example.selftest.adapters.VideoItemAdapter;
import com.example.selftest.entity.AnchorInfo;
import com.example.selftest.entity.AnchorSearchResponse;
import com.example.selftest.entity.HttpAction;
import com.example.selftest.entity.LiveListResponse;
import com.example.selftest.entity.MyWebResponse;
import com.example.selftest.entity.RoomInfo;
import com.example.selftest.entity.RoomSearchResponse;
import com.example.selftest.utils.HttpUtil;
import com.example.selftest.utils.JsonUtil;
import com.example.selftest.utils.HttpUtil.WebRequestListener;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener,
		WebRequestListener {

	private EditText mEditTextKeyWord;
	private Button mButtonSearch;
	private Button mSegButtonRoom;
	private Button mSegButtonAnchor;
	private GridView mGridViewRooms;
	private ListView mListViewAnchors;

	private int selectedIndex = 0;

	private boolean isBusy_Room;
	private boolean isBusy_Anchor;
	private boolean noMore_Room;
	private boolean noMore_Anchor;
	private int currentPage_Room = 1;
	private int currentPage_Anchor = 1;
	private final int REQ_COUNT = 20;

	private VideoItemAdapter roomAdapter;
	private AnchorItemAdapter anchorAdapter;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			if (msg.what == HttpAction.SEARCH_ROOM.ordinal()) {
				if (currentPage_Room == 1) {
					roomAdapter.setData((RoomInfo[]) msg.obj);
				} else {
					roomAdapter.appendData((RoomInfo[]) msg.obj);
				}
			} else if (msg.what == HttpAction.SEARCH_ANCHOR.ordinal()) {
				if (currentPage_Anchor == 1) {
					anchorAdapter.setData((AnchorInfo[]) msg.obj);
				} else {
					anchorAdapter.appendData((AnchorInfo[]) msg.obj);
				}
			}

			return true;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		mEditTextKeyWord = (EditText) findViewById(R.id.et_key_word);
		mButtonSearch = (Button) findViewById(R.id.btn_search);
		mSegButtonRoom = (Button) findViewById(R.id.btn_program);
		mSegButtonAnchor = (Button) findViewById(R.id.btn_anchor);
		mGridViewRooms = (GridView) findViewById(R.id.gv_rooms);
		mListViewAnchors = (ListView) findViewById(R.id.lv_anchors);

		mButtonSearch.setOnClickListener(this);
		mSegButtonRoom.setOnClickListener(this);
		mSegButtonRoom.setSelected(true);
		mSegButtonRoom.setTextColor(getResources().getColor(R.color.white));
		mSegButtonAnchor.setSelected(false);
		mSegButtonAnchor.setTextColor(getResources().getColor(R.color.black));
		mSegButtonAnchor.setOnClickListener(this);

		roomAdapter = new VideoItemAdapter(this, null);
		mGridViewRooms.setAdapter(roomAdapter);

		anchorAdapter = new AnchorItemAdapter(this, null);
		mListViewAnchors.setAdapter(anchorAdapter);

	}

	@Override
	public void onComplete(MyWebResponse result) {
		Log.d("SearchResult", result.getResponseString());
		if (result.getAction() == HttpAction.SEARCH_ROOM) {
			isBusy_Room = false;
			RoomSearchResponse resp = JsonUtil.fromJson(
					result.getResponseString(),
					new TypeToken<RoomSearchResponse>() {
					}.getType());
			if (resp != null) {
				mHandler.obtainMessage(result.getAction().ordinal(),
						resp.getData()).sendToTarget();

				if (resp.getData().length < REQ_COUNT) {
					noMore_Room = true;
				} else {
					currentPage_Room++;
				}

				if (resp.getCode() == "1") {
					Toast.makeText(this, resp.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			}
		} else if (result.getAction() == HttpAction.SEARCH_ANCHOR) {
			isBusy_Anchor = false;
			AnchorSearchResponse resp = JsonUtil.fromJson(
					result.getResponseString(),
					new TypeToken<AnchorSearchResponse>() {
					}.getType());
			if (resp != null) {
				mHandler.obtainMessage(result.getAction().ordinal(),
						resp.getData()).sendToTarget();

//				insertData(resp.getData());

				if (resp.getData().length < REQ_COUNT) {
					noMore_Anchor = true;
				} else {
					currentPage_Anchor++;
				}

				if (resp.getCode() == "1") {
					Toast.makeText(this, resp.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			}
		}

	}

	private void insertData(AnchorInfo[] anchors) {
		for (AnchorInfo anchor : anchors) {
			ContentValues values = new ContentValues();
			values.put(AnchorInfo.NICKNAME, anchor.getName());
			values.put(AnchorInfo.AVATAR, anchor.getAvatar());
			getContentResolver().insert(AnchorInfo.CONTENT_URI, values);
		}
	}

	@Override
	public void onClick(View v) {

		if (v.equals(mButtonSearch)) {

			doSearch();

		} else if (v.equals(mSegButtonRoom)) {
			selectedIndex = 0;
			mGridViewRooms.setVisibility(View.VISIBLE);
			mListViewAnchors.setVisibility(View.GONE);
			mSegButtonRoom.setSelected(true);
			mSegButtonRoom.setTextColor(getResources().getColor(R.color.white));
			mSegButtonAnchor.setSelected(false);
			mSegButtonAnchor.setTextColor(getResources()
					.getColor(R.color.black));
		} else if (v.equals(mSegButtonAnchor)) {
			selectedIndex = 1;
			mGridViewRooms.setVisibility(View.GONE);
			mListViewAnchors.setVisibility(View.VISIBLE);
			mSegButtonRoom.setSelected(false);
			mSegButtonRoom.setTextColor(getResources().getColor(R.color.black));
			mSegButtonAnchor.setSelected(true);
			mSegButtonAnchor.setTextColor(getResources()
					.getColor(R.color.white));
		}
	}

	private void doSearch() {
		Editable text = mEditTextKeyWord.getText();
		if (TextUtils.isEmpty(text)) {
			Toast.makeText(this, "搜索關鍵詞不能爲空", Toast.LENGTH_SHORT).show();
			return;
		}
		String encodedText = text.toString();
		try {
			encodedText = URLEncoder.encode(encodedText, "utf-8");
			Toast.makeText(this, "Encoded Text:" + encodedText,
					Toast.LENGTH_SHORT).show();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (selectedIndex == 0) {
			if (!isBusy_Room) {
				isBusy_Room = true;
				HttpUtil.get(
						String.format(
								Locale.getDefault(),
								"http://www.zhanqi.tv/api/touch/search?t=live&q=%s&page=%d&nums=%d",
								encodedText, currentPage_Room, REQ_COUNT),
						HttpAction.SEARCH_ROOM, this);
			}
		} else {
			if (!isBusy_Anchor) {
				isBusy_Anchor = true;
				HttpUtil.get(
						String.format(
								Locale.getDefault(),
								"http://www.zhanqi.tv/api/touch/search?t=anchor&q=%s&page=%d&nums=%d",
								encodedText, currentPage_Anchor, REQ_COUNT),
						HttpAction.SEARCH_ANCHOR, this);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("SearchActivity", "OnKeyDown,Key:" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			doSearch();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
