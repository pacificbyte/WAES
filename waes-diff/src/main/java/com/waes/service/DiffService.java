package com.waes.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.pojo.Message;
import com.waes.pojo.MessageKey;
import com.waes.repository.MessageRepository;

@Service
public class DiffService {
    
    @Autowired
    private MessageRepository msgRepository;
    
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        msgRepository.findAll().forEach(messages::add);
        return messages;
    }
    
    public void setMessage(String id, String message, String side) {
        MessageKey key = new MessageKey(id, side);
        msgRepository.save(new Message(key, message));
    }

    public String getDiffs(String id) {
        String left = "{\"id\": \"js\",\"message\": \"JavaScript\"}";
        String right = "{\"id\": \"js\",\"message\": \"Java\"}";
        
        return compareSides(left, right);
    }

    @SuppressWarnings("unchecked")
    public String compareSides(String left, String right) {
        String result;
        
        ObjectMapper om = new ObjectMapper();
        try {
            Map<String, Object> leftJsonMap = (Map<String, Object>)(om.readValue(left, Map.class));
            Map<String, Object> rightJsonMap = (Map<String, Object>)(om.readValue(right, Map.class));
            
            if(leftJsonMap.equals(rightJsonMap)) {
                result = "{\"equals\": true}";
            }
            else if(leftJsonMap.size() != rightJsonMap.size()) {
                result = "{\"size\": \"The messages have different size\"}";
            }
            else {
                StringBuffer buffer = new StringBuffer("{\"equals\": false,");
                for(String key : leftJsonMap.keySet()) {
                    String leftValue = leftJsonMap.get(key).toString();
                    String rightValue = rightJsonMap.get(key).toString();
                    
                    if(rightValue == null) {
                        buffer.append("\"key-");
                        buffer.append(key);
                        buffer.append("\": \"Does not exist in both messages.\"");
                    }
                    else if(leftValue.length() != rightValue.length()) {
                            buffer.append("\"left-value\": ");
                            buffer.append(leftValue);
                            buffer.append(",");
                            buffer.append("\"left-value-length\": ");
                            buffer.append(leftValue.length());
                            buffer.append(",");
                            buffer.append("\"right-value\": ");
                            buffer.append(rightValue);
                            buffer.append(",");
                            buffer.append("\"right-value-length\": ");
                            buffer.append(rightValue.length());
                    }
                }
                
                for(String key : rightJsonMap.keySet()) {
                    String leftValue = leftJsonMap.get(key).toString();
                    
                    if(leftValue == null) {
                        buffer.append("\"key-");
                        buffer.append(key);
                        buffer.append("\": \"Does not exist in both messages.\"");
                    }
                }
                
                buffer.append("}");
                result = buffer.toString();
            }
        } catch (IOException e) {
           result = "{\"error\": \"Error when converting the JSon object into Map.\"";
        }
        
        return result;
    }

}
