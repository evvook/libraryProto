package com.library.proto.book.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.library.proto.book.domain.Book;
import com.library.proto.book.domain.BookRepository;
import com.library.proto.book.domain.SearchEngin;
import com.library.proto.book.infra.ExcelUtil;

@Service
public class BookInfoService {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	ExcelUtil excelUtil;
	
	@Autowired
	WebClientService webClientService;
	
	@Autowired
	SearchEngin searchEngin;

	public void insertExcel(MultipartFile file) {
		// TODO Auto-generated method stub
		if(file.isEmpty()) {
			return;
		}
		
		String contentType = file.getContentType();
		
		if(!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
			return;
		}
		
		List<Map<String,Object>> listMap = excelUtil.getListData(file, 1, 5);
		
		try {
			
			for(Map<String,Object> map:listMap) {
				String isbn = (String)map.get("5");
				if(isbn != null && !"".equals(isbn)) {
					Map<String,Object> bookInfo = webClientService.get(isbn);
					List<Map<String,Object>> documents = (List<Map<String,Object>>)bookInfo.get("documents");
					if(documents.size()>0) {
//						new Book(documents.get(0),bookRepository).saveBook();
						Map<String,Object> document = documents.get(0);
						Book.builder(bookRepository, searchEngin)
										.title((String)document.get("title"))
										.isbn((String)document.get("isbn"))
										.id()
										.authors((List<String>)document.get("authors"))
										.translators((List<String>)document.get("translators"))
										.publisher((String)document.get("publisher"))
										.contents((String)document.get("contents"))
										.thumbnail((String)document.get("thumbnail"))
										.datetime((String)document.get("datetime"))
									.build()
								.saveWithSearch();
					}
				}else {
					System.out.println(map.toString());
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List<Book> retrieveBooks(Map<String,String> param) {
		// TODO Auto-generated method stub
		return bookRepository.findByKeywords(param);
	}

	public Book retrieveBook(Map<String, String> param) {
		// TODO Auto-generated method stub
		return bookRepository.findById(param);
	}

	public void saveBook(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<String> authors = new ArrayList<String>();
		List<String> translators = new ArrayList<String>();
		for(Map<String,String> author:(List<Map<String,String>>)param.get("author")) {
			if("A".equals(author.get("type"))) {
				authors.add(author.get("name"));
			}else if("T".equals(author.get("type"))) {
				translators.add(author.get("name"));
			}
		}
		Book.builder(bookRepository,searchEngin)
				.id((String)param.get("id"))
				.title((String)param.get("title"))
				.authors(authors)
				.translators(translators)
				.publisher((String)param.get("publisher"))
				.publication_date((String)param.get("publication_date"))
				.isbn10((String)param.get("isbn10"))
				.isbn13((String)param.get("isbn13"))
				.contents((String)param.get("contents"))
			.build()
			.saveWithSearch();
	}

	public void deleteBooks(List<Map<String, Object>> params) {
		// TODO Auto-generated method stub
		for(Map<String,Object> param:params) {
			Book.builder(bookRepository,searchEngin).id((String)param.get("id")).build().deleteWithSearch();
		}
		
	}

	public List<Book> retrieveBooksByQuery(Map<String, String> param) {
		// TODO Auto-generated method stub
		List<String> idList = searchEngin.searchId(param.get("query"));
		List<Book> books = new ArrayList<Book>();
		for(String id:idList) {
			Book book = Book.builder(bookRepository).id(id).build();
			books.add(book);
		}
		return books;
	}

	public void indexBooks(List<Map<String, Object>> params) {
		// TODO Auto-generated method stub
		for(Map<String,Object> param:params) {
			searchEngin.createDocument(param);
		}
	}
}
