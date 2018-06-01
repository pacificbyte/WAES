package com.waes.pojo;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * This is the composite key class to assign to the entity class. 
 * It is formed by id and side of the message (left/right).
 * 
 * @author ccamarena
 *
 */
@Embeddable
public class MessageKey implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String side;
    
    /**
     * Default contrsuctor
     */
    public MessageKey() {
    }
    
    /**
     * Constructor with fields in the entity class.
     * 
     * @param id This is the id to bind the messages together.
     * @param side Indicates if the message is left or right side.
     */
    public MessageKey(String id, String side) {
        this.id = id;
        this.side = side;
    }
    
    /**
     * @return Return Id 
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id Id to be assigned to the key
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return returns the side (left/right)
     */
    public String getSide() {
        return side;
    }
    
    /**
     * @param side Side to be assigned to the key (left/right)
     */
    public void setSide(String side) {
        this.side = side;
    }
    
}
