package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.config.PhrProperties;
import gov.samhsa.c2s.phr.domain.UploadedDocument;
import gov.samhsa.c2s.phr.domain.UploadedDocumentRepository;
import gov.samhsa.c2s.phr.service.dto.SaveNewUploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.SavedNewUploadedDocumentResponseDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import gov.samhsa.c2s.phr.service.exception.DocumentSaveException;
import gov.samhsa.c2s.phr.service.exception.InvalidInputException;
import gov.samhsa.c2s.phr.service.exception.InvalidPatientForDocumentException;
import gov.samhsa.c2s.phr.service.exception.NoDocumentsFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UploadedDocumentServiceImpl implements UploadedDocumentService {
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final ModelMapper modelMapper;
    private final PhrProperties phrProperties;

    private long maxFileSize;

    @Autowired
    public UploadedDocumentServiceImpl(UploadedDocumentRepository uploadedDocumentRepository, ModelMapper modelMapper, PhrProperties phrProperties) {
        super();
        this.uploadedDocumentRepository = uploadedDocumentRepository;
        this.modelMapper = modelMapper;
        this.phrProperties = phrProperties;

        this.maxFileSize = this.phrProperties.getPatientDocumentUploads().getMaximumUploadFileSize();
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        this.maxFileSize = phrProperties.getPatientDocumentUploads().getMaximumUploadFileSize();
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
                    throw new InvalidPatientForDocumentException("The document requested does not belong to the patient specified");
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

        // TODO: Check file length is less than or equal to configured maximum file upload size

        // TODO: Check file extension is one of the configured permitted extensions

        // TODO: Check to make sure the patient doesn't already have any saved documents with the same documentName and/or documentFileName

        // TODO: Validate uploaded CCDA/C32 file using document validator service

        UploadedDocument newUploadedDocument = new UploadedDocument();
        newUploadedDocument.setPatientMrn(saveNewUploadedDocumentDto.getPatientMrn());
        newUploadedDocument.setDocumentContents(saveNewUploadedDocumentDto.getDocumentContents());
        newUploadedDocument.setDocumentContentType(saveNewUploadedDocumentDto.getDocumentContentType());
        newUploadedDocument.setDocumentDescription(saveNewUploadedDocumentDto.getDocumentDescription());
        newUploadedDocument.setDocumentFileName(saveNewUploadedDocumentDto.getDocumentFileName());
        newUploadedDocument.setDocumentName(saveNewUploadedDocumentDto.getDocumentName());

        UploadedDocument savedUploadedDocument =  uploadedDocumentRepository.save(newUploadedDocument);

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
    public boolean isFileOversized(MultipartFile file){
        if (file.getSize() > maxFileSize){
            log.warn("Size of uploaded file is " + file.getSize() + " bytes, which is greater than the configured max size of " + maxFileSize + " bytes");
            return true;
        }else {
            return false;
        }
    }
}
