package com.library.proto.book.domain;

import java.util.HashMap;
import java.util.Map;

public class Author {
	private String book_id;
	private int seq;
	private String type;
	private String name;
	
	private BookRepository bookRepository;
	
	public Author(BookRepository bookRepository, String bookId, int seq, String type, String name) {
		// TODO Auto-generated constructor stub
		this.book_id = bookId;
		this.seq = seq;
		this.type = type;
		this.name = name;
		
		this.bookRepository = bookRepository;
	}
	public Author(String bookId, int seq, String type, String name) {
		// TODO Auto-generated constructor stub
		this.book_id = bookId;
		this.seq = seq;
		this.type = type;
		this.name = name;
	}

	public int save() {
		Map<String,String> author = bookRepository.findAuthorById(getInfo());
		if(author == null) {
			return bookRepository.createAuthor(this);
		}else {
			return bookRepository.updateAuthor(this);
		}
	}
	
	public int delete() {
		return bookRepository.deleteAuthor(this);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Map<String,Object> getInfo(){
		Map<String,Object> info = new HashMap<String,Object>();
		info.put("book_id", book_id);
		info.put("seq", seq);
		info.put("type", type);
		info.put("name", name);
		
		return info;
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
			return new Author(bookRepository, book_id, seq, type, name);
		}
	}
}
