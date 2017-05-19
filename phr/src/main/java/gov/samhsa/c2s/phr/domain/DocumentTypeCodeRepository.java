package gov.samhsa.c2s.phr.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeCodeRepository extends JpaSpecificationExecutor<DocumentTypeCode>, JpaRepository<DocumentTypeCode, Long> {
    Optional<DocumentTypeCode> findOneById(Long id);
}
