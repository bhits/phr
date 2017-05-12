package gov.samhsa.c2s.phr.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoPatientDocumentsFoundException extends RuntimeException {
    public NoPatientDocumentsFoundException() {
    }

    public NoPatientDocumentsFoundException(String message) {
        super(message);
    }

    public NoPatientDocumentsFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPatientDocumentsFoundException(Throwable cause) {
        super(cause);
    }

    public NoPatientDocumentsFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
