package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/* 把对数据库的常见操作封装起来*/
public class CoolWeatherDB {
	
	//数据库名
	private static final String DB_NAME = "cool_weather";
	//数据库版本
	public static final int version = 1;

	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db ;
	
	//构造方法私有化
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, version);
		db = dbHelper.getWritableDatabase();
	}
	//获取coolWeatherDB实例
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null)
			coolWeatherDB = new CoolWeatherDB(context);
		return coolWeatherDB ;
	}
	//将province数据存储到数据库
	public void savaProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	//从数据库读取全国所有的省份信息
	public List<Province> loadProvince(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
        return list;		
	}
	
	//将City数据存储到数据库
	public void savaCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_Id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	//从数据库读取某省下所有的城市信息
	public List<City> loadCity(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?"
				, new String[] {String.valueOf(provinceId)}, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
        return list;		
	}
	//将county实例存储到数据库
	public void saveCounty(County county){
		if (county !=  null) {
			ContentValues values =  new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getId());
			db.insert("county", null, values);
		}
	}
	//从数据库读取某城市下的所有县信息
	public List<County> loadCounties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("county", null, "city_id = ?"
				, new String[]{String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		if(cursor !=  null)
			cursor.close();
		return list;
	}
}
