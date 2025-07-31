package com.polarbookshop.catalogservice.domain;

import org.springframework.stereotype.Service;

@Service // 이 클래스는 스프링이 관리하는 서비스라는 것을 표시하는 스테레오타입 어노테이션
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository; // BookRepository를 생성자 오토와이어링을 통해 제공
    }

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn) // 존재하지 않는 책을 보려고 할 때 그에 해당하는 예외를 발생
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) { // 동일한 책을 여러 번 추가햐려고 시도하면 그에 해당하는 예외 발생
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    var bookToUpdate = new Book( // 책을 수정할 때 개체 식별자인 isbn 코들르 제외한 코든 필드를 수정할 수 있음
                            existingBook.isbn(),
                            book.title(),
                            book.author(),
                            book.price());
                    return bookRepository.save(bookToUpdate);
                })
                .orElseGet(() -> addBookToCatalog(book)); // 카탈로그에 존재하지 않는 책을 수정하려고 하면 새로운 책을 만듦
    }

}
