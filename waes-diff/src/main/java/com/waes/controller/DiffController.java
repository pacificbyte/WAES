package com.waes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.waes.pojo.Message;
import com.waes.service.DiffService;


/**
 * This is the controller class. This class manages the uri redirects and request from the broswer
 * and calls the service layer accordingly.
 * 
 * @author ccamarena
 *
 */
@RestController
@RequestMapping("/v1/diff")
public class DiffController {
    
    /**
     * Linking the Service class to the Controller class.
     */
    @Autowired
    private DiffService diffService;
    
    /**
     * This method returns all the messages in the DB at any given moment.
     * @return List<Message> List of Messages
     */
    @RequestMapping("/all")
    public List<Message> getAllMessages() {
        return diffService.getAll();
    }
    
    /**
     * This method sends the left message to the DB to be stored.
     * 
     * @param id This is the identifier of the message where will be binding to the right side with he same id.
     * @param message This is the Base64 encoded message to be stored.
     */
    @RequestMapping(method=RequestMethod.POST, value="/{id}/left")
    public void setLeftSide(@PathVariable String id, @RequestBody String message) {
        diffService.setMessage(id, message, Message.LEFT_SIDE);
    }

    /**
     * This method sends the right message to the DB to be stored.
     * 
     * @param id This is the identifier of the message where will be binding to the left side with he same id.
     * @param Message message This is the Base64 encoded message to be stored.
     */
    @RequestMapping(method=RequestMethod.POST, value="/{id}/right")
    public Message setRightSide(@PathVariable String id, @RequestBody String message) {
        return diffService.setMessage(id, message, Message.RIGHT_SIDE);
    }
    
    /**
     * This method performs the comparison between the left and the right side for the given id.
     *  
     * @param id This is the identifier of the messages to be compared.
     * @return String JSON string indicating differences.
     */
    @RequestMapping("/{id}")
    public String getDiffs(@PathVariable String id) {
        return diffService.getDiffs(id);
    }
}
