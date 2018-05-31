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


@RestController
public class DiffController {
    
    @Autowired
    private DiffService diffService;
    
    @RequestMapping("/")
    public String showInstructions() {
        return "Instructions";
    }
    
    @RequestMapping("/all")
    public List<Message> getAllMessages() {
        return diffService.getAll();
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/v1/diff/{id}/left")
    public void setLeftSide(@PathVariable String id, @RequestBody String message) {
        diffService.setMessage(id, message, Message.LEFT_SIDE);
    }

    @RequestMapping(method=RequestMethod.POST, value="/v1/diff/{id}/right")
    public void setRightSide(@PathVariable String id, @RequestBody String message) {
        diffService.setMessage(id, message, Message.RIGHT_SIDE);
    }
    
    @RequestMapping("/v1/diff/{id}")
    public String getDiffs(@PathVariable String id) {
        return diffService.getDiffs(id);
    }
}
