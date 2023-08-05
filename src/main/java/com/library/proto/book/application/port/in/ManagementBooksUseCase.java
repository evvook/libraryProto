package com.library.proto.book.application.port.in;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ManagementBooksUseCase {
	public int SaveBook(Map<String,Object> bookInfo);
	public int deleteBooks(List<Map<String,Object>> bookInfos);
	public int uploadExcel(MultipartFile excelFile);
	public void indexBooks(List<Map<String,Object>> bookInfos);
}
