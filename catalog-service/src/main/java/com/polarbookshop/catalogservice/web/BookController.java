package com.polarbookshop.catalogservice.web;

import javax.validation.Valid;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController // 클래스가 스프링 컨포넌트이고 rest 엔드포인트를 위한 핸들러를 제공한다는 것을 표시하는 스테레오 타입 어노테이션
@RequestMapping("books") // 클래스가 핸들러를 제공하는 루트 패스 uri("/books")를 인식
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping // http get 매서드를 특정 핸들러 메서드로 연결
    public Iterable<Book> get() {
        return bookService.viewBookList();
    }

    @GetMapping("{isbn}") // 루트 패스 uri에 추가되는 uri 템플릿 변수 ("/books/{isbn}")
    public Book getByIsbn(@PathVariable String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping // http post 요청을 특정 핸들러 메서드로 연결
    @ResponseStatus(HttpStatus.CREATED) // 책이 성공적으로 생성되면 201 상태 코드를 반환
    public Book post(@Valid @RequestBody Book book) { // @RequestBody는 웹 요청의 본문을 메서드 변수로 바인드한다.
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}") // http delete 요청을 특정 핸들러 메서드로 연결
    @ResponseStatus(HttpStatus.NO_CONTENT) // 책이 성공적으로 삭제되면 204 상태 코드를 반환
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("{isbn}") // http put 요청을 특정 핸들러 메서드로 연결
    public Book put(@PathVariable String isbn, @Valid @RequestBody Book book) {
        return bookService.editBookDetails(isbn, book);
    }

}
