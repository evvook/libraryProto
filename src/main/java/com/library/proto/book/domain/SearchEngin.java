package com.library.proto.book.domain;

import java.util.List;
import java.util.Map;

public interface SearchEngin {
	List<String> searchId(String query);

	boolean isDocumentExists(String id);
	void createDocument(Map<String, Object> bookInfo);
	public void updateDocument(Map<String,Object> bookInfo);
	public void deleteDocument(String id);
}
