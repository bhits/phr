package gov.samhsa.c2s.phr.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedDocumentRepository extends JpaRepository<UploadedDocument, String> {
    Page<UploadedDocument> findAllByPatientMrn(String patientMrn);
}
