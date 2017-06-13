package gov.samhsa.c2s.phr.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadedDocumentInfoDto {
    @NotEmpty
    private Long id;

    @NotNull
    private boolean isSampleDocument;

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String documentName;

    @NotEmpty
    private String contentType;

    private String description;

    @NotEmpty
    private Long documentTypeCodeId;

    @NotEmpty
    private String documentTypeDisplayName;
}
