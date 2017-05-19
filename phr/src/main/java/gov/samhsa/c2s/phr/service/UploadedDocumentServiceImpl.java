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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UploadedDocumentServiceImpl implements UploadedDocumentService {
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final DocumentTypeCodeService documentTypeCodeService;
    private final FileCheckService fileCheckService;
    private final ModelMapper modelMapper;

    @Autowired
    public UploadedDocumentServiceImpl(
            UploadedDocumentRepository uploadedDocumentRepository,
            DocumentTypeCodeService documentTypeCodeService,
            FileCheckService fileCheckService,
            ModelMapper modelMapper) {
        super();
        this.uploadedDocumentRepository = uploadedDocumentRepository;
        this.documentTypeCodeService = documentTypeCodeService;
        this.fileCheckService = fileCheckService;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UploadedDocumentInfoDto> getPatientDocumentInfoList(String patientMrn) {
        if((patientMrn == null) || (patientMrn.length() <= 0)){
            log.error("The patientMrn value passed to the getPatientDocumentInfoList method was null or empty");
            throw new InvalidInputException("Patient MRN cannot be null or empty");
        }

        List<UploadedDocument> uploadedPatientDocumentsList = uploadedDocumentRepository.findAllByPatientMrn(patientMrn);

        if(uploadedPatientDocumentsList.size() <= 0){
            log.error("No documents were found for the specified patientMrn (patientMrn: " + patientMrn + ") in the getPatientDocumentInfoList method");
            throw new NoDocumentsFoundException("No documents found for specified patient MRN");
        }

        List<UploadedDocumentInfoDto> uploadedDocumentInfoDtoList = new ArrayList<>();

        uploadedPatientDocumentsList.forEach(uploadedDocument -> {
            UploadedDocumentInfoDto uploadedDocumentInfoDto = modelMapper.map(uploadedDocument, UploadedDocumentInfoDto.class);
            uploadedDocumentInfoDtoList.add(uploadedDocumentInfoDto);
        });

        return uploadedDocumentInfoDtoList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UploadedDocumentDto getPatientDocumentByDocId(String patientMrn, Long id) {
        if((patientMrn == null) || (patientMrn.length() <= 0)){
            log.error("The patientMrn value passed to the getPatientDocumentInfoList method was null or empty");
            throw new InvalidInputException("Patient MRN cannot be null or empty");
        }

        UploadedDocument uploadedDocument = uploadedDocumentRepository.findOneById(id).orElseThrow(() -> {
            log.error("No documents were found with the specified document ID: " + id);
            return new NoDocumentsFoundException("No document found with the specified document ID");
        });

        if(!Objects.equals(patientMrn, uploadedDocument.getPatientMrn())){
            log.error("The document requested in the call to the getPatientDocumentByDocId method (document ID: " + id + ") does not belong to the patient specified by the patientMrn parameter value passed to the method (patientMrn: " + patientMrn + ")");
            throw new NoDocumentsFoundException("No document found with the specified document ID");
        }

        return modelMapper.map(uploadedDocument, UploadedDocumentDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedNewUploadedDocumentResponseDto saveNewPatientDocument(String patientMrn, MultipartFile file, String documentName, String description, Long documentTypeCodeId) {
        SaveNewUploadedDocumentDto saveNewUploadedDocumentDto;

        saveNewUploadedDocumentDto = generateSaveDtoForDoc(patientMrn, file, documentName, description, documentTypeCodeId);

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
        newUploadedDocument.setContents(saveNewUploadedDocumentDto.getContents());
        newUploadedDocument.setContentType(saveNewUploadedDocumentDto.getContentType());
        newUploadedDocument.setDescription(saveNewUploadedDocumentDto.getDescription());
        newUploadedDocument.setFileName(saveNewUploadedDocumentDto.getFileName());
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
    public void deletePatientDocument(String patientMrn, Long id){
        if((patientMrn == null) || (patientMrn.length() <= 0)){
            log.error("The patientMrn value passed to the deletePatientDocument method was null or empty");
            throw new InvalidInputException("Patient MRN cannot be null or empty");
        }

        if((id == null) || (id < 0)){
            log.error("The document ID value passed to the deletePatientDocument method was null or a negative number");
            throw new InvalidInputException("Document ID cannot be null or a negative number");
        }

        UploadedDocument uploadedDocument = uploadedDocumentRepository.findOneById(id).orElseThrow(() -> {
            log.error("No documents were found with the specified document ID: " + id);
            return new NoDocumentsFoundException("No document found with the specified document ID");
        });

        if(!Objects.equals(patientMrn, uploadedDocument.getPatientMrn())){
            log.error("The document requested in the call to the deletePatientDocument method (document ID: " + id + ") does not belong to the patient specified by the patientMrn parameter value passed to the method (patientMrn: " + patientMrn + ")");
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
     * Generates a SaveNewUploadedDocumentDto object to be used to save an uploaded document
     *
     * @param patientMrn - the MRN of the patient for whom the uploaded file belongs to
     * @param file - the file to be save
     * @param documentName - the user chosen name of the file being uploaded (this may or may not be identical to the fileName)
     * @param description - A description of the file being uploaded (this value can be null)
     * @param documentTypeCodeId - The document type
     * @return A SaveNewUploadedDocumentDto object containing the file and associated metadata to be saved
     * @see SaveNewUploadedDocumentDto
     */
    private SaveNewUploadedDocumentDto generateSaveDtoForDoc(
            String patientMrn,
            MultipartFile file,
            String documentName,
            String description,
            Long documentTypeCodeId
    ){
        if((patientMrn == null) || (patientMrn.length() <= 0)){
            log.error("Unable to generate a SaveNewUploadedDocumentDto in generateSaveDtoForDoc method because value of patientMrn parameter is null or empty");
            throw new InvalidInputException("The patient MRN value cannot be null or empty");
        }

        if(file == null){
            log.error("Unable to generate a SaveNewUploadedDocumentDto in generateSaveDtoForDoc method because value of file parameter is null");
            throw new InvalidInputException("The file cannot be null");
        }

        if((documentName == null) || (documentName.length() <= 0)){
            log.error("Unable to generate a SaveNewUploadedDocumentDto in generateSaveDtoForDoc method because value of documentName parameter is null or empty");
            throw new InvalidInputException("The document name value cannot be null or empty");
        }

        if(documentTypeCodeId == null){
            log.error("Unable to generate a SaveNewUploadedDocumentDto in generateSaveDtoForDoc method because value of documentTypeCodeId parameter is null");
            throw new InvalidInputException("The document type code ID cannot be null");
        }

        // Check if the file's extension is one of the configured permitted extension types
        if(!fileCheckService.isFileExtensionPermitted(file)){
            log.error("The uploaded file (filename: " + file.getOriginalFilename() + ") was not saved because the file extension is not a permitted extension type");
            throw new InvalidInputException("The uploaded file's extension was not a permitted extension type");
        }

        byte[] uploadedFileBytes;

        try {
            // extract file content as byte array
            uploadedFileBytes = file.getBytes();
        }catch(IOException e){
            log.error("An IOException occurred while invoking file.getBytes from inside the generateSaveDtoForDoc method", e);
            throw new DocumentSaveException("An error occurred while attempting to save a new document");
        }

        if(uploadedFileBytes.length <= 0){
            log.error("The byte array extracted from the uploaded MultipartFile object was empty (uploadedFileBytes.length: " + uploadedFileBytes.length + ")");
            throw new InvalidInputException("The uploaded file was empty");
        }

        return new SaveNewUploadedDocumentDto(patientMrn, uploadedFileBytes, file.getOriginalFilename(), documentName, file.getContentType(), description, documentTypeCodeId);
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
