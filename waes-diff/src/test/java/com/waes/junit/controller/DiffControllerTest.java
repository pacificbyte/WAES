package com.waes.junit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.waes.controller.DiffController;
import com.waes.pojo.Message;
import com.waes.pojo.MessageKey;
import com.waes.service.DiffService;
import com.waes.util.TestUtil;

import net.minidev.json.JSONObject;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DiffController.class, secure = false)
public class DiffControllerTest {

	Message message;
    String id;
    JSONObject json;
    String encodedText;
    MessageKey key;
    
    
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DiffService diffService;
	
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
	public void getAllMessages() {
		try {
			String uri = "/v1/diff/all";
	    	
			when(diffService.getAll()).thenReturn(Arrays.asList(message));
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri);
			
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			String expected = "[{\"key\":{\"id\":\"1\",\"side\":\"left\"},\"text\":\"{\\\"country\\\":\\\"Netherlands\\\",\\\"company\\\":\\\"WAES\\\",\\\"industry\\\":\\\"Software\\\"}\"}]";
			assertEquals(expected, result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	@Test
	public void setLeftSide() {
		try {
			String uri = "/v1/diff/left";
			when(diffService.setMessage(
					Mockito.anyString(), 
					Mockito.anyString(), 
					Mockito.eq(Message.LEFT_SIDE))
			).thenReturn(message);
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.post(uri)
					.accept(MediaType.APPLICATION_JSON).content(encodedText)
					.contentType(MediaType.APPLICATION_JSON_VALUE);
			
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			
			assertEquals(200, result.getResponse().getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	@Test
	public void setRightSide() {
		try {
			String uri = "/v1/diff/right";
			when(diffService.setMessage(
					Mockito.anyString(), 
					Mockito.anyString(), 
					Mockito.eq(Message.RIGHT_SIDE))
			).thenReturn(message);
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.post(uri)
					.accept(MediaType.APPLICATION_JSON).content(encodedText)
					.contentType(MediaType.APPLICATION_JSON);
			
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			
			assertEquals(200, response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	@Test
	public void setMessage_empty() {
		try {
			String uri = "/v1/diff/left";
			when(diffService.setMessage(
					Mockito.anyString(), 
					Mockito.anyString(), 
					Mockito.eq(Message.LEFT_SIDE))
			).thenReturn(message);
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.post(uri)
					.accept(MediaType.APPLICATION_JSON).content("")
					.contentType(MediaType.APPLICATION_JSON_VALUE);
			
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			
			assertEquals(200, result.getResponse().getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	@Test
	public void setMessage_notEncoded() {
		try {
			String uri = "/v1/diff/left";
			when(diffService.setMessage(
					Mockito.anyString(), 
					Mockito.anyString(), 
					Mockito.eq(Message.LEFT_SIDE))
			).thenReturn(message);
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.post(uri)
					.accept(MediaType.APPLICATION_JSON).content("notEncodedText")
					.contentType(MediaType.APPLICATION_JSON_VALUE);
			
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			
			assertEquals(200, result.getResponse().getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	@Test
	public void getDiffs() {
		try {
			String uri = "/v1/diff/" + id;
	    	
			when(diffService.getDiffs(id)).thenReturn(message.getText());
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri);
			
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			String expected = json.toString();
			
			assertEquals(expected, result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
}
