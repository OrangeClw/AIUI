package com.iflytek.aiui.utils;

import java.util.UUID;

public class UuidUtils {
	public static String getUuid32() {
		return UUID.randomUUID().toString().replace("-", "");        
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
}
