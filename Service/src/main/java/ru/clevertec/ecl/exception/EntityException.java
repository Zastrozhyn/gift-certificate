package ru.clevertec.ecl.exception;

public class EntityException extends RuntimeException{
    private final int errorCode;
    public EntityException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}