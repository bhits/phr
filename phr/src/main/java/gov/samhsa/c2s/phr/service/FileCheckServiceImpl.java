package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.config.PhrProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileCheckServiceImpl implements FileCheckService {
    private final PhrProperties phrProperties;

    private long maxFileSize;

    @Autowired
    public FileCheckServiceImpl(PhrProperties phrProperties) {
        this.phrProperties = phrProperties;

        this.maxFileSize = phrProperties.getPatientDocumentUploads().getMaximumUploadFileSize();
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
