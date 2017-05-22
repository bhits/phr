package gov.samhsa.c2s.phr.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class DocumentTypeCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Length(max = 50)
    private String displayName;

    @NotEmpty
    @Length(max = 50)
    private String code;

    @NotEmpty
    @Length(max = 50)
    private String codeSystem;

    @NotEmpty
    @Length(max = 50)
    private String codeSystemVersion;

    @Length(max = 255)
    private String codeSystemName;
}
