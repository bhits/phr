package gov.samhsa.c2s.phr.infrastructure;

import feign.Headers;
import feign.Param;
import gov.samhsa.c2s.phr.config.MultipartSupportConfig;
import gov.samhsa.c2s.phr.infrastructure.dto.ValidationResponseDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "document-validator", configuration = MultipartSupportConfig.class)
// Get configured context-path
@RequestMapping(value = "${c2s.phr.document-validator.context-path:/}")
@Service
public interface DocumentValidatorService {

    @RequestMapping(value = "/multipartFileDocumentValidation", method = RequestMethod.POST)
    @Headers("Content-Type: multipart/form-data")
    ValidationResponseDto validateClinicalDocumentFile(@Param("documentFile") MultipartFile documentFile);
}
