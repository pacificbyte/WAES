package com.waes.pojo;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageTest {
    
    Message message;
    
    @Before
    public void init() {
    }
    
    
    @Test
    public void decodeMessage() {
//        String id = "1";
//        String text = "{\"id\": \"js\",\"message\": \"JavaScript\"}";
//        String encodedText = this.encodeMessage(text);
//        this.message = new Message(id, encodedText, Message.LEFT_SIDE);
//        
//        assertEquals(text, this.message.getText());
    }
    
    private String encodeMessage(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }
}
