package gov.samhsa.c2s.phr.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentTypeCodeDto {
    @NotEmpty
    private Long id;

    @NotEmpty
    private String displayName;

    @NotEmpty
    private String code;

    @NotEmpty
    private String codeSystem;

    @NotEmpty
    private String codeSystemVersion;

    private String codeSystemName;
}
