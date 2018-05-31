package com.waes.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffServiceTest {

    private DiffService diffService;
    
    @Before
    public void init() {
        diffService = new DiffService();
    }
    
    @Test
    public void compareSides() {
        String left = "{\"id\": \"js\",\"message\": \"JavaScript\"}";
        String right = "{\"id\": \"js\",\"message\": \"Java\"}";
        
        String result = diffService.compareSides(left, right);
        String expected = "{\"equals\": false,\"left-value\": JavaScript,\"left-value-length\": 10,\"right-value\": Java,\"right-value-length\": 4}";
        
        assertEquals(expected, result);
    }
}
