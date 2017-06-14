package gov.samhsa.c2s.phr.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentValidationResultDetail {
    private String description;
    private ValidationDiagnosticType diagnosticType;
    private String xPath;
    private String documentLineNumber;
    private boolean isSchemaError;
    private boolean isIGIssue;
}
