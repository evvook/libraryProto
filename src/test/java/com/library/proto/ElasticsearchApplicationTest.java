package com.library.proto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.library.proto.book.adapter.out.api.WebClientServiceImpl;
import com.library.proto.book.application.port.out.BookRepository;
import com.library.proto.book.domain.Book;

@SpringBootTest
public class ElasticsearchApplicationTest {
	
	@Test
	void test() {
		String hostname = "localhost";
		int port = 9200;
		String scheme = "http";
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host));
		
		try {
			GetRequest request = new GetRequest("test_table_data","B0000000002");
			GetResponse response = client.get(request, RequestOptions.DEFAULT);
			
			System.out.println(response.getSourceAsMap());
			
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
