package ru.clevertec.ecl.exception;

public enum ExceptionCode {
    ERROR_INPUT_DATA(40400),
    CONNECTION_DATABASE_ERROR(40701),
    TAG_NOT_FOUND(40401),
    USER_NOT_FOUND(40201),
    NOT_VALID_USER_NAME(40202),
    GIFT_CERTIFICATE_NOT_FOUND(40301),
    NOT_VALID_TAG_DATA(40403),
    NOT_VALID_GIFT_CERTIFICATE_DATA(40600),
    NOT_VALID_GIFT_CERTIFICATE_NAME(40601),
    NOT_VALID_GIFT_CERTIFICATE_DESCRIPTION(40602),
    NOT_VALID_GIFT_CERTIFICATE_PRICE(40603),
    NOT_VALID_GIFT_CERTIFICATE_DURATION(40604),
    WRONG_FIND_PARAMETERS(40501),
    ORDER_NOT_FOUND(40801);

    private final int errorCode;

    ExceptionCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}