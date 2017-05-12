package gov.samhsa.c2s.phr.service;

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
}
