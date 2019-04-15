package com.example.dbjsonstudy.util;

import android.text.TextUtils;

import com.example.dbjsonstudy.db.City;
import com.example.dbjsonstudy.db.County;
import com.example.dbjsonstudy.db.Province;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SongZhihao on 2019/4/15.
 */
public class Utility {
	/*
	 * 解析和处理服务器返回的省级、市级、县级数据
	 */
	public static boolean handleProvinceResponse(String response) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONArray allProvince = new JSONArray(response);
				for (int i = 0; i < allProvince.length(); i++) {
					JSONObject provinceObject = allProvince.getJSONObject(i);
					Province province = new Province();
					province.setProvinceName(provinceObject.getString("name"));
					province.setProvinceCode(provinceObject.getInt("id"));
					province.save();
				}
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean handleCityResponse(String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONArray allCities = new JSONArray(response);
				for (int i = 0; i < allCities.length(); i++) {
					JSONObject cityObject = allCities.getJSONObject(i);
					City city = new City();
					city.setCityName(cityObject.getString("name"));
					city.setCityCode(cityObject.getInt("id"));
					city.setProvinceID(provinceId);
					city.save();
				}
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean handleCountyResopnse(String response, int cityID) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONArray allCounties = new JSONArray(response);
				for (int i = 0; i < allCounties.length(); i++) {
					JSONObject countyObject = allCounties.getJSONObject(i);
					County county = new County();
					county.setCountyName(countyObject.getString("name"));
					county.setCityId(cityID);
					county.setWeatherID(countyObject.optString("weather_id"));
					county.save();
				}
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
