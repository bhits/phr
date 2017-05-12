package gov.samhsa.c2s.phr.domain;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;

@Entity
@Data
@Audited
public class UploadedDocument {
    @Id
    private Long documentId;

    @NotEmpty
    private String patientMrn;

    @NotEmpty
    private byte[] documentContents;

    @NotEmpty
    private String documentFileName;

    @NotEmpty
    private String documentName;

    @Valid
    @NotEmpty
    private UploadedDocumentContentType documentContentType;

    private String documentDescription;
}
