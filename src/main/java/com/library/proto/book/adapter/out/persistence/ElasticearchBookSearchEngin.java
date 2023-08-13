package com.library.proto.book.adapter.out.persistence;

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
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import com.library.proto.book.application.port.out.SearchEngin;
import com.library.proto.book.domain.Book;

@Component
public class ElasticearchBookSearchEngin implements SearchEngin{
	String hostname = "13.210.129.145";
	int port = 9200;
	String scheme = "http";
	String indexName = "library_data";
	
	public List<String> searchId(String query) {
		List<String> idList = new ArrayList<String>();
		
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host));
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		//searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "title.nori_mixed","authors.nori_mixed","publisher.nori_mixed"));
		searchSourceBuilder.query(QueryBuilders.disMaxQuery().add(QueryBuilders.matchQuery("title.nori_mixed", query).boost(2))
																.add(QueryBuilders.matchQuery("authors.nori_mixed", query))
																.add(QueryBuilders.matchQuery("publisher.nori_mixed", query)));
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
	
	public void createDocument(Book book) {
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host).setDefaultHeaders(compatibilityHeaders()));
		
		IndexRequest request = new IndexRequest(indexName).id(book.getId());
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
														.startObject()
															.field("id",book.getId())
															.field("title",book.getTitle())
															.field("authors",book.getAuthorNames())
															.field("publisher",book.getPublisher())
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
	
	public void updateDocument(Book book) {
		HttpHost host = new HttpHost(hostname, port, scheme);
		
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host).setDefaultHeaders(compatibilityHeaders()));
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject()
					.field("id",book.getId())
					.field("title",book.getTitle())
					.field("authors",book.getAuthorNames())
					.field("publisher",book.getPublisher())
					.endObject();
			UpdateRequest request = new UpdateRequest().index(indexName).id(book.getId()).doc(builder);
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
