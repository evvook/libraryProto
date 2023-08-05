package com.library.proto;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.library.proto.book.adapter.out.api.WebClientServiceImpl;
import com.library.proto.book.application.port.out.BookRepository;
import com.library.proto.book.domain.Book;

@SpringBootTest
public class WebClientApplicationTest {

	@Autowired
	private WebClientServiceImpl webClientService;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	void get() {
		Map<String,Object> result = webClientService.get("9788932916804");

		List<Map<String,Object>> documents = (List<Map<String,Object>>)result.get("documents");
	}
}
