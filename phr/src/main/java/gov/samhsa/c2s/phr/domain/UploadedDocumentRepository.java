package gov.samhsa.c2s.phr.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadedDocumentRepository extends JpaRepository<UploadedDocument, Long> {
    List<UploadedDocument> findAllByPatientMrn(String patientMrn);
}
