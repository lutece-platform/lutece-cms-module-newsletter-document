--
-- Table structure for table newsletter_document_topic
--
DROP TABLE IF EXISTS newsletter_document_topic;
CREATE TABLE newsletter_document_topic (
  id_topic INT NOT NULL,
  id_template INT NOT NULL,
  PRIMARY KEY (id_topic)
);

--
-- Table structure for table newsletter_document_category
--
DROP TABLE IF EXISTS newsletter_document_category;
CREATE TABLE newsletter_document_category (
  id_category INT NOT NULL,
  id_topic INT NOT NULL,
  PRIMARY KEY (id_category,id_topic)
);