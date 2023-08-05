package com.library.proto.book.application.port.out;

import java.util.List;
import java.util.Map;

import com.library.proto.book.domain.Book;

public interface SearchEngin {
	List<String> searchId(String query);

	boolean isDocumentExists(String id);
	void createDocument(Book book);
	public void updateDocument(Book book);
	public void deleteDocument(String id);
}
