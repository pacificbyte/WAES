package com.waes.pojo;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * @author ccamarena
 *
 */
@Entity
public class Message {
    
    /**
     * 
     */
    public static final String LEFT_SIDE    = "left";
    public static final String RIGHT_SIDE   = "right";

    /**
     * 
     */
    @EmbeddedId
    private MessageKey key;
    private String text;
    

    /**
     * @param key
     * @param message
     */
    public Message(MessageKey key, String message) {
        this.key = key;
        this.text = decodeMessage(message);
    }
    
    /**
     * 
     */
    public Message() {
    }


    /**
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return
     */
    public MessageKey getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(MessageKey key) {
        this.key = key;
    }

    /**
     * @param message
     * @return
     */
    private String decodeMessage(String message) {
        String decodedMessage;
        
        try {
            byte[] base64decodedBytes = Base64.getDecoder().decode(message);
            decodedMessage = new String(base64decodedBytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            decodedMessage = "Message cannot be decoded with Base64";
        }
        
        return decodedMessage;
    }
}
