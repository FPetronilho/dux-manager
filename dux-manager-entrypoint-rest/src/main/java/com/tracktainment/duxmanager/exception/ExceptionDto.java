package com.tracktainment.duxmanager.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDto {

    private String code;
    private int httpStatusCode;
    private String reason;
    private String message;
}
