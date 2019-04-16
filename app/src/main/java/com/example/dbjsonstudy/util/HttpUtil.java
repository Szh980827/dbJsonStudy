package com.example.dbjsonstudy.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SongZhihao on 2019/4/15.
 */
public class HttpUtil {
	public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
		Log.d("HttpUtil", "进入sendOKHTTPRequest");
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(address).build();
		client.newCall(request).enqueue(callback);
	}
}
