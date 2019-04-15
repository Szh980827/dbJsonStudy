package com.example.dbjsonstudy.db;

import org.litepal.crud.DataSupport;

/**
 * Created by SongZhihao on 2019/4/15.
 */
public class County extends DataSupport {
	private int id;
	private String countyName;
	private int cityId;
	private String weatherID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getWeatherID() {
		return weatherID;
	}

	public void setWeatherID(String weatherID) {
		this.weatherID = weatherID;
	}
}
