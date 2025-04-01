package com.tracktainment.duxmanager.exception;

public class ParameterValidationErrorException extends BusinessException {

    public ParameterValidationErrorException(String message) {
        super(ExceptionCode.PARAMETER_VALIDATION_ERROR, message);
    }
}
