package gov.samhsa.c2s.phr.service.mapping;

import gov.samhsa.c2s.phr.domain.UploadedDocument;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class UploadedDocumentToUploadedDocumentDtoMap extends PropertyMap<UploadedDocument, UploadedDocumentDto> {
    @Override
    protected void configure() {
        map().setId(source.getId());
        map().setFileName(source.getFileName());
        map().setDocumentName(source.getDocumentName());
        map().setContentType(source.getContentType());
        map().setDescription(source.getDescription());
        map().setContents(source.getContents());
        map().setPatientMrn(source.getPatientMrn());
        map().setDocumentTypeCodeId(source.getDocumentTypeCode().getId());
        map().setDocumentTypeDisplayName(source.getDocumentTypeCode().getDisplayName());
    }
}
