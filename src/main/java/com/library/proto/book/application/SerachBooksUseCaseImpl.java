package com.library.proto.book.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.proto.book.application.port.in.SearchBooksUseCase;
import com.library.proto.book.application.port.out.BookRepository;
import com.library.proto.book.application.port.out.SearchEngin;
import com.library.proto.book.application.port.out.WebClientService;
import com.library.proto.book.domain.Book;

@Service
public class SerachBooksUseCaseImpl implements SearchBooksUseCase {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	SearchEngin searchEngin;
	
	@Override
	public List<Book> searchBooks(Map<String, String> param) {
		// TODO Auto-generated method stub
		return bookRepository.findByKeywords(param);
	}

	@Override
	public List<Book> searchBooksByQuery(Map<String,String> param) {
		// TODO Auto-generated method stub
		List<String> idList = searchEngin.searchId(param.get("query"));
		List<Book> books = new ArrayList<Book>();
		for(String id:idList) {
			Book book = Book.builder(bookRepository).id(id).build();
			books.add(book);
		}
		return books;
	}

}
