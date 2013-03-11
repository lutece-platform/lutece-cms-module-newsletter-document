DROP TABLE IF EXISTS newsletter_document_section;
CREATE TABLE newsletter_document_section (
  id_section INT DEFAULT NOT NULL,
  id_template INT DEFAULT NOT NULL,
  PRIMARY KEY (id_section)
);