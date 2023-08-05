package com.library.proto.book.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.library.proto.book.application.port.in.ManagementBooksUseCase;
import com.library.proto.book.application.port.out.BookRepository;
import com.library.proto.book.application.port.out.SearchEngin;
import com.library.proto.book.application.port.out.WebClientService;
import com.library.proto.book.domain.Author;
import com.library.proto.book.domain.Book;
import com.library.proto.book.utils.ExcelUtil;

@Service
public class ManagementBooksUseCaseImpl implements ManagementBooksUseCase {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	WebClientService webClientService;
	
	@Autowired
	SearchEngin searchEngin;
	
	@Override
	public int SaveBook(Map<String,Object> bookInfo) {
		// TODO Auto-generated method stub
		List<String> authorNames = new ArrayList<String>();
		List<String> translatorNames = new ArrayList<String>();
		for(Map<String,String> author:(List<Map<String,String>>)bookInfo.get("author")) {
			if("A".equals(author.get("type"))) {
				authorNames.add(author.get("name"));
			}else if("T".equals(author.get("type"))) {
				translatorNames.add(author.get("name"));
			}
		}
		Book book = Book.builder(bookRepository)
				.id((String)bookInfo.get("id"))
				.title((String)bookInfo.get("title"))
				.authorNames(authorNames)
				.translatorNames(translatorNames)
				.publisher((String)bookInfo.get("publisher"))
				.publication_date((String)bookInfo.get("publication_date"))
				.isbn10((String)bookInfo.get("isbn10"))
				.isbn13((String)bookInfo.get("isbn13"))
				.contents((String)bookInfo.get("contents"))
			.build();
		saveBook(book);
		return 0;
	}

	@Override
	public int deleteBooks(List<Map<String,Object>> bookInfos) {
		// TODO Auto-generated method stub
		for(Map<String,Object> bookInfo:bookInfos) {
			Book book = Book.builder(bookRepository).id((String)bookInfo.get("id")).build();
			bookRepository.deleteBook(book);
			for(Author author:book.getAuthors()) {
				bookRepository.deleteAuthor(author);
			}
			searchEngin.deleteDocument(book.getId());
		}
		return 0;
	}

	@Override
	public int uploadExcel(MultipartFile excelFile) {
		// TODO Auto-generated method stub
		if(excelFile.isEmpty()) {
			return 0;
		}
		
		String contentType = excelFile.getContentType();
		
		if(!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
			return 0;
		}
		
		ExcelUtil excelUtil = new ExcelUtil();
		
		List<Map<String,Object>> listMap = excelUtil.getListData(excelFile, 1, 5);
		
		try {
			for(Map<String,Object> map:listMap) {
				String isbn = (String)map.get("5");
				if(isbn != null && !"".equals(isbn)) {
					Map<String,Object> bookInfo = webClientService.get(isbn);
					List<Map<String,Object>> documents = (List<Map<String,Object>>)bookInfo.get("documents");
					if(documents.size()>0) {
						Map<String,Object> document = documents.get(0);
						Book book = Book.builder(bookRepository)
										.title((String)document.get("title"))
										.isbn((String)document.get("isbn"))
										.authorNames((List<String>)document.get("authors"))
										.translatorNames((List<String>)document.get("translators"))
										.publisher((String)document.get("publisher"))
										.contents((String)document.get("contents"))
										.thumbnail((String)document.get("thumbnail"))
										.datetime((String)document.get("datetime"))
									.build();
						saveBook(book);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void indexBooks(List<Map<String, Object>> bookInfos) {
		// TODO Auto-generated method stub
		for(Map<String,Object> bookInfo:bookInfos) {
			Book book = Book.builder(bookRepository).id((String)bookInfo.get("id")).build();
			if(searchEngin.isDocumentExists(book.getId())) {
				searchEngin.updateDocument(book);
			}else {
				searchEngin.createDocument(book);
			}
		}
	}

	private boolean isBookExists(String book_id) {
		Map<String,String> param = new HashMap<String,String>();
		param.put("book_id", book_id);
		Book book = bookRepository.findById(param);
		if(book == null) {
			return false;
		}else {
			return true;
		}
	}
	
	private int saveBook(Book book) {
		if(isBookExists(book.getId())) {
			bookRepository.updateBook(book);
			for(Author author:book.getAuthors()) {
				bookRepository.updateAuthor(author);
			}
			if(searchEngin.isDocumentExists(book.getId())) {
				searchEngin.updateDocument(book);
			}else {
				searchEngin.createDocument(book);
			}
		}else {
			bookRepository.createBook(book);
			for(Author author:book.getAuthors()) {
				bookRepository.createAuthor(author);
			}
			if(searchEngin.isDocumentExists(book.getId())) {
				searchEngin.updateDocument(book);
			}else {
				searchEngin.createDocument(book);
			}
		}		
		return 0;
	}
}
