package com.waes.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.pojo.Message;
import com.waes.pojo.MessageKey;
import com.waes.repository.MessageRepository;

/**
 * @author ccamarena
 *
 */
@Service
public class DiffService {
	private static final Logger LOGGER = Logger.getLogger(DiffService.class.getName());
    
    /**
     * 
     */
    @Autowired
    private MessageRepository msgRepository;
    
    /**
     * @return
     */
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        msgRepository.findAll().forEach(messages::add);
        return messages;
    }
    
    /**
     * @param id
     * @param message
     * @param side
     */
    public Message setMessage(String id, String message, String side) {
        MessageKey key = new MessageKey(id, side);
        return msgRepository.save(new Message(key, message));
    }

    /**
     * @param id
     * @return
     */
    public String getDiffs(String id) {
        String left = getTextByIdAndSide(id, Message.LEFT_SIDE);
        String right = getTextByIdAndSide(id, Message.RIGHT_SIDE);
        
        return compareSides(left, right);
    }

	/**
	 * @param id
	 * @param side
	 * @return
	 */
	public String getTextByIdAndSide(String id, String side) {
		return msgRepository.findAllByKeyId(id).stream()
                .filter(m -> m.getKey().getSide().equals(side))
                .findFirst()
                .get().getText();
	}

    /**
     * @param left
     * @param right
     * @return
     */
    @SuppressWarnings("unchecked")
    public String compareSides(String left, String right) {
        String result;
        ObjectMapper om = new ObjectMapper();
        JSONObject json = new JSONObject();
        
        try {
            Map<String, Object> leftJsonMap = (Map<String, Object>)(om.readValue(left, Map.class));
            Map<String, Object> rightJsonMap = (Map<String, Object>)(om.readValue(right, Map.class));
            
            if(leftJsonMap.equals(rightJsonMap)) {
                json.put("equals", true);
                result = json.toString();
            }
            else if(leftJsonMap.size() != rightJsonMap.size()) {
                json.put("size", "The messages have different size");
                result = json.toString();
            }
            else {
                json.put("equals", false);
                
                for(String key : leftJsonMap.keySet()) {
                    String leftValue = leftJsonMap.get(key).toString();
                    Object rightValue = rightJsonMap.get(key);
                    
                    if(rightValue == null) {
                        json.put("key-" + key, "Doesn't exist in right message.");
                    }
                    else if(leftValue.length() != rightValue.toString().length()) {
                        json.put("left-value", leftValue);
                        json.put("left-value-length", leftValue.length());
                        json.put("right-value", rightValue);
                        json.put("right-value-length", rightValue.toString().length());
                    }
                }
                
                for(String key : rightJsonMap.keySet()) {
                	Object leftValue = leftJsonMap.get(key);
                    
                    if(leftValue == null) {
                        json.put("key-" + key, "Doesn't exist in left message.");
                    }
                }
                
                result = json.toString();
            }
        } catch (IOException | JSONException e) {
            result = "{\"error\":\"Error when converting the JSon object into Map.\"}";
            LOGGER.log(Level.FINE, result, e.toString());
        } catch (Exception e) {
        	result = "{\"error\":\"An exception has ocurred.\"}";
        	LOGGER.log(Level.FINE, result, e.toString());
        }
        
        return result;
    }

}
