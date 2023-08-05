package com.library.proto.book.infra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import com.library.proto.book.domain.SearchEngin;

@Component
public class ElasticearchBookSearchEngin implements SearchEngin{
	String hostname = "localhost";
	int port = 9200;
	String scheme = "http";
	String indexName = "test_table_data";
	
	public List<String> searchId(String query) {
		List<String> idList = new ArrayList<String>();
		
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host));
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "authors","title","publisher"));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(100);
		
		SearchRequest request = new SearchRequest(indexName);
		request.source(searchSourceBuilder);
		
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			SearchHits searchHits = response.getHits();
			
			for(SearchHit hit:searchHits ) {
				Map<String,Object> sourceMap = hit.getSourceAsMap();
				idList.add((String)sourceMap.get("id"));
			}
			
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idList;
	}
	
	public boolean isDocumentExists(String id) {
		boolean result = false;
		HttpHost host = new HttpHost(hostname, port, scheme);
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host).setDefaultHeaders(compatibilityHeaders()));
		try {
			GetRequest request = new GetRequest(indexName).id(id);
			result = client.exists(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void createDocument(Map<String,Object> bookInfo) {
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host).setDefaultHeaders(compatibilityHeaders()));
		
		IndexRequest request = new IndexRequest(indexName).id((String)bookInfo.get("id"));
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
														.startObject()
															.field("id",bookInfo.get("id"))
															.field("title",bookInfo.get("title"))
															.field("authors",bookInfo.get("author_names"))
															.field("publisher",bookInfo.get("publisher"))
														.endObject();
			request.source(builder);
			IndexResponse response = client.index(request, RequestOptions.DEFAULT);
			System.out.println(response.status());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateDocument(Map<String,Object> bookInfo) {
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host).setDefaultHeaders(compatibilityHeaders()));
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject()
					.field("id",bookInfo.get("id"))
					.field("title",bookInfo.get("title"))
					.field("authors",bookInfo.get("author_names"))
					.field("publisher",bookInfo.get("publisher"))
					.endObject();
			UpdateRequest request = new UpdateRequest().index(indexName).id((String)bookInfo.get("id")).doc(builder);
			UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
			System.out.println(response.status());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void deleteDocument(String id) {
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host).setDefaultHeaders(compatibilityHeaders()));
		
		DeleteRequest request = new DeleteRequest(indexName).id(id);
		
		try {
			DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
			System.out.println(response.status());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Header[] compatibilityHeaders() {
	     return new Header[]{
	             new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"),
	             new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")
	     };
	 }
}
