package gov.samhsa.c2s.phr.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeCodeRepository extends JpaSpecificationExecutor<DocumentTypeCode>, JpaRepository<DocumentTypeCode, Long> {
}
