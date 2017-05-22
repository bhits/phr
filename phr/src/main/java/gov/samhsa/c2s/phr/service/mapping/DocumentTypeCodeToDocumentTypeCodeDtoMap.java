package gov.samhsa.c2s.phr.service.mapping;

import gov.samhsa.c2s.phr.service.dto.DocumentTypeCodeDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeCodeToDocumentTypeCodeDtoMap extends PropertyMap<gov.samhsa.c2s.phr.domain.DocumentTypeCode, DocumentTypeCodeDto> {
    @Override
    protected void configure() {
        map().setId(source.getId());
        map().setCode(source.getCode());
        map().setCodeSystem(source.getCodeSystem());
        map().setCodeSystemName(source.getCodeSystemName());
        map().setCodeSystemVersion(source.getCodeSystemVersion());
        map().setDisplayName(source.getDisplayName());
    }
}
