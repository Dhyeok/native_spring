package com.polarbookshop.catalogservice.web;

import java.util.HashMap;
import java.util.Map;

import com.polarbookshop.catalogservice.domain.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 클래스가 중앙식 예외 핸들러임을 표시
public class BookControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class) // 이 핸들러가 실행되어야 할 대상인 예외 정의
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bookNotFoundHandler(BookNotFoundException ex) {
        return ex.getMessage();// http 응답 본문에 포함할 메시지
    }

    @ExceptionHandler(BookAlreadyExistsException.class) // 이 핸들러가 실행되어야 할 대상인 예외 정의
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 예외를 발생할 때 http 응답에 포함할 상태 코드 정의
    String bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) { // 책 데이터 유효성 검증이 실패한 경우 발생하는 예외 처리
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage); // 빈 메시지 대신 의미 있는 오류 메시지를 위해 유효하지 않은 필드 확인
        });
        return errors;
    }

}
