package com.library.proto.book.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.library.proto.book.application.port.out.BookRepository;

import lombok.Getter;

@Getter
public class Book {
	
	private String id;
	private String title;
	private List<Author> authors;
	private String publisher;
	private String isbn10;
	private String isbn13;
	private String contents;
	private String thumbnail;
	private String publication_date;
	
	public Book(String id, String title, String publisher, String author_names, String translator_names, String isbn10, String isbn13,
			String contents, String thumbnail, String publication_date) {
		this.id = id;
		this.title = title;
		this.publisher = publisher;
		this.authors = new ArrayList<Author>();
		if(author_names != null) {
			for(String author_name:author_names.split(",")) {
				this.authors.add(Author.builder().bookId(this.id).type("A").name(author_name).build());
			}
		}
		if(translator_names != null) {
			for(String translator_name:translator_names.split(",")) {
				this.authors.add(Author.builder().bookId(this.id).type("T").name(translator_name).build());
			}
		}
		this.isbn10 = isbn10;
		this.isbn13 = isbn13;
		this.contents = contents;
		this.thumbnail = thumbnail;
		this.publication_date = publication_date;
	}
	
	public Book(String id, String title, String publisher, List<Author> authors, String isbn10, String isbn13,
				String contents, String thumbnail, String publication_date) {
		
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.publisher = publisher;
		this.isbn10 = isbn10;
		this.isbn13 = isbn13;
		this.contents = contents;
		this.thumbnail = thumbnail;
		this.publication_date = publication_date;
	}
	
	public String getAuthorNames() {
		String authorNames = "";
		for(Author author:this.authors) {
			authorNames += authorNames.length()==0?author.getName():","+author.getName();
		}
		return authorNames;
	}

	public static BookBuilder builder() {
		return new BookBuilder();
	}
	
	public static BookBuilder builder(BookRepository bookRepository) {
		return new BookBuilder(bookRepository);
	}
	
	public static class BookBuilder {
		private String id;
		private String title;
		private List<Author> authors = new ArrayList<Author>();
		private List<String> authorNames = new ArrayList<String>();
		private List<String> translatorNames = new ArrayList<String>();
		private String publisher;
		private String isbn10;
		private String isbn13;
		private String contents;
		private String thumbnail;
		private String publication_date;
		
		private int seq = 1;
		
		private BookRepository bookRepository;
		
		public BookBuilder() {};
		
		public BookBuilder(BookRepository bookRepository) {
			this.bookRepository = bookRepository;
		}
		
		public BookBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public BookBuilder title(String title) {
			this.title = title;
			return this;
		}
		public BookBuilder authorNames(List<String> authorNames) {
			this.authorNames = authorNames;
			return this;
		}
		public BookBuilder translatorNames(List<String> translatorNames) {
			this.translatorNames = translatorNames;
			return this;
		}
		public BookBuilder publisher(String publisher) {
			this.publisher = publisher;
			return this;
		}
		public BookBuilder isbn(String isbns) {
			String[] isbnArr = isbns.split(" ");
			for(String isbn:isbnArr) {
				if(isbn.length()==10) {
					this.isbn10 = isbn;
				}else if(isbn.length()==13) {
					this.isbn13 = isbn;
				}
			}
			return this;
		}
		public BookBuilder isbn10(String isbn10) {
			this.isbn10 = isbn10;
			return this;
		}
		public BookBuilder isbn13(String isbn13) {
			this.isbn13 = isbn13;
			return this;
		}
		public BookBuilder contents(String contents) {
			this.contents = contents;
			return this;
		}
		public BookBuilder thumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
			return this;
		}
		public BookBuilder publication_date(String publication_date) {
			this.publication_date = publication_date;
			return this;
		}
		public BookBuilder datetime(String datetime) {
			Map<String,Object> dateInfo = new HashMap<String,Object>();
			dateInfo.put("datetime", datetime);
			Map<String,String> dateMap = bookRepository.getDate(dateInfo);
			this.publication_date = dateMap.get("PUBLICATION_DATE");
			return this;
		}
		
		public Book build() {
			if(id == null || "".equals(id)) {
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("ISBN10", this.isbn10);
				paramMap.put("ISBN13", this.isbn13);
				Map<String,String> rtnMap = bookRepository.findId(paramMap);
				this.id = rtnMap.get("ID");
				for(String author:authorNames) {
					this.authors.add(new Author(this.id,seq++,"A",author));
				}
				for(String translator:translatorNames) {
					this.authors.add(new Author(this.id,seq++,"T",translator));
				}
			}else if(id != null && title == null) {
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("id", id);
				Map<String,String> bookInfo = bookRepository.findById2(param);
				
				this.id = bookInfo.get("ID");
				this.title = bookInfo.get("TITLE");
				this.publisher = bookInfo.get("PUBLISHER");
				this.authors = new ArrayList<Author>();
				if(bookInfo.get("AUTHOR_NAMES") != null) {
					for(String author_name:bookInfo.get("AUTHOR_NAMES").split(",")) {
						this.authors.add(Author.builder(bookRepository).bookId(this.id).type("A").name(author_name).build());
					}
				}
				if(bookInfo.get("TRANSLATOR_NAMES") != null) {
					for(String translator_name:bookInfo.get("TRANSLATOR_NAMES").split(",")) {
						this.authors.add(Author.builder(bookRepository).bookId(this.id).type("T").name(translator_name).build());
					}
				}
				this.isbn10 = bookInfo.get("ISBN10");
				this.isbn13 = bookInfo.get("ISBN13");
				this.contents = bookInfo.get("CONTENTS");
				this.thumbnail = bookInfo.get("THUMBNAIL");
				this.publication_date = bookInfo.get("PUBLICATION_DATE");
			}
			
			return new Book(this.id,this.title,this.publisher,this.authors,this.isbn10,this.isbn13,this.contents,this.thumbnail,this.publication_date);
		}
	}
}
