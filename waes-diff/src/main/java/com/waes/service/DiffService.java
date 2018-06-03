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
 * This is the Service class. This class in the middle tier between the Controller and the DB. 
 * It bridges the calls from the browser with the Repository. This class also defines the logic 
 * behind the methods exposed by the Controller. 
 * 
 * @author ccamarena
 *
 */
@Service
public class DiffService {
	private static final Logger LOGGER = Logger.getLogger(DiffService.class.getName());
    
    /**
     * Linking the Repository class to the Service class.
     */
    @Autowired
    private MessageRepository msgRepository;
    
    /**
     * This method returns all the messages in the DB in a given moment. It leverages the 
     * findAll built-in to the CRUD Repository.
     * 
     * @return List of messages in the DB.
     */
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        msgRepository.findAll().forEach(messages::add);
        return messages;
    }
    
    /**
     * This method saves the message in the DB.
     * 
     * @param id String binding the left and right messages to be compared
     * @param message Encoded Message
     * @param side Indicates which side the messages belongs to
     */
    public Message setMessage(String id, String message, String side) {
        MessageKey key = new MessageKey(id, side);
        return msgRepository.save(new Message(key, message));
    }

    /**
     * This method performs the differences between left and right messages under the same id.
     * 
     * @param id String to identify the messages to be compared
     * @return
     */
    public String getDiffs(String id) {
        String left = getTextByIdAndSide(id, Message.LEFT_SIDE);
        String right = getTextByIdAndSide(id, Message.RIGHT_SIDE);
        
        return compareSides(left, right);
    }

	/** 
	 * This method get the text from DB given the id and the side.
	 * 
	 * @param id Id of the message to retrieve 
	 * @param side Side of the message to retrieve 
	 * @return Text from message in the DB according to Id and side
	 */
	private String getTextByIdAndSide(String id, String side) {
		return msgRepository.findAllByKeyId(id).stream()
                .filter(m -> m.getKey().getSide().equals(side))
                .findFirst()
                .get().getText();
	}

    /**
     * This method performs the comparison between the 2 texts. It assumes the messages are in JSON format
     * once they are decoded. 
     * 
     * @param left Left text
     * @param right Right text
     * @return JSON string with the results of the comparison
     */
    @SuppressWarnings("unchecked")
    public String compareSides(String left, String right) {
        String result;
        ObjectMapper om = new ObjectMapper();
        JSONObject json = new JSONObject();
        
        try {
        	// Converts each JSON string into map. 
            Map<String, Object> leftJsonMap = (Map<String, Object>)(om.readValue(left, Map.class));
            Map<String, Object> rightJsonMap = (Map<String, Object>)(om.readValue(right, Map.class));
            
            // Checks if the maps are the equal
            if(leftJsonMap.equals(rightJsonMap)) {
                json.put("equals", true);
                result = json.toString();
            }
            // If the maps are not equal, checks the sizes. 
            else if(leftJsonMap.size() != rightJsonMap.size()) {
                json.put("size", "The messages have different size");
                result = json.toString();
            }
            // If they are not equal but they are the same size, then it checks keys and values to find the differences.
            else {
                json.put("equals", false);
                
                // Get all the left map keys checks if they exist in the right side
                // and then verifies the values for those keys both maps
                for(String key : leftJsonMap.keySet()) {
                    String leftValue = leftJsonMap.get(key).toString();
                    Object rightValue = rightJsonMap.get(key);
                    
                    // If the key returns null in the right side map, then it doesn't exist. 
                    if(rightValue == null) {
                        json.put("key-" + key, "Doesn't exist in right message.");
                    }
                    // If the key has a value, then checks it to verify if both key and value are the same in both maps.
                    else if(leftValue.length() != rightValue.toString().length()) {
                        json.put("left-value", leftValue);
                        json.put("left-value-length", leftValue.length());
                        json.put("right-value", rightValue);
                        json.put("right-value-length", rightValue.toString().length());
                    }
                }
                
                // Get all the right map keys checks if they exist in the left side
                for(String key : rightJsonMap.keySet()) {
                	Object leftValue = leftJsonMap.get(key);
                    
                    if(leftValue == null) {
                        json.put("key-" + key, "Doesn't exist in left message.");
                    }
                }
                
                // Converts JSON object to string and is ready to be returned.
                result = json.toString();
            }
        } catch (IOException | JSONException e) { // Exception occurred and needs to be sent back and logged.
            result = "{\"error\":\"Error when converting the JSon object into Map.\"}";
            LOGGER.log(Level.FINE, result, e.toString());
        } catch (Exception e) {
        	result = "{\"error\":\"An exception has ocurred.\"}";
        	LOGGER.log(Level.FINE, result, e.toString());
        }
        
        // return result in JSON formatted string. 
        return result;
    }

}
