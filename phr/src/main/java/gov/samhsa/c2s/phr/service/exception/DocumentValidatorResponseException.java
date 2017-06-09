package gov.samhsa.c2s.phr.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DocumentValidatorResponseException extends RuntimeException {
    public DocumentValidatorResponseException() {}

    public DocumentValidatorResponseException(String message) {
        super(message);
    }
}
