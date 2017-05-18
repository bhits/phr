package gov.samhsa.c2s.phr.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class SaveNewUploadedDocumentDto {
    @NotEmpty
    private String patientMrn;

    @NotEmpty
    private byte[] documentContents;

    @NotEmpty
    private String documentFileName;

    @NotEmpty
    private String documentName;

    @NotEmpty
    private String documentContentType;

    private String documentDescription;

    @NotEmpty
    private Long documentTypeCodeId;
}
