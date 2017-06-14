package gov.samhsa.c2s.phr.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentValidationResultSummary {
    private String validationCriteria;
    private String validationType;
    private List<ValidationDiagnosticStatistics> diagnosticStatistics;
}
