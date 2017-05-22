package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.domain.DocumentTypeCode;
import gov.samhsa.c2s.phr.service.dto.DocumentTypeCodeDto;
import gov.samhsa.c2s.phr.service.exception.DocumentTypeCodeNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentTypeCodeService {

    /**
     * Gets a list of all document type codes
     *
     * @return A List of all document type code DTOs
     * @see DocumentTypeCodeDto
     */
    @Transactional(readOnly = true)
    List<DocumentTypeCodeDto> getAllDocumentTypeCodes();

    /**
     * Gets a document type code entity object by document type code ID
     *
     * @param id - the document type code id to retrieve
     * @return The DocumentTypeCode object for the requested document type code id
     * @see DocumentTypeCode
     * @throws DocumentTypeCodeNotFoundException - thrown if no document type code is found for requested document type code id
     */
    @Transactional(readOnly = true)
    DocumentTypeCode getDocumentTypeCodeById(Long id) throws DocumentTypeCodeNotFoundException;
}
