package com.example.dbjsonstudy.db;

import org.litepal.crud.DataSupport;

/**
 * Created by SongZhihao on 2019/4/15.
 */
public class City extends DataSupport {
	private int id;
	private String cityName;
	private int cityCode;
	private int provinceID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getCityCode() {
		return cityCode;
	}

	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}

	public int getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}
}