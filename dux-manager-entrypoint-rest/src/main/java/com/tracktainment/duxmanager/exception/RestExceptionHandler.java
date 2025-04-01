package com.tracktainment.duxmanager.exception;

import com.tracktainment.duxmanager.mapper.ExceptionMapperEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final ExceptionMapperEntryPoint mapper;

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ExceptionDto> handleBusinessException(BusinessException e) {
        return new ResponseEntity<>(
                mapper.toExceptionDto(e),
                new HttpHeaders(),
                e.getHttpStatusCode()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ExceptionDto> handleGlobalException(Exception e) {
        return handleBusinessException(
                new  InternalServerErrorException(e.getMessage())
        );
    }
}
