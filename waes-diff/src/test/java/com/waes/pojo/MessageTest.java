package com.waes.pojo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.waes.util.TestUtil;

import net.minidev.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageTest {
    
    Message message;
    String id;
    
    @Before
    public void init() {
    	id = "1";
    }
    
    
    @Test
    public void decodeMessage() {
        JSONObject json = new JSONObject();
    	json.put("company", "WAES");
    	json.put("country", "Netherlands");
    	json.put("industry", "Software");
    	String encodedText = TestUtil.encodeMessage(json.toString());
    	MessageKey key = new MessageKey(id, Message.LEFT_SIDE);
    	this.message = new Message(key, encodedText);
    	String expected = "{\"country\":\"Netherlands\",\"company\":\"WAES\",\"industry\":\"Software\"}";
    	
    	assertEquals(expected, this.message.getText());
    }
    
    @Test
    public void decodeMessage_notEncoded() {
        JSONObject json = new JSONObject();
    	json.put("company", "WAES");
    	json.put("country", "Netherlands");
    	json.put("industry", "Software");
    	String encodedText = json.toString();
    	MessageKey key = new MessageKey(id, Message.LEFT_SIDE);
    	this.message = new Message(key, encodedText);
    	String expected = "Message cannot be decoded with Base64";
    	
    	assertEquals(expected, this.message.getText());
    }
    
    @Test
    public void decodeMessage_notJSON() {
    	String encodedText = TestUtil.encodeMessage("This is not a JSON string");
    	MessageKey key = new MessageKey(id, Message.LEFT_SIDE);
    	this.message = new Message(key, encodedText);
    	String expected = "This is not a JSON string";
    	
    	assertEquals(expected, this.message.getText());
    }
    
    @Test
    public void decodeMessage_nullEncodedMessage() {
    	String encodedText = TestUtil.encodeMessage(null);
    	MessageKey key = new MessageKey(id, Message.LEFT_SIDE);
    	this.message = new Message(key, encodedText);
    	String expected = "";
    	
    	assertEquals(expected, this.message.getText());
    }
    
    @Test
    public void decodeMessage_nullNotEncodedMessage() {
    	String encodedText = null;
    	MessageKey key = new MessageKey(id, Message.LEFT_SIDE);
    	this.message = new Message(key, encodedText);
    	String expected = "";
    	
    	assertEquals(expected, this.message.getText());
    }
}
