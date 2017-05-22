package gov.samhsa.c2s.phr.service.exception;

public class DocumentTypeCodeNotFoundException extends Exception {
    public DocumentTypeCodeNotFoundException() {}

    public DocumentTypeCodeNotFoundException(String message) {
        super(message);
    }

    public DocumentTypeCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentTypeCodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public DocumentTypeCodeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
