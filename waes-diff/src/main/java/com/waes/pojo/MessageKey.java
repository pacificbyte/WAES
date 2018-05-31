package com.waes.pojo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class MessageKey implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String side;
    
    public MessageKey() {
    }
    
    public MessageKey(String id, String side) {
        this.id = id;
        this.side = side;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSide() {
        return side;
    }
    public void setSide(String side) {
        this.side = side;
    }
    
}
