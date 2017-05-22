CREATE TABLE `document_type_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `code_system` varchar(50) NOT NULL,
  `code_system_version` varchar(50) NOT NULL,
  `code_system_name` varchar(255) DEFAULT NULL,
  `display_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_code_code_system_code_system_version` (`code_system`,`code_system_version`,`code`)
);

CREATE TABLE `uploaded_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contents` longblob NOT NULL,
  `content_type` varchar(255) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `file_name` varchar(255) NOT NULL,
  `document_name` varchar(255) NOT NULL,
  `patient_mrn` varchar(255) NOT NULL,
  `document_type_code_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_MRN` (`patient_mrn`),
  KEY `FK__idx` (`document_type_code_id`),
  CONSTRAINT `FK_document_type_code` FOREIGN KEY (`document_type_code_id`) REFERENCES `document_type_code` (`id`) ON UPDATE CASCADE
);
