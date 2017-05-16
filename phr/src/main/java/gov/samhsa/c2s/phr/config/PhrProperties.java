package gov.samhsa.c2s.phr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "c2s.phr")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhrProperties {
    @NotNull
    @Valid
    private PatientDocumentUploads patientDocumentUploads;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatientDocumentUploads {
        @Min(0)
        private long maximumUploadFileSize;

        @NotEmpty
        private String extensionsPermittedToUpload;
    }
}
