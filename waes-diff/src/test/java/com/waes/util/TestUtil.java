package com.waes.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class TestUtil {

	public static String encodeMessage(String message) {
		if(message != null) {
			return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
		}
		return null;
    }
	
	public static void putMessageInDB(String id, String encodedText, String side, MockMvc mockMvc) throws Exception {
		String uri = "/v1/diff/" + id +"/" + side;
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON).content(encodedText)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder).andReturn();
	}

}
