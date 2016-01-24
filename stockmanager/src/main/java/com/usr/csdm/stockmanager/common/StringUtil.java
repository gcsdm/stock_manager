package com.csdm.util;

public class StringUtil {

	public static long string2Long(String val){
		if(val != null){
			return Long.parseLong(val.trim().replaceAll(",", ""));
		}else{
			return 0;
		}
	}
	
	public static float string2Float(String val){
		if(val != null){
			return Float.parseFloat(val.trim().replaceAll(",", ""));
		}else{
			return 0;
		}
	}	
	public static int string2Int(String val){
		if(val != null){
			return Integer.parseInt(val.trim().replaceAll(",", ""));
		}else{
			return 0;
		}
	}
	public static String null2Trim(String value){
		if(value != null){
			return value.trim();
		}else{
			return "";
		}
	}
}
