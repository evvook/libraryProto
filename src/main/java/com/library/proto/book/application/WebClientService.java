package com.library.proto.book.application;

import java.util.Map;

public interface WebClientService {
	public Map<String,Object> get(String isbn);
}
