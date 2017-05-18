package gov.samhsa.c2s.phr.service.mapping;

import gov.samhsa.c2s.phr.domain.UploadedDocument;
import gov.samhsa.c2s.phr.service.dto.SavedNewUploadedDocumentResponseDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class UploadedDocumentToSavedNewUploadedDocumentResponseDtoMap extends PropertyMap<UploadedDocument, SavedNewUploadedDocumentResponseDto> {
    @Override
    protected void configure() {
        map().setDocumentId(source.getDocumentId());
        map().setDocumentFileName(source.getDocumentFileName());
        map().setDocumentName(source.getDocumentName());
        map().setDocumentContentType(source.getDocumentContentType());
        map().setDocumentDescription(source.getDocumentDescription());
        map().setDocumentTypeCodeId(source.getDocumentTypeCode().getId());
        map().setDocumentTypeDisplayName(source.getDocumentTypeCode().getDisplayName());
    }
}
