package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.domain.DocumentTypeCode;
import gov.samhsa.c2s.phr.domain.UploadedDocument;
import gov.samhsa.c2s.phr.domain.UploadedDocumentRepository;
import gov.samhsa.c2s.phr.service.dto.SaveNewUploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.SavedNewUploadedDocumentResponseDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import gov.samhsa.c2s.phr.service.exception.DocumentDeleteException;
import gov.samhsa.c2s.phr.service.exception.DocumentNameExistsException;
import gov.samhsa.c2s.phr.service.exception.DocumentSaveException;
import gov.samhsa.c2s.phr.service.exception.DocumentTypeCodeNotFoundException;
import gov.samhsa.c2s.phr.service.exception.InvalidInputException;
import gov.samhsa.c2s.phr.service.exception.NoDocumentsFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UploadedDocumentServiceImpl implements UploadedDocumentService {
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final DocumentTypeCodeService documentTypeCodeService;
    private final ModelMapper modelMapper;

    @Autowired
    public UploadedDocumentServiceImpl(
            UploadedDocumentRepository uploadedDocumentRepository,
            DocumentTypeCodeService documentTypeCodeService,
            ModelMapper modelMapper) {
        super();
        this.uploadedDocumentRepository = uploadedDocumentRepository;
        this.documentTypeCodeService = documentTypeCodeService;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UploadedDocumentInfoDto> getPatientDocumentInfoList(String patientMrn) {
        if((patientMrn != null) && (patientMrn.length() > 0)){
            List<UploadedDocument> uploadedPatientDocumentsList = uploadedDocumentRepository.findAllByPatientMrn(patientMrn);

            if(uploadedPatientDocumentsList.size() > 0){
                List<UploadedDocumentInfoDto> uploadedDocumentInfoDtoList = new ArrayList<>();

                uploadedPatientDocumentsList.forEach(uploadedDocument -> {
                    UploadedDocumentInfoDto uploadedDocumentInfoDto = modelMapper.map(uploadedDocument, UploadedDocumentInfoDto.class);
                    uploadedDocumentInfoDtoList.add(uploadedDocumentInfoDto);
                });

                return uploadedDocumentInfoDtoList;
            }else{
                log.error("No documents were found for the specified patientMrn (patientMrn: " + patientMrn + ") in the getPatientDocumentInfoList method");
                throw new NoDocumentsFoundException("No documents found for specified patient MRN");
            }
        }else{
            log.error("The patientMrn value passed to the getPatientDocumentInfoList method was null or empty");
            throw new InvalidInputException("Patient MRN cannot be null or empty");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UploadedDocumentDto getPatientDocumentByDocId(String patientMrn, Long documentId) {
        UploadedDocumentDto uploadedDocumentDto;

        if((patientMrn != null) && (patientMrn.length() > 0)){
            UploadedDocument uploadedDocument = uploadedDocumentRepository.findOne(documentId);

            if(uploadedDocument != null){
                if(Objects.equals(patientMrn, uploadedDocument.getPatientMrn())){
                    uploadedDocumentDto = modelMapper.map(uploadedDocument, UploadedDocumentDto.class);
                }else{
                    log.error("The document requested in the call to the getPatientDocumentByDocId method (documentId: " + documentId + ") does not belong to the patient specified by the patientMrn parameter value passed to the method (patientMrn: " + patientMrn + ")");
                    throw new NoDocumentsFoundException("No document found with the specified document ID");
                }
            }else{
                log.error("No documents were found with the specified document ID: " + documentId);
                throw new NoDocumentsFoundException("No document found with the specified document ID");
            }
        }else{
            log.error("The patientMrn value passed to the getPatientDocumentInfoList method was null or empty");
            throw new InvalidInputException("Patient MRN cannot be null or empty");
        }

        return uploadedDocumentDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedNewUploadedDocumentResponseDto saveNewPatientDocument(SaveNewUploadedDocumentDto saveNewUploadedDocumentDto) {
        if(saveNewUploadedDocumentDto == null){
            log.error("The saveNewUploadedDocumentDto parameter value passed to saveNewPatientDocument method was null");
            throw new InvalidInputException("The system could not save the uploaded file");
        }

        String newDocumentName = saveNewUploadedDocumentDto.getDocumentName();

        if(isDocumentNameDuplicateForPatient(newDocumentName, saveNewUploadedDocumentDto.getPatientMrn())){
            log.info("A patient tried to upload a document with document name '" + newDocumentName + "', however the patient already has an uploaded document with that document name.");
            throw new DocumentNameExistsException("The specified patient already has a document with the same document name");
        }

        // TODO: Validate uploaded CCDA/C32 file using document validator service

        DocumentTypeCode documentTypeCode;
        try{
            documentTypeCode = documentTypeCodeService.getDocumentTypeCodeById(saveNewUploadedDocumentDto.getDocumentTypeCodeId());
        }catch(DocumentTypeCodeNotFoundException e){
            log.error("The saveNewUploadedDocumentDto.documentTypeCodeId parameter value passed to saveNewPatientDocument method was not a valid document type code ID", e);
            throw new InvalidInputException("The system could not save the uploaded file");
        }

        UploadedDocument newUploadedDocument = new UploadedDocument();
        newUploadedDocument.setPatientMrn(saveNewUploadedDocumentDto.getPatientMrn());
        newUploadedDocument.setDocumentContents(saveNewUploadedDocumentDto.getDocumentContents());
        newUploadedDocument.setDocumentContentType(saveNewUploadedDocumentDto.getDocumentContentType());
        newUploadedDocument.setDocumentDescription(saveNewUploadedDocumentDto.getDocumentDescription());
        newUploadedDocument.setDocumentFileName(saveNewUploadedDocumentDto.getDocumentFileName());
        newUploadedDocument.setDocumentName(saveNewUploadedDocumentDto.getDocumentName());
        newUploadedDocument.setDocumentTypeCode(documentTypeCode);

        UploadedDocument savedUploadedDocument;

        try {
            savedUploadedDocument =  uploadedDocumentRepository.save(newUploadedDocument);
        }catch (DataAccessException e){
            log.error("A DataAccessException occurred while attempting to save a new uploaded patient document in UploadedDocumentServiceImpl.saveNewPatientDocument method", e);
            throw new DocumentSaveException("An error occurred while attempting to save a new document");
        }

        if(savedUploadedDocument == null){
            log.error("When the saveNewPatientDocument method invoked the document repository's 'save' method to save a new patient document, the repository's save method returned null instead of the newly saved 'UploadedDocument' entity");
            throw new DocumentSaveException("An error occurred while attempting to save a new document");
        }

        return modelMapper.map(savedUploadedDocument, SavedNewUploadedDocumentResponseDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePatientDocument(String patientMrn, Long documentId){
        if((patientMrn == null) || (patientMrn.length() <= 0)){
            log.error("The patientMrn value passed to the deletePatientDocument method was null or empty");
            throw new InvalidInputException("Patient MRN cannot be null or empty");
        }

        if((documentId == null) || (documentId < 0)){
            log.error("The documentId value passed to the deletePatientDocument method was null or a negative number");
            throw new InvalidInputException("Document ID cannot be null or a negative number");
        }

        UploadedDocument uploadedDocument = uploadedDocumentRepository.findOne(documentId);

        if(uploadedDocument == null){
            log.error("No documents were found with the specified document ID: " + documentId);
            throw new NoDocumentsFoundException("No document found with the specified document ID");
        }

        if(!Objects.equals(patientMrn, uploadedDocument.getPatientMrn())){
            log.error("The document requested in the call to the deletePatientDocument method (documentId: " + documentId + ") does not belong to the patient specified by the patientMrn parameter value passed to the method (patientMrn: " + patientMrn + ")");
            throw new NoDocumentsFoundException("No document found with the specified document ID");
        }

        try {
            uploadedDocumentRepository.delete(uploadedDocument);
        }catch (DataAccessException e){
            log.error("A DataAccessException occurred while attempting to delete a patient document in UploadedDocumentServiceImpl.deletePatientDocument method", e);
            throw new DocumentDeleteException("An error occurred while attempting to delete a document");
        }
    }


    /**
     * Checks to see if the specified patient has any existing uploaded documents with the
     * same document name as newDocumentName string passed into this method as a parameter
     *
     * @param newDocumentName - The document name to check for duplicates
     * @param patientMrn - The MRN of the patient whose documents should be checked for duplicate document names
     * @return true if duplicate(s) found; false if no duplicates found
     */
    private boolean isDocumentNameDuplicateForPatient(String newDocumentName, String patientMrn){
        List<UploadedDocument> patientUploadedDocuments = uploadedDocumentRepository.findAllByPatientMrn(patientMrn);
        return patientUploadedDocuments.stream()
                .anyMatch(doc -> doc.getDocumentName().equals(newDocumentName));
    }
}
