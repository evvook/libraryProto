package com.library.proto;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.library.proto.book.domain.Book;
import com.library.proto.book.domain.BookRepository;
import com.library.proto.book.infra.WebClientServiceImpl;

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
