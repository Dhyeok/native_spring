package com.polarbookshop.catalogservice.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public record Book ( //도메인 모델은 불가변 객체인 레코드로 구현

    @Id // 이 필드를 엔티티에 대한 기본 키로 식별한다.
    Long id,

    @NotBlank( // 이 필드는 널 값이 되어서는 안되고 화이트 스페이가 아닌 문자를 최소 하나 이상 있어야 함
            message = "The book ISBN must be defined."
    )

    @Pattern( // 이 필드는 주어진 정규표현식의 값과 일치하는 형식을 가져와야 한다(표준 isbn 형식)
            regexp = "^([0-9]{10}|[0-9]{13})$",
            message = "The ISBN format must be valid."
    )
    String isbn,

    @NotBlank(message = "The book title must be defined.")
    String title,

    @NotBlank(message = "The book author must be defined.")
    String author,

    @NotNull(message = "The book price must be defined.")

    @Positive( // 이 필드는 널값이 되어서는 안되고 0보다 큰 값을 가져와야 함
            message = "The book price must be greater than zero."
    )
    Double price

    @CreatedDate // 엔티티가 생성된 때
    Instant createdDate,

    @LastModifiedDate // 엔티티가 마지막으로 수정된 때
    Instant lastModifiedDate,

    @Version
    int version

){
    public static Book of(String isbn, String title, String author, Double price, String publisher) {
        return new Book(null, isbn, title, author, price, publisher, null, null, 0); // id가 널이고 버전이 0이면 새로운 엔티티로 인식한다.
    }


}