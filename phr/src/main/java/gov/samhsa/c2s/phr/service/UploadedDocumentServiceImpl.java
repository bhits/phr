package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.domain.UploadedDocument;
import gov.samhsa.c2s.phr.domain.UploadedDocumentRepository;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import gov.samhsa.c2s.phr.service.exception.InvalidInputException;
import gov.samhsa.c2s.phr.service.exception.NoPatientDocumentsFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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
                throw new NoPatientDocumentsFoundException("No documents found for specified patient MRN");
            }
        }else{
            throw new InvalidInputException("Patient MRN cannot be null or empty");
        }
    }
}
