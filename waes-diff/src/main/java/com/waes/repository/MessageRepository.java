package com.waes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.waes.pojo.Message;


/**
 * This is the CRUD repository default froms Spring
 * This class allows you to connect to the DB and perform the basic operations:
 * Create, Read, Update and Delete
 * 
 * The DB for this project is Embedded Apache Derby. 
 * 
 * @author ccamarena
 *
 */
public interface MessageRepository extends CrudRepository<Message, String>{

	// Custom method created with Spring Boot to find all the messages with the same key. 
	// The implementation of this method since the nomenclature meets the Spring JPA standard. 
    List<Message> findAllByKeyId(String id);

}
