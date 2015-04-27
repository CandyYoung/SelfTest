package com.example.selftest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.selftest.entity.HttpAction;
import com.example.selftest.entity.MyWebResponse;

public class HttpUtil {

	public interface WebRequestListener {
		void onComplete(MyWebResponse result);
	}

	public static void get(final String url, final HttpAction action,
			final WebRequestListener callback) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
							url).openConnection();
					httpURLConnection.setConnectTimeout(3000);
					String result = "";

					if (httpURLConnection.getResponseCode() == 200) {

						InputStream inputStream = httpURLConnection
								.getInputStream();
						if (httpURLConnection
								.getHeaderField("Content-Encoding") == "gzip") {

							result = StreamConvertor
									.gzipStreamToString(inputStream);

						} else {

							result = StreamConvertor
									.inputStreamToString(inputStream);
						}
					}

					if (callback != null) {
						callback.onComplete(new MyWebResponse(result, action));
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
