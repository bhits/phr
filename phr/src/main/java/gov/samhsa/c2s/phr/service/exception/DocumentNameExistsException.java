package gov.samhsa.c2s.phr.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DocumentNameExistsException extends RuntimeException {
    public DocumentNameExistsException() {}

    public DocumentNameExistsException(String message) {
        super(message);
    }

    public DocumentNameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNameExistsException(Throwable cause) {
        super(cause);
    }

    public DocumentNameExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
