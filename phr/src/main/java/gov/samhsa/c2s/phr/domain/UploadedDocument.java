package gov.samhsa.c2s.phr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class UploadedDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
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

    @NotNull
    @ManyToOne
    @JsonIgnore
    private DocumentTypeCode documentTypeCode;
}
