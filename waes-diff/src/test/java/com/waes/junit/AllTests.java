package com.waes.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.waes.junit.controller.DiffControllerTest;
import com.waes.junit.pojo.MessageTest;
import com.waes.junit.service.DiffServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	DiffControllerTest.class
	, DiffServiceTest.class
	, MessageTest.class
})
public class AllTests {

}
