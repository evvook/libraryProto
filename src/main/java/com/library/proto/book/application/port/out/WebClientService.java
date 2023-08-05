package com.library.proto.book.application.port.out;

import java.util.Map;

public interface WebClientService {
	public Map<String,Object> get(String isbn);
}
