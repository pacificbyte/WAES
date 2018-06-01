package com.waes.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.waes.WaesDiffApplication;
import com.waes.pojo.Message;
import com.waes.pojo.MessageKey;
import com.waes.util.TestUtil;

import net.minidev.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = WaesDiffApplication.class
)
@AutoConfigureMockMvc
public class WaesDiffApplicationIT {

	Message message;
    String id;
    JSONObject json;
    String encodedText;
    MessageKey key;
    
	
	@Autowired
	private MockMvc mockMvc;
	
	@Before
	public void init() {
		id = "1";
		json = new JSONObject();
    	json.put("company", "WAES");
    	json.put("country", "Netherlands");
    	json.put("industry", "Software");
    	encodedText = TestUtil.encodeMessage(json.toString());
    	key = new MessageKey(id, Message.LEFT_SIDE);
    	message = new Message(key, encodedText);
	}
	
	
	@Test
	public void sendMessage_left() throws Exception {
		
		String uri = "/v1/diff/" + id +"/left";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON).content(encodedText)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println(result.getResponse().getStatus());
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void sendMessage_right() throws Exception {
		
		String uri = "/v1/diff/" + id +"/right";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON).content(encodedText)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println(result.getResponse().getStatus());
		assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	public void sendMessage_notEncoded() throws Exception {
		
		String uri = "/v1/diff/" + id +"/left";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON).content("notEncodedText")
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println(result.getResponse().getStatus());
		assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test
	public void sendMessage_empty() throws Exception {
		
		String uri = "/v1/diff/" + id +"/right";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(uri)
				.accept(MediaType.APPLICATION_JSON).content("")
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println(result.getResponse().getStatus());
		assertEquals(400, result.getResponse().getStatus());
	}
	
	@Test
	public void getAllMessages() throws Exception {
		TestUtil.putMessageInDB(id, encodedText, Message.LEFT_SIDE, mockMvc);
		TestUtil.putMessageInDB(id, encodedText, Message.RIGHT_SIDE, mockMvc);
		
		String uri = "/v1/diff/all";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(uri)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{\"key\":{\"id\":\"1\",\"side\":\"left\"},\"text\":\"{\\\"country\\\":\\\"Netherlands\\\",\\\"company\\\":\\\"WAES\\\",\\\"industry\\\":\\\"Software\\\"}\"},"
						+ "{\"key\":{\"id\":\"1\",\"side\":\"right\"},\"text\":\"{\\\"country\\\":\\\"Netherlands\\\",\\\"company\\\":\\\"WAES\\\",\\\"industry\\\":\\\"Software\\\"}\"}]";
		assertEquals(expected, result.getResponse().getContentAsString());
	}
	

	@Test
	public void getDifferences_sameMessages() throws Exception {
		
		String uri = "/v1/diff/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("equals", true);
		String expected = jsonResult.toString();
		
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(expected, result.getResponse().getContentAsString());
	}
	
	@Test
	public void getDifferences_diffLengthMessages() throws Exception {
		json = new JSONObject();
    	json.put("company", "SomeCompany");
    	json.put("country", "USA");
    	String encodedText2 = TestUtil.encodeMessage(json.toString());
    	
    	TestUtil.putMessageInDB(id, encodedText, Message.LEFT_SIDE, mockMvc);
		TestUtil.putMessageInDB(id, encodedText2, Message.RIGHT_SIDE, mockMvc);
    	
		String uri = "/v1/diff/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("size", "The messages have different size");
		String expected = jsonResult.toString();
		
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(expected, result.getResponse().getContentAsString());
	}
	
	@Test
	public void getDifferences_diffMessages() throws Exception {
		json = new JSONObject();
    	json.put("company", "SomeCompany");
    	json.put("country", "USA");
    	json.put("industry", "Software");
    	String encodedText2 = TestUtil.encodeMessage(json.toString());
    	
    	TestUtil.putMessageInDB(id, encodedText, Message.LEFT_SIDE, mockMvc);
		TestUtil.putMessageInDB(id, encodedText2, Message.RIGHT_SIDE, mockMvc);
    	
		String uri = "/v1/diff/" + id;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("right-value", "SomeCompany");
		jsonResult.put("right-value-length", 11);
		jsonResult.put("equals", false);
		jsonResult.put("left-value", "WAES");
		jsonResult.put("left-value-length", 4);
		String expected = jsonResult.toString();
		
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(expected, result.getResponse().getContentAsString());
	}
}
