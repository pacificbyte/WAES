package com.waes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.waes.pojo.Message;


public interface MessageRepository extends CrudRepository<Message, String>{

    List<Message> findAllByKeyId(String id);

}
