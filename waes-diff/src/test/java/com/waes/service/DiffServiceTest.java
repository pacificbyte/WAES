package com.waes.service;

import static org.junit.Assert.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.waes.pojo.Message;
import com.waes.pojo.MessageKey;
import com.waes.repository.MessageRepository;
import com.waes.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffServiceTest {

	@Autowired
    private DiffService diffService;
	
	@MockBean
	private MessageRepository msgRepository;
    
	String id;
	MessageKey key1;
	MessageKey key2;
	Message msg1;
	Message msg2;
	JSONObject json;
    
    @Before
    public void init() {
    	id = "1";
    	key1 = new MessageKey(id, Message.LEFT_SIDE);
    	key2 = new MessageKey(id, Message.RIGHT_SIDE);
    }
    
    @Test
    public void compareSides_sameMessages() {
    	try {
    		msg1 = new Message(key1, TestUtil.encodeMessage("{\"left\":\"message\"}"));
    		String left = msg1.getText();
        	String right = msg1.getText();
        	String expected = new JSONObject().put("equals", true).toString();
			
        	assertEquals(expected, diffService.compareSides(left, right));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void compareSides_notJSONMessage() {
    	try {
    		msg1 = new Message(key1, TestUtil.encodeMessage("\"left\":\"message\""));
    		msg2 = new Message(key1, TestUtil.encodeMessage("\"left\":\"message\""));
    		String left = msg1.getText();
        	String right = msg2.getText();
        	String expected = new JSONObject().put("error", "Error when converting the JSon object into Map.").toString();
			
        	assertEquals(expected, diffService.compareSides(left, right));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void compareSides_differentMessages() {
    	try {
    		msg1 = new Message(key1, TestUtil.encodeMessage("{\"left\":\"message\",\"key\":\"value\"}"));
        	msg2 = new Message(key2, TestUtil.encodeMessage("{\"right\":\"message\"}"));
    		String left = msg1.getText();
        	String right = msg2.getText();
        	String expected = new JSONObject().put("size", "The messages have different size").toString();
			
        	assertEquals(expected, diffService.compareSides(left, right));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void compareSides_sameSize() {
    	try {
    		msg1 = new Message(key1, TestUtil.encodeMessage("{\"left\":\"message\"}"));
        	msg2 = new Message(key2, TestUtil.encodeMessage("{\"right\":\"message\"}"));
    		String left = msg1.getText();
        	String right = msg2.getText();
        	JSONObject json = new JSONObject();
        	json.put("equals", false);
        	json.put("key-right", "Doesn't exist in left message.");
        	json.put("key-left", "Doesn't exist in right message.");
        	String expected = json.toString();
			
        	assertEquals(expected, diffService.compareSides(left, right));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void compareSides_sameSizeDiffValues() {
    	try {
    		msg1 = new Message(key1, TestUtil.encodeMessage("{\"left\":\"left message\"}"));
        	msg2 = new Message(key2, TestUtil.encodeMessage("{\"left\":\"right message\"}"));
    		String left = msg1.getText();
        	String right = msg2.getText();
        	JSONObject json = new JSONObject();
        	json.put("right-value", "right message");
        	json.put("right-value-length", 13);
        	json.put("equals", false);
        	json.put("left-value", "left message");
        	json.put("left-value-length", 12);
        	String expected = json.toString();
			
        	assertEquals(expected, diffService.compareSides(left, right));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void compareSides_nullMessages() {
    	try {
    		String left = null;
        	String right = null;
        	JSONObject json = new JSONObject();
        	json.put("error", "An exception has ocurred.");
        	String expected = json.toString();
			
        	assertEquals(expected, diffService.compareSides(left, right));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void compareSides_emptyMessages() {
    	try {
    		String left = "";
        	String right = "";
        	JSONObject json = new JSONObject();
        	json.put("error", "Error when converting the JSon object into Map.");
        	String expected = json.toString();
			
        	assertEquals(expected, diffService.compareSides(left, right));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
}
