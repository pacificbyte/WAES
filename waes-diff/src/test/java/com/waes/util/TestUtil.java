package com.waes.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

	public static String encodeMessage(String message) {
		if(message != null) {
			return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
		}
		return null;
    }
	
	public static String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objMapper = new ObjectMapper();
		return objMapper.writeValueAsString(object);
	}
}
