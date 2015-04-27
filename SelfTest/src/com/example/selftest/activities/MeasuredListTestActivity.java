package com.example.selftest.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.selftest.R;
import com.example.selftest.widgets.LinearItemsControl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

public class MeasuredListTestActivity extends Activity {
	private ListView mListView;
	private MeasuredListAdpater mAdpater ;
	private List<Integer> data;
	
	{
		data = new ArrayList<Integer>();
        for (int i  = 0; i < 100; i++) {
            data.add(i % 6);
        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_test);
		
		/*mListView = (ListView) findViewById(R.id.measured_list);
		mAdpater = new MeasuredListAdpater(this, data);
		mListView.setAdapter(mAdpater);*/

        LinearItemsControl linearItemsControl = (LinearItemsControl) findViewById(R.id.items_control);
        linearItemsControl.setCallback(new LinearItemsControl.DrawItemCallback() {
            @Override
            public View getItemView(int position) {
                Log.d("DrawItemCallback", "getItemView, position="+position);
                View convertView = View.inflate(MeasuredListTestActivity.this, R.layout.item_rating, null);
                RatingBar rating = (RatingBar)convertView.findViewById(R.id.rating);
                rating.setRating(data.get(position));
                return convertView;
            }

            @Override
            public int getCount() {
                return data.size();
            }
        });
        linearItemsControl.drawItems();

		super.onCreate(savedInstanceState);

	}
}

class MeasuredListAdpater extends BaseAdapter {
	private Context mContext;
	private List<Integer> mDataSource;
	
	MeasuredListAdpater(Context context, List<Integer> data) {
		mContext = context;
		mDataSource = data;
	}

	@Override
	public int getCount() {
		return mDataSource.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataSource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("MeasuredListAdpater", "getView, position="+position);
		convertView = View.inflate(mContext, R.layout.item_rating, null);
		RatingBar rating = (RatingBar)convertView.findViewById(R.id.rating);
		rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

			}
		});
		rating.setRating(mDataSource.get(position));
		return convertView;
	}
	
}
