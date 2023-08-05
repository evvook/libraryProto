package com.library.proto.book.presentation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.library.proto.book.application.BookInfoService;
import com.library.proto.book.domain.Book;

@Controller
public class BookController {

	@Autowired
	BookInfoService bookInfoService;
	
	@ResponseBody
	@RequestMapping(value="/searchBookList",method= {RequestMethod.POST, RequestMethod.OPTIONS})
	public List<Book> retrieveBooks(@RequestBody Map<String,String> param){
		List<Book> books = bookInfoService.retrieveBooks(param);
		return books;
	}

	@ResponseBody
	@RequestMapping(value="/excelUpload",method= {RequestMethod.POST})
	public void excelUpload(MultipartHttpServletRequest request) {
		MultipartFile file= request.getFile("uploadfile");
		bookInfoService.insertExcel(file);
	}
	
	@ResponseBody
	@RequestMapping(value="/bookInfo",method= {RequestMethod.POST, RequestMethod.OPTIONS})
	public Book retrieveBook(@RequestBody Map<String,String> param){
		Book book = bookInfoService.retrieveBook(param);
		return book;
	}
	
	@ResponseBody
	@RequestMapping(value="/saveBookInfo",method= {RequestMethod.POST, RequestMethod.OPTIONS})
	public void saveBook(@RequestBody Map<String,Object> param){
		bookInfoService.saveBook(param);
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteBooks",method= {RequestMethod.POST, RequestMethod.OPTIONS})
	public void deleteBooks(@RequestBody List<Map<String,Object>> params){
		bookInfoService.deleteBooks(params);
	}
	
	@ResponseBody
	@RequestMapping(value="/indexBooks",method= {RequestMethod.POST, RequestMethod.OPTIONS})
	public void indexBooks(@RequestBody List<Map<String,Object>> params){
		bookInfoService.indexBooks(params);
	}
	
	@ResponseBody
	@RequestMapping(value="/searchBookByQuery",method= {RequestMethod.POST, RequestMethod.OPTIONS})
	public List<Book> retrieveBooksByQuery(@RequestBody Map<String,String> param){
		List<Book> books = bookInfoService.retrieveBooksByQuery(param);
		return books;
	}
}
