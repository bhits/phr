package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.domain.DocumentTypeCode;
import gov.samhsa.c2s.phr.domain.DocumentTypeCodeRepository;
import gov.samhsa.c2s.phr.service.dto.DocumentTypeCodeDto;
import gov.samhsa.c2s.phr.service.exception.DocumentTypeCodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class DocumentTypeCodeServiceImpl implements DocumentTypeCodeService {
    private final ModelMapper modelMapper;
    private final DocumentTypeCodeRepository documentTypeCodeRepository;

    @Autowired
    public DocumentTypeCodeServiceImpl(ModelMapper modelMapper, DocumentTypeCodeRepository documentTypeCodeRepository) {
        this.modelMapper = modelMapper;
        this.documentTypeCodeRepository = documentTypeCodeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentTypeCodeDto> getAllDocumentTypeCodes() {
        List<DocumentTypeCode> documentTypeCodeList = documentTypeCodeRepository.findAll();

        return documentTypeCodeList.stream()
                .map(typeCode -> modelMapper.map(typeCode, DocumentTypeCodeDto.class))
                .collect(toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentTypeCode getDocumentTypeCodeById(Long id) throws DocumentTypeCodeNotFoundException {
        return documentTypeCodeRepository.findOneById(id).orElseThrow(() -> {
            log.warn("No document type code found with id: " + id);
            return new DocumentTypeCodeNotFoundException("No document type code found with specified id");
        });
    }
}
