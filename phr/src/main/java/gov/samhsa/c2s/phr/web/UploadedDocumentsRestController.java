package gov.samhsa.c2s.phr.web;

import gov.samhsa.c2s.phr.service.DocumentTypeCodeService;
import gov.samhsa.c2s.phr.service.UploadedDocumentService;
import gov.samhsa.c2s.phr.service.dto.DocumentTypeCodeDto;
import gov.samhsa.c2s.phr.service.dto.SaveNewUploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.SavedNewUploadedDocumentResponseDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import gov.samhsa.c2s.phr.service.exception.DocumentSaveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/uploadedDocuments")
public class UploadedDocumentsRestController {
    private final UploadedDocumentService uploadedDocumentService;
    private final DocumentTypeCodeService documentTypeCodeService;

    @Autowired
    public UploadedDocumentsRestController(UploadedDocumentService uploadedDocumentService, DocumentTypeCodeService documentTypeCodeService){
        this.uploadedDocumentService = uploadedDocumentService;
        this.documentTypeCodeService = documentTypeCodeService;
    }

    /**
     * Gets a list of all document type codes
     *
     * @return A List of all document type code DTOs
     * @see DocumentTypeCodeDto
     */
    @GetMapping("/documentTypeCodes")
    public List<DocumentTypeCodeDto> getAllDocumentTypeCodesList(){
        return documentTypeCodeService.getAllDocumentTypeCodes();
    }

    /**
     * Gets a list of documents for the specified patient
     *
     * @param patientMrn - the MRN of the patient whose list of documents is to be retrieved
     * @return An List of UploadedDocumentInfoDto objects containing metadata about each of the specified patient's documents
     * @see UploadedDocumentInfoDto
     */
    @GetMapping("/patient/{patientMrn}/documentsList")
    public List<UploadedDocumentInfoDto> getPatientDocumentsList(@PathVariable String patientMrn){
        return uploadedDocumentService.getPatientDocumentInfoList(patientMrn);
    }

    /**
     * Gets a specific document by document ID
     * <p>
     * This method requires the patient MRN as a parameter in order to confirm
     * that the document being retrieved belongs to the specified patient.
     *
     * @param patientMrn - the MRN of the patient whose document is to be retrieved
     * @param documentId - the ID of the document to be retrieved
     * @return An UploadedDocumentDto object containing the requested document, as well as metadata about the document
     * @see UploadedDocumentDto
     */
    @GetMapping("/patient/{patientMrn}/document/{documentId}")
    public UploadedDocumentDto getPatientDocument(@PathVariable String patientMrn, @PathVariable Long documentId){
        return uploadedDocumentService.getPatientDocumentByDocId(patientMrn, documentId);
    }

    /**
     * Saves new uploaded patient document file
     *
     * @param patientMrn - the MRN of the patient for whom the uploaded file belongs to
     * @param file - the file to be saved
     * @param documentName - the user chosen name of the file being uploaded (this may or may not be identical to the fileName)
     * @param description - An optional description of the file being uploaded
     * @param documentTypeCodeId - The document type
     * @return A SavedNewUploadedDocumentResponseDto object containing metadata about the newly saved patient document;
     *         - if the specified patient already has document saved with the same
     *           documentName, an HTTP 409 - CONFLICT status code will be returned.
     * @see SavedNewUploadedDocumentResponseDto
     */
    @PostMapping("/patient/{patientMrn}/document")
    public SavedNewUploadedDocumentResponseDto saveNewPatientDocument(
            @PathVariable String patientMrn,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentName") String documentName,
            @RequestParam("description") String description,
            @RequestParam("documentTypeCodeId") Long documentTypeCodeId
    ){
        // TODO: Invoke ClamAV scanner service to scan uploaded file for viruses before doing anything else.

        SaveNewUploadedDocumentDto saveNewUploadedDocumentDto;

        try{
            saveNewUploadedDocumentDto = uploadedDocumentService.generateSaveDtoForDoc(patientMrn, file, documentName, description, documentTypeCodeId);
        }catch (IOException e){
            log.error("An IOException occurred while invoking UploadedDocumentService.generateSaveDtoForDoc from inside the saveNewPatientDocument controller method", e);
            throw new DocumentSaveException("An error occurred while attempting to save a new document");
        }

        return uploadedDocumentService.saveNewPatientDocument(saveNewUploadedDocumentDto);
    }

    /**
     * Deletes a document by document ID
     * <p>
     * This method requires the patient MRN as a parameter in order to confirm
     * that the document being deleted belongs to the specified patient.
     *
     * @param patientMrn - The MRN of the patient whom the document to be deleted belongs to
     * @param documentId - The ID of the document to delete
     */
    @DeleteMapping("/patient/{patientMrn}/document/{documentId}")
    public void deletePatientDocument(@PathVariable String patientMrn, @PathVariable Long documentId){
        uploadedDocumentService.deletePatientDocument(patientMrn, documentId);
    }

}
