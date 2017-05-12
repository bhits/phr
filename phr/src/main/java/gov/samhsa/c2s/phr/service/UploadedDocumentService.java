package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.service.dto.UploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UploadedDocumentService {
    /**
     * Gets list of uploaded documents metadata for a specific patient
     *
     * @param patientMrn - The MRN of the patient whose uploaded documents metadata is being queried
     * @return A list of UploadedDocumentInfoDto objects with information about each of the patient's uploaded documents
     */
    @Transactional
    List<UploadedDocumentInfoDto> getPatientDocumentInfoList(String patientMrn);

    /**
     * Gets a specific document by document ID
     *
     *    This method requires the patient MRN as a parameter in order to confirm
     *    that the document being retrieved belongs to the specified patient.
     *
     * @param patientMrn - The MRN of the patient whom the queried document belongs to
     * @param documentId - The ID of the document to retrieve
     * @return An UploadedDocumentDto which contains the document itself, as well as document metadata
     */
    @Transactional
    UploadedDocumentDto getPatientDocumentByDocId(String patientMrn, Long documentId);
}
