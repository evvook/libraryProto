package com.library.proto.book.infra;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.proto.book.domain.Author;
import com.library.proto.book.domain.Book;
import com.library.proto.book.domain.BookRepository;

@Repository
public class MybatisBookRepository implements BookRepository{

	private String nameSpace = "BookMapper";
	
	@Autowired
	SqlSession session;
	
	@Override
	public List<Book> findAll() {
		// TODO Auto-generated method stub
		return session.selectList(nameSpace+".retrieveAllBooks");
	}

	@Override
	public Map<String, String> findId(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return session.selectOne(nameSpace+".retrieveId", paramMap);
	}

	@Override
	public int createAuthor(Author author) {
		// TODO Auto-generated method stub
		return session.insert(nameSpace+".insertAuthor", author);
	}

	@Override
	public int updateAuthor(Author author) {
		return session.update(nameSpace+".updateAuthor", author);
	}
	
	@Override
	public int createBook(Book book) {
		// TODO Auto-generated method stub
		return session.insert(nameSpace+".insertBook",book);
	}

	@Override
	public int updateBook(Book book) {
		return session.update(nameSpace+".updateBook",book);
	}
	
	@Override
	public Map<String, String> getDate(Map<String, Object> bookInfo) {
		// TODO Auto-generated method stub
		return session.selectOne(nameSpace+".getDate", bookInfo);
	}

	@Override
	public List<Book> findByKeywords(Map<String, String> param) {
		// TODO Auto-generated method stub
		return session.selectList(nameSpace+".retrieveBooks", param);
	}

	@Override
	public Book findById(Map<String, String> param) {
		// TODO Auto-generated method stub
		return session.selectOne(nameSpace+".retrieveBook", param);
	}

	@Override
	public Map<String, String> findById2(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return session.selectOne(nameSpace+".retrieveBookById", param);
	}

	@Override
	public int findAuthorSeq(Map<String, String> authorMap) {
		// TODO Auto-generated method stub
		return session.selectOne(nameSpace+".findAuthorSeq",authorMap);
	}

	@Override
	public int deleteBook(Book book) {
		// TODO Auto-generated method stub
		return session.delete(nameSpace+".deleteBook", book);
	}

	@Override
	public int deleteAuthor(Author author) {
		// TODO Auto-generated method stub
		return session.delete(nameSpace+".deleteAuthor", author);
	}

	@Override
	public Map<String, String> findAuthorById(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return session.selectOne(nameSpace+".retrieveAuthor", param);
	}

}
