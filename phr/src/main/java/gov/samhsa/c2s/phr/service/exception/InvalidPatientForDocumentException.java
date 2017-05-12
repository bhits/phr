package gov.samhsa.c2s.phr.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidPatientForDocumentException extends RuntimeException {
    public InvalidPatientForDocumentException() {
    }

    public InvalidPatientForDocumentException(String message) {
        super(message);
    }

    public InvalidPatientForDocumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPatientForDocumentException(Throwable cause) {
        super(cause);
    }

    public InvalidPatientForDocumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
