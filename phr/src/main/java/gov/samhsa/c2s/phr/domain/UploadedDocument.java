package gov.samhsa.c2s.phr.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data
public class UploadedDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String documentContentType;

    private String documentDescription;
}
