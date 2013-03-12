--
-- Table structure for table newsletter_document_section
--
DROP TABLE IF EXISTS newsletter_document_section;
CREATE TABLE newsletter_document_section (
  id_section INT NOT NULL,
  id_template INT NOT NULL,
  PRIMARY KEY (id_section)
);

--
-- Table structure for table newsletter_document_category
--
DROP TABLE IF EXISTS newsletter_document_category;
CREATE TABLE newsletter_document_category (
  id_category INT NOT NULL,
  id_section INT NOT NULL,
  PRIMARY KEY (id_category,id_section)
);