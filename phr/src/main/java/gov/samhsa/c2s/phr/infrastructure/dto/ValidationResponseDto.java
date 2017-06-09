package gov.samhsa.c2s.phr.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ValidationResponseDto {
    private String documentType;
    @NotNull
    private boolean isDocumentValid;
    private DocumentValidationResultSummary validationResultSummary;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DocumentValidationResultDetail> validationResultDetails;
}
