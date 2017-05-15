package gov.samhsa.c2s.phr.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadedDocumentRepository extends JpaSpecificationExecutor<UploadedDocument>, JpaRepository<UploadedDocument, Long> {
    List<UploadedDocument> findAllByPatientMrn(String patientMrn);
}
