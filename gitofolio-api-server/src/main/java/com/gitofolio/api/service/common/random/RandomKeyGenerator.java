package com.gitofolio.api.service.common.random;

import java.util.UUID;

public class RandomKeyGenerator{
	
	public static String generateKey(){
		return generateKey(1);
	}
	
	public static String generateKey(int secureCount){
		if(secureCount > 10) secureCount = 10;
		StringBuilder key = new StringBuilder();
		for(int i = 0; i < secureCount; i++) key.append(UUID.randomUUID().toString().replaceAll("-","").replaceAll("\\+",""));
		return key.toString();
	}
}