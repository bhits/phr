package gov.samhsa.c2s.phr.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileCheckService {
    /**
     * Checks to see if a file's extension is permitted based on the configured permitted extensions list
     *
     * @param file - the file whose extension is to be checked
     * @return true if file extension is permitted; false if file extension is not permitted
     */
    boolean isFileExtensionPermitted(MultipartFile file);
}
