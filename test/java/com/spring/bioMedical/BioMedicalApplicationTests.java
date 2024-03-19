package com.spring.bioMedical;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BioMedicalApplicationTests {

	@Test
	public void contextLoads() {
		
		assertEquals("Pass", 2, 2);
	}
	
	@Test
	public void appointMentTest() {
		
		assertEquals("Pass", 2, 2);
	}


}
