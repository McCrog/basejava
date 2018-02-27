CREATE TABLE resume
(
  uuid      CHAR(36)  NOT NULL PRIMARY KEY,
  full_name VARCHAR   NOT NULL
);

CREATE TABLE contact
(
  id          SERIAL   NOT NULL PRIMARY KEY,
  resume_uuid CHAR(36) NOT NULL REFERENCES resume ON DELETE CASCADE,
  type        VARCHAR  NOT NULL,
  value       VARCHAR  NOT NULL
);

CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (resume_uuid, type);