package com.library.proto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.library.proto.book.domain.Book;
import com.library.proto.book.domain.BookRepository;
import com.library.proto.book.infra.WebClientServiceImpl;

@SpringBootTest
public class ElasticsearchApplicationTest2 {
	
	@Test
	void test() {
		String hostname = "localhost";
		int port = 9200;
		String scheme = "http";
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host));
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery("문학", "authors","title","publisher"));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(100);
		
		SearchRequest request = new SearchRequest("test_table_data");
		request.source(searchSourceBuilder);
		
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			SearchHits searchHits = response.getHits();
			
			for(SearchHit hit:searchHits ) {
				Map<String,Object> sourceMap = hit.getSourceAsMap();
			}
			
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
