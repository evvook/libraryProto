package com.library.proto.book.domain;

import java.util.List;
import java.util.Map;

public interface BookRepository {

	List<Book> findAll();

	Map<String, String> findId(Map<String, String> paramMap);

	int createBook(Book book);
	
	int updateBook(Book book);
	
	int createAuthor(Author author);
	
	int updateAuthor(Author ahoutr);

	Map<String, String> getDate(Map<String, Object> bookInfo);

	List<Book> findByKeywords(Map<String, String> param);

	Book findById(Map<String, String> param);

	Map<String,String> findById2(Map<String, Object> param);
	
	Map<String,String> findAuthorById(Map<String, Object> param);

	int findAuthorSeq(Map<String, String> authorMap);

	int deleteBook(Book book);

	int deleteAuthor(Author author);

}
