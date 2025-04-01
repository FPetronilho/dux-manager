package com.tracktainment.duxmanager.exception;

public class ResourceNotFoundException extends BusinessException {

    public static final String ERROR_MESSAGE = "%s %s not found.";

    public ResourceNotFoundException(Class<?> clazz, String resourceAttribute) {
        super(
                ExceptionCode.RESOURCE_NOT_FOUND,
                String.format(ERROR_MESSAGE, clazz.getSimpleName(), resourceAttribute)
        );
    }
}
