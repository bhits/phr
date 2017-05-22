package gov.samhsa.c2s.phr.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DocumentDeleteException extends RuntimeException {
    public DocumentDeleteException() {}

    public DocumentDeleteException(String message) {
        super(message);
    }

    public DocumentDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentDeleteException(Throwable cause) {
        super(cause);
    }

    public DocumentDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
