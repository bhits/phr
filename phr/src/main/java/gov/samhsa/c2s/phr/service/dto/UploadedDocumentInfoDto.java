package gov.samhsa.c2s.phr.service.dto;

import gov.samhsa.c2s.phr.domain.UploadedDocumentContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadedDocumentInfoDto {
    @NotEmpty
    private String documentId;

    @NotEmpty
    private String documentFileName;

    @NotEmpty
    private String documentName;

    @Valid
    private UploadedDocumentContentType documentContentType;

    private String documentDescription;


    public UploadedDocumentInfoDto(String documentId, String documentFileName, String documentName){
        this.documentId = documentId;
        this.documentFileName = documentFileName;
        this.documentName = documentName;
    }
}
