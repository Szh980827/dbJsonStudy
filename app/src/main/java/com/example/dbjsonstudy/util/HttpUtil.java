package com.example.dbjsonstudy.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by SongZhihao on 2019/4/15.
 */
public class HttpUtil {
	public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(address).build();
		client.newCall(request).equals(callback);
	}
}
