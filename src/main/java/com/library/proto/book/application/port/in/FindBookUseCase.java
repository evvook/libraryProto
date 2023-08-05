package com.library.proto.book.application.port.in;

import java.util.Map;

import com.library.proto.book.domain.Book;

public interface FindBookUseCase {
	public Book findBook(Map<String,String> param);
}
