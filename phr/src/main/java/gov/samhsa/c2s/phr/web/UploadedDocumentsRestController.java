package gov.samhsa.c2s.phr.web;

import gov.samhsa.c2s.phr.service.UploadedDocumentService;
import gov.samhsa.c2s.phr.service.dto.UploadedDocumentInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/uploadedDocuments")
public class UploadedDocumentsRestController {
    private final UploadedDocumentService uploadedDocumentService;

    @Autowired
    public UploadedDocumentsRestController(UploadedDocumentService uploadedDocumentService) {
        this.uploadedDocumentService = uploadedDocumentService;
    }

    @GetMapping("/patient/{patientMrn}/documentsList")
    public List<UploadedDocumentInfoDto> getPatientDocumentsList(@PathVariable String patientMrn){
        return uploadedDocumentService.getPatientDocumentInfoList(patientMrn);
    }
}
