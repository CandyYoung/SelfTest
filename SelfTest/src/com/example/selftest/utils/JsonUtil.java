package com.example.selftest.utils;


import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author 海强
 *
 */
public class JsonUtil {
	
	/**将object转换成Json串
	 * @param obj
	 * @return
	 */
	public static <T> String toJson(Object obj){
		Gson gson = new Gson();
		return gson.toJson(obj, new TypeToken<T>(){}.getType());
	}
	
	
	/**将Json串转换成object
	 * @param json
	 * @return
	 */
	public static <T> T fromJson(String json,Type type){
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}
	
	/**将Json串转换成object
	 * @param json
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> cla){
		Gson gson = new Gson();
		return gson.fromJson(json, cla);
	}
}
