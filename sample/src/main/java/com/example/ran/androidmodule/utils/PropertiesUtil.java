package com.example.ran.androidmodule.utils;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @作者          吴孝然
 * @创建日期      2019/2/11 10:36
 * @描述          配置工具类
 **/
public class PropertiesUtil {
	private final static String DEFAULT_PROPERTY_PATH = "/host.properties";

	private static PropertiesUtil instance = null;
	private static Properties properties = null;

	
	public PropertiesUtil() {
		instance = this;
		properties = new Properties();
		InputStream in=null;
		try {
			in = PropertiesUtil.class.getResourceAsStream(DEFAULT_PROPERTY_PATH);
			properties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}

		}
	}

	public static PropertiesUtil getInstance() {
		if (instance == null) {
			instance = new PropertiesUtil();
		}
		return instance;
	}

	public String getProp(String prop) {
		return properties.getProperty(prop);
	}
	
	public boolean getBooleanProp(String prop) {
	    String result = properties.getProperty(prop);
	    if(!TextUtils.isEmpty(result) && result.equalsIgnoreCase("true")) {
	        return true;
	    } else {
	        return false;
	    }
	}
}
