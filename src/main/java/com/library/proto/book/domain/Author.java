package com.library.proto.book.domain;

import java.util.HashMap;
import java.util.Map;

import com.library.proto.book.application.port.out.BookRepository;

import lombok.Getter;

@Getter
public class Author {
	private String book_id;
	private int seq;
	private String type;
	private String name;
	
	public Author(String bookId, int seq, String type, String name) {
		// TODO Auto-generated constructor stub
		this.book_id = bookId;
		this.seq = seq;
		this.type = type;
		this.name = name;
	}
	
	public static AuthorBuilder builder() {
		return new AuthorBuilder();
	}
	
	public static AuthorBuilder builder(BookRepository bookRepository) {
		return new AuthorBuilder(bookRepository);
	}
	
	public static class AuthorBuilder {
		private String book_id;
		private int seq;
		private String type;
		private String name;
		
		private BookRepository bookRepository;
		
		public AuthorBuilder() {
			// TODO Auto-generated constructor stub
		}
		public AuthorBuilder(BookRepository bookRepository) {
			this.bookRepository = bookRepository;
		}

		public AuthorBuilder bookId(String book_id) {
			this.book_id = book_id;
			return this;
		}
		public AuthorBuilder seq(int seq) {
			this.seq = seq;
			return this;
		}
		public AuthorBuilder type(String type) {
			this.type = type;
			return this;
		}
		public AuthorBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public Author build() {
			if(seq == 0 && bookRepository != null) {
				Map<String,String> authorMap = new HashMap<String,String>();
				authorMap.put("book_id", book_id);
				authorMap.put("type", type);
				authorMap.put("name", name);
				seq = bookRepository.findAuthorSeq(authorMap);
			}
			return new Author(book_id, seq, type, name);
		}
	}
}
