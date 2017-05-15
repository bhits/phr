package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.domain.UploadedDocument;
import gov.samhsa.c2s.phr.domain.UploadedDocumentRepository;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentDto;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import gov.samhsa.c2s.phr.service.exception.InvalidInputException;
import gov.samhsa.c2s.phr.service.exception.InvalidPatientForDocumentException;
import gov.samhsa.c2s.phr.service.exception.NoDocumentsFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UploadedDocumentServiceImpl implements UploadedDocumentService {
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UploadedDocumentServiceImpl(UploadedDocumentRepository uploadedDocumentRepository, ModelMapper modelMapper) {
        this.uploadedDocumentRepository = uploadedDocumentRepository;
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
}
