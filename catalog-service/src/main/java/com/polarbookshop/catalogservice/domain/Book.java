package com.polarbookshop.catalogservice.domain;

public record Book ( //도메인 모델은 불가변 객체인 레코드로 구현
                     String isbn, // 책을 고유하게 식별
                     String title,
                     String author,
                     Double price
){}