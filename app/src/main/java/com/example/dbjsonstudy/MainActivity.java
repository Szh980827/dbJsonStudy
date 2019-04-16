package com.example.dbjsonstudy;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbjsonstudy.db.City;
import com.example.dbjsonstudy.db.County;
import com.example.dbjsonstudy.db.Province;
import com.example.dbjsonstudy.util.HttpUtil;
import com.example.dbjsonstudy.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

	/*
	 * 设置列表级别
	 */
	private static final int LEVEL_PROVINCE = 0;
	private static final int LEVEL_CITY = 1;
	private static final int LEVEL_COUNTY = 2;

	/*
	 * 声明相关控件
	 */
	private ProgressDialog progressDialog;
	private Button but_addCityBack;
	private TextView tv_addCityTitle;
	private ListView listView;

	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<>();
	/*
	 * 省市县列表
	 */
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;

	/*
	 * 选中的省市县
	 */
	private Province selectedProvince;
	private City selectedCity;
	private County selectedCounty;

	/*
	 * 当前选中级别
	 */
	private int currentLevel;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		 * 隐藏标题栏
		 */
		ActionBar actionbar = getSupportActionBar();
		if (actionbar != null) {
			actionbar.hide();
		}
		/*
		 * 初始化控件
		 */
		but_addCityBack = findViewById(R.id.addcity_back);
		tv_addCityTitle = findViewById(R.id.addcity_title_tv);
		listView = findViewById(R.id.listView);
		/*
		 * button 单击事件
		 */
		but_addCityBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentLevel == LEVEL_PROVINCE) {
					Log.d("MainActivity", "已返回到上一级界面");
					finish();
				} else if (currentLevel == LEVEL_CITY) {
					queryProvinces();
				} else if (currentLevel == LEVEL_COUNTY) {
					queryCities();
				}

			}
		});
		/*
		 listView 单击事件
		 */
		adapter = new ArrayAdapter<>(listView.getContext(), android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_COUNTY) {
					selectedCounty = countyList.get(position);
					String areaName = selectedCounty.getCountyName();
					String areaID = selectedCounty.getId() + "";
					Toast.makeText(MainActivity.this, areaName + "++" + areaID, Toast.LENGTH_SHORT).show();
				}
			}
		});
		queryProvinces();
	}


	/*
	 * 查询全国的省市县，优先从数据库查询，如果没有再去服务器上查询
	 */

	private void queryProvinces() {
		tv_addCityTitle.setText("中国");
		but_addCityBack.setVisibility(View.VISIBLE);
		provinceList = DataSupport.findAll(Province.class);
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
				Log.d("MainActivity", province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_PROVINCE;
		} else {
			String address = "http://guolin.tech/api/china";
			queryFromServer(address, "province");
		}

	}

	private void queryCities() {
		tv_addCityTitle.setText(selectedProvince.getProvinceName());
		but_addCityBack.setVisibility(View.VISIBLE);
		cityList = DataSupport.where("provinceid=?", String.valueOf(selectedProvince.getId())).find(City.class);
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_CITY;
		} else {
			int provinceCode = selectedProvince.getProvinceCode();
			String address = "http://guolin.tech/api/china/" + provinceCode;
			queryFromServer(address, "city");
		}
	}


	private void queryCounties() {
		tv_addCityTitle.setText(selectedCity.getCityName());
		but_addCityBack.setVisibility(View.VISIBLE);
		countyList = DataSupport.where("cityid=?", String.valueOf(selectedCity.getId())).find(County.class);
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				currentLevel = LEVEL_COUNTY;
			}
		} else {
			int provinceCode = selectedProvince.getProvinceCode();
			int cityCode = selectedCity.getCityCode();
			String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
			queryFromServer(address, "county");
		}

	}

	/*
	 * 根据传入的地址和类型从服务器上查询 省市县数据
	 */
	private void queryFromServer(String address, final String type) {
		Log.d("MainActivity", "进入queryFromServer");
		showProgressDialog();
		HttpUtil.sendOkHttpRequest(address, new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String responseText = response.body().string();
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvinceResponse(responseText);
				} else if ("city".equals(type)) {
					result = Utility.handleCityResponse(responseText, selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountyResopnse(responseText, selectedCity.getId());
				}
				if (result) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals((type))) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onFailure(Call call, IOException e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	/*
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/*
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}


}
