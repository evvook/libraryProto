package com.library.proto;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElasticsearchApplicationTest3 {
	
	@Test
	void test() {
		String hostname = "localhost";
		int port = 9200;
		String scheme = "http";
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host).setDefaultHeaders(compatibilityHeaders()));
		
		String indexName = "test_table_data";
		
		IndexRequest request = new IndexRequest(indexName).id("B0000000001");
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
														.startObject()
															.field("id","B0000000001")
															.field("title","밝은 밤")
															.field("authors","최은영")
															.field("publisher","문학동네")
														.endObject();
			request.source(builder);
			IndexResponse response = client.index(request, RequestOptions.DEFAULT);
			System.out.println(response.status());
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Header[] compatibilityHeaders() {
	     return new Header[]{
	             new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"),
	             new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")
	     };
	 }
}
