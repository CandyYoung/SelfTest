package com.example.selftest.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

import com.example.selftest.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTaskTestActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_test);

        /*findViewById(R.id.btn_task1).setOnClickListener(this);*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn_task1:
                for (int i = 1; i < 20; i++) {
                    new MyAsyncTask(i).executeOnExecutor(Executors.newFixedThreadPool(3), 11);
                }

                break;*/
        }
    }

    class MyAsyncTask extends AsyncTask {
        private static final String TAG = "MyAsyncTask";
        private int index;

        MyAsyncTask(int index) {
            this.index = index;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Log.d(TAG, index + " is doInBackground");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(50);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    }

}
