package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.service.dto.SaveNewUploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.SavedNewUploadedDocumentResponseDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadedDocumentService {
    /**
     * Gets list of uploaded documents metadata for a specific patient
     *
     * @param patientMrn - The MRN of the patient whose uploaded documents metadata is being queried
     * @return A list of UploadedDocumentInfoDto objects with information about each of the patient's uploaded documents
     */
    @Transactional(readOnly = true)
    List<UploadedDocumentInfoDto> getPatientDocumentInfoList(String patientMrn);

    /**
     * Gets a specific document by document ID
     * <p>
     * This method requires the patient MRN as a parameter in order to confirm
     * that the document being retrieved belongs to the specified patient.
     *
     * @param patientMrn - The MRN of the patient whom the queried document belongs to
     * @param id - The ID of the document to retrieve
     * @return An UploadedDocumentDto which contains the document itself, as well as document metadata
     */
    @Transactional(readOnly = true)
    UploadedDocumentDto getPatientDocumentByDocId(String patientMrn, Long id);


    /**
     * Saves new uploaded patient document file
     * <p>
     * If the specified patient already has document saved with the same
     * documentName, a DocumentNameExistsException will be thrown, which
     * will trigger a 409 - CONFLICT HTTP status code response to be sent
     * back to the client.
     * @see gov.samhsa.c2s.phr.service.exception.DocumentNameExistsException
     *
     * @param patientMrn - the MRN of the patient for whom the uploaded file belongs to
     * @param file - the file to be saved
     * @param documentName - the user chosen name of the file being uploaded (this may or may not be identical to the fileName)
     * @param description - A description of the file being uploaded (this value can be null)
     * @param documentTypeCodeId - The document type
     * @return An object containing metadata about the newly saved patient document file, including the system generated id
     * @see SavedNewUploadedDocumentResponseDto
     */
    @Transactional
    SavedNewUploadedDocumentResponseDto saveNewPatientDocument(String patientMrn, MultipartFile file, String documentName, String description, Long documentTypeCodeId);

    /**
     * Deletes a document by document ID
     * <p>
     * This method requires the patient MRN as a parameter in order to confirm
     * that the document being deleted belongs to the specified patient.
     *
     * @param patientMrn - The MRN of the patient whom the document to be deleted belongs to
     * @param id - The ID of the document to delete
     */
    @Transactional
    void deletePatientDocument(String patientMrn, Long id);
}
