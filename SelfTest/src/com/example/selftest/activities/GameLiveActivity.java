package com.example.selftest.activities;

import com.example.selftest.R;
import com.example.selftest.fragments.LiveFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class GameLiveActivity extends FragmentActivity {

	public static final String GAME_ID = "game_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_lives);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			Toast.makeText(this, "未传Game ID", Toast.LENGTH_SHORT).show();
			return;
		}
		String gId = bundle.getString(GAME_ID);
		if (gId == null) {
			Toast.makeText(this, "未传Game ID", Toast.LENGTH_SHORT).show();
			return;
		}
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fl_lives, new LiveFragment(gId)).commit();
	}
}
