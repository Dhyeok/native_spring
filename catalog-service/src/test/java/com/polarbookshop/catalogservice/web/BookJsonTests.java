package com.polarbookshop.catalogservice.web;

import java.time.Instant;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest // json 직렬화에 중점을 둔 테스트 클래스임을 나타냄
class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json; // json 직렬화 및 역질렬화를 확인하기 위한 유틸리티 클래스

    @Test
    void testSerialize() throws Exception {
        var now = Instant.now();
        var book = new Book(394L, "1234567890", "Title", "Author", 9.90, "Polarsophia", now, now, 21);
        var jsonContent = json.write(book);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(book.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn") // jsonpath 형식을 사용해 json 객체를 탐색하고 자바의 json 변환을 확인한다.
                .isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());
        assertThat(jsonContent).extractingJsonPathStringValue("@.publisher")
                .isEqualTo(book.publisher());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(book.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
                .isEqualTo(book.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                .isEqualTo(book.version());
    }
    }

    @Test
    void testDeserialize() throws Exception {
        // 1. 고정된 시간값 준비
        var instant = Instant.parse("2021-09-07T22:50:37.135029Z");

        // 2. JSON 문자열 준비
        var content = """  
                {
                    "id": 394,
                    "isbn": "1234567890",
                    "title": "Title",
                    "author": "Author",
                    "price": 9.90,
                    "publisher": "Polarsophia",
                    "createdDate": "2021-09-07T22:50:37.135029Z",
                    "lastModifiedDate": "2021-09-07T22:50:37.135029Z",
                    "version": 21
                }
                """;

        // 자바 텍스트 블록 기능을 사용해 json 객체를 정의
        assertThat(json.parse(content)) // json에서 자바 객체로의 변환을 확인 3. JSON을 Book 객체로 변환 후 비교
                .usingRecursiveComparison()
                .isEqualTo(new Book(
                        394L, "1234567890", "Title", "Author", 9.90, "Polarsophia", instant, instant, 21
                ));
     }

}
