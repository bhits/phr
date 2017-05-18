package gov.samhsa.c2s.phr.service;

import gov.samhsa.c2s.phr.config.PhrProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class FileCheckServiceImpl implements FileCheckService {
    private static final String PERMITTED_EXTENSIONS_DELIMITER = ",";
    private static final String FILE_EXTENSION_DELIMITER = ".";

    private final PhrProperties phrProperties;

    private String permittedExtensions;
    private List<String> permittedExtensionsList;

    @Autowired
    public FileCheckServiceImpl(PhrProperties phrProperties) {
        this.phrProperties = phrProperties;

        this.permittedExtensions = phrProperties.getPatientDocumentUploads().getExtensionsPermittedToUpload().toLowerCase();
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        this.permittedExtensions = phrProperties.getPatientDocumentUploads().getExtensionsPermittedToUpload().toLowerCase();

        this.permittedExtensionsList = Arrays.asList(permittedExtensions.split(PERMITTED_EXTENSIONS_DELIMITER));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFileExtensionPermitted(MultipartFile file){
        boolean isExtensionPermitted = false;
        String fileName = file.getOriginalFilename();
        int indexOfDot = fileName.lastIndexOf(FILE_EXTENSION_DELIMITER);

        /* Do not allow any files that start with '.', regardless of their extension,
           as those types of files can have special significance in some file systems
           and could pose a security risk. */
        if(indexOfDot > 0){
            String fileExtension = fileName.substring(indexOfDot + 1).toLowerCase();
            isExtensionPermitted = permittedExtensionsList.contains(fileExtension);
        }

        return isExtensionPermitted;
    }
}
