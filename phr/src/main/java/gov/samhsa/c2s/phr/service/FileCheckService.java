package gov.samhsa.c2s.phr.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileCheckService {
    /**
     * Checks to see if a file's size is over the configured maximum limit in bytes
     *
     * @param file - the file whose size is to be checked
     * @return true if file size is over the max, false if file size is equal to or less than the max
     */
    boolean isFileOversized(MultipartFile file);
}
