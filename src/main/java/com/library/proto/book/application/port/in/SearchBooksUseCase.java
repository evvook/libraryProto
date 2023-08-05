package com.library.proto.book.application.port.in;

import java.util.List;
import java.util.Map;

import com.library.proto.book.domain.Book;

public interface SearchBooksUseCase {
	List<Book> searchBooks(Map<String,String> param);
	List<Book> searchBooksByQuery(Map<String,String> param);
}
