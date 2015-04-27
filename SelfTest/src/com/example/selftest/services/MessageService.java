package com.example.selftest.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import com.example.selftest.R;
import com.example.selftest.activities.MainActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MessageService extends Service {
	// 获取消息线程
	private MessageThread messageThread = null;

	// 点击查看
	private Intent messageIntent = null;
	private PendingIntent messagePendingIntent = null;

	// 通知栏消息
	private int messageNotificationID = 1000;
	private Notification messageNotification = null;
	private NotificationManager messageNotificatioManager = null;
	
	private Notification.Builder builder;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 初始化
		messageNotification = new Notification();
		messageNotification.icon = R.drawable.home_icon_selector;
		messageNotification.tickerText = "新消息1111";
		messageNotification.defaults = Notification.DEFAULT_SOUND;
		messageNotification.flags = Notification.FLAG_AUTO_CANCEL;
		messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		messageIntent = new Intent(this, MainActivity.class);
		
		builder = new Notification.Builder(MessageService.this);
		builder.setSmallIcon(R.drawable.home_icon_selector);
		builder.setAutoCancel(true);
		builder.setDefaults(Notification.DEFAULT_SOUND);

		// 开启线程
		messageThread = new MessageThread();
		messageThread.isRunning = true;
		messageThread.start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		 System.exit(0);
//		messageThread.isRunning = false;
		super.onDestroy();
	}

	/**
	 * 从服务器端获取消息
	 * 
	 */
	class MessageThread extends Thread {
		// 运行状态，下一步骤有大用
		public boolean isRunning = true;

		@SuppressLint("NewApi")
		public void run() {
			while (isRunning) {
				try {
					
					// 获取服务器消息
					String serverMessage = getServerMessage();
					if (serverMessage != null && !"".equals(serverMessage)) {

						messagePendingIntent = PendingIntent.getActivity(MessageService.this, 0,
								messageIntent, PendingIntent.FLAG_CANCEL_CURRENT);
						// 更新通知栏
						builder.setTicker("新消息111");
						builder.setContentIntent(messagePendingIntent);
						builder.setContentText("奥巴马宣布,本拉登兄弟挂了!"
										+ serverMessage);
						builder.setContentTitle("新消息");
						
						builder.setShowWhen(true);
						builder.setWhen(Calendar.getInstance().getTimeInMillis());
//						messageNotification.setLatestEventInfo(
//								MessageService.this, "新消息", "奥巴马宣布,本拉登兄弟挂了!"
//										+ serverMessage, messagePendingIntent);
//						messageNotificatioManager.notify(messageNotificationID,
//								messageNotification);
						messageNotificatioManager.notify(messageNotificationID, builder.build());
						// 每次通知完，通知ID递增一下，避免消息覆盖掉
						messageNotificationID++;
					}

					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getServerMessage() {
		return "YES!";
	}
}
