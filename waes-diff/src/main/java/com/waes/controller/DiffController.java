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
 * @author ccamarena
 *
 */
@RestController
public class DiffController {
    
    /**
     * 
     */
    @Autowired
    private DiffService diffService;
    
    /**
     * @return
     */
    @RequestMapping("/")
    public String showInstructions() {
        return "Instructions";
    }
    
    /**
     * @return
     */
    @RequestMapping("/all")
    public List<Message> getAllMessages() {
        return diffService.getAll();
    }
    
    /**
     * @param id
     * @param message
     */
    @RequestMapping(method=RequestMethod.POST, value="/v1/diff/{id}/left")
    public void setLeftSide(@PathVariable String id, @RequestBody String message) {
        diffService.setMessage(id, message, Message.LEFT_SIDE);
    }

    /**
     * @param id
     * @param message
     */
    @RequestMapping(method=RequestMethod.POST, value="/v1/diff/{id}/right")
    public void setRightSide(@PathVariable String id, @RequestBody String message) {
        diffService.setMessage(id, message, Message.RIGHT_SIDE);
    }
    
    /**
     * @param id
     * @return
     */
    @RequestMapping("/v1/diff/{id}")
    public String getDiffs(@PathVariable String id) {
        return diffService.getDiffs(id);
    }
}
