package com.example.selftest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null) {
			String msg = intent.getStringExtra("MSG");
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}

	private void test(){
		int d = 1;
	}

    private void test1() {

    }

    private void test2() {

    }
}
