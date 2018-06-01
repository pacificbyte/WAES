package com.waes.pojo;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * This is the entity class where the messages are decoded and stored
 * 
 * @author ccamarena
 *
 */
@Entity
public class Message {
	private static final Logger LOGGER = Logger.getLogger(Message.class.getName());
    /**
     * Setting up constants to identify the message's side. 
     */
    public static final String LEFT_SIDE    = "left";
    public static final String RIGHT_SIDE   = "right";

    /**
     * Composite key to store in the DB
     */
    @EmbeddedId
    private MessageKey key;
    private String text;
    

    /**
     * Constructor with fields in the entity class.
     * 
     * @param key This is the composite key id-side
     * @param message This is the message. It is decoded and then assigned to the entity.
     */
    public Message(MessageKey key, String message) {
        this.key = key;
        this.text = decodeMessage(message);
    }
    
    /**
     * Default constructor
     */
    public Message() {
    }


    /**
     * @return Returns the decoded text.
     */
    public String getText() {
        return text;
    }

    /**
     * @param text Text to be set in this entity class.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return Return the key of the message
     */
    public MessageKey getKey() {
        return key;
    }

    /**
     * @param key Composite key associated with the message to be stored in DB.
     */
    public void setKey(MessageKey key) {
        this.key = key;
    }

    /**
     * This is the method to decode the Base64 text that is coming from the POST method in the rest call.
     * 
     * @param message This is the text to be decoded.
     * @return Returns decoded text to be assigned to the entity class.
     */
    private String decodeMessage(String message) {
        String decodedMessage = "";
        
        try {
        	// verifies the message is not null
        	if(message != null) {
        		// Decodes message 
	            byte[] base64decodedBytes = Base64.getDecoder().decode(message);
	            decodedMessage = new String(base64decodedBytes, "utf-8");
        	}
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
        	// An exception has ocurred. Accordingly message is returned and logged.
            decodedMessage = "Message cannot be decoded with Base64";
            LOGGER.log(Level.FINE, decodedMessage, e.toString());
        }
        
        // return decoded message
        return decodedMessage;
    }
}
