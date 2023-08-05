package com.library.proto.book.application;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.proto.book.application.port.in.FindBookUseCase;
import com.library.proto.book.application.port.out.BookRepository;
import com.library.proto.book.domain.Book;

@Service
public class FindBookUseCaseImpl implements FindBookUseCase {

	@Autowired
	BookRepository bookRepository;
	
	@Override
	public Book findBook(Map<String, String> param) {
		// TODO Auto-generated method stub
		return bookRepository.findById(param);
	}

}
