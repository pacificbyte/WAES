package com.waes.repository;

import org.springframework.data.repository.CrudRepository;

import com.waes.pojo.Message;


public interface MessageRepository extends CrudRepository<Message, String>{

}
