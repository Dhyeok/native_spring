package com.polarbookshop.catalogservice.demo;

import java.util.List;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("testdata") // 이 클래스를 testdata 프로파일에 할당, 이 클래스는 testdata프로파일이 활성화 할 때만 로드 된다.
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class) // applicationREadyEvent가 발생하면 테스트 데이터 생성이 시작된다. 이 이벤트는 애블리케이션 시작 단계가 완료되면 발생한다.
    public void loadBookTestData() {
        bookRepository.deleteAll(); // 빈 데이터베이스로 시작하기 위해 기존 책이 있다면 모두 삭제한다.
        var book1 = new Book.of("1234567891", "Northern Lights", "Lyra Silverstar", 9.90);
        var book2 = new Book.of("1234567892", "Polar Journey", "Iorek Polarson", 12.90);
        bookRepository.saveAll(List.of(book1, book2));
    }
}
