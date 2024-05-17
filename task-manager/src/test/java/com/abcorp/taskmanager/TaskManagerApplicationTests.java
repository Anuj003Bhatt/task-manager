package com.abcorp.taskmanager;

import com.abcorp.taskmanager.config.MailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class TaskManagerApplicationTests {
	@MockBean
	private MailConfig mailConfig;

	@MockBean
	private JavaMailSender mailSender;
	@Test
	void contextLoads() {
	}

}
