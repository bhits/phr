package gov.samhsa.c2s.phr.domain;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data
@Audited
public class UploadedDocument {
    @Id
    private Long documentId;

    @NotEmpty
    private String patientMrn;

    @Lob
    @NotEmpty
    private byte[] documentContents;

    @NotEmpty
    private String documentFileName;

    @NotEmpty
    private String documentName;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private UploadedDocumentContentType documentContentType;

    private String documentDescription;
}
