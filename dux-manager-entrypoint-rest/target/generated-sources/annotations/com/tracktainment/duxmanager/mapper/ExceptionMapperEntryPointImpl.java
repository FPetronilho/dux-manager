package com.tracktainment.duxmanager.mapper;

import com.tracktainment.duxmanager.exception.BusinessException;
import com.tracktainment.duxmanager.exception.ExceptionDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-09T23:34:35+0200",
    comments = "version: 1.6.2, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class ExceptionMapperEntryPointImpl implements ExceptionMapperEntryPoint {

    @Override
    public ExceptionDto toExceptionDto(BusinessException e) {
        if ( e == null ) {
            return null;
        }

        ExceptionDto.ExceptionDtoBuilder exceptionDto = ExceptionDto.builder();

        exceptionDto.code( e.getCode() );
        exceptionDto.httpStatusCode( e.getHttpStatusCode() );
        exceptionDto.reason( e.getReason() );
        exceptionDto.message( e.getMessage() );

        return exceptionDto.build();
    }
}
