package com.library.proto.book.infra;

import java.util.Map;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.library.proto.book.application.WebClientService;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Service
public class WebClientServiceImpl implements WebClientService {
	private ClientHttpConnector connector() {
	    return new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection()));
	}
	
	public Map<String,Object> get(String isbn) {
		WebClient webClient = WebClient.builder().clientConnector(connector()).baseUrl("https://dapi.kakao.com").build();
		@SuppressWarnings("unchecked")
		Map<String,Object> response = webClient.get().uri(uriBuilder -> uriBuilder.path("/v3/search/book")
																					.queryParam("target","isbn")
																					.queryParam("query",isbn)
															.build())
															.header("Authorization", "KakaoAK e8aa5630dd5a7f452e5ee85250f3118f")
															.retrieve().bodyToMono(Map.class).block();
		return response;
	}
}
