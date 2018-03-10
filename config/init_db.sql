CREATE TABLE resume
(
  uuid      CHAR(36)  NOT NULL PRIMARY KEY,
  full_name VARCHAR   NOT NULL
);

-- auto-generated definition
CREATE TABLE contact
(
  id          SERIAL   NOT NULL
              CONSTRAINT contact_pkey
              PRIMARY KEY,
  resume_uuid CHAR(36) NOT NULL
              CONSTRAINT contact_resume_uuid_fkey
              REFERENCES resume
              ON DELETE CASCADE,
  type        VARCHAR  NOT NULL,
  value       VARCHAR  NOT NULL
);

CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (resume_uuid, type);

CREATE TABLE section
(
  id          SERIAL   NOT NULL
              CONSTRAINT section_pkey
              PRIMARY KEY,
  resume_uuid CHAR(36) NOT NULL
              CONSTRAINT section_resume_uuid_fkey
              REFERENCES resume
              ON DELETE CASCADE,
  type        VARCHAR  NOT NULL,
  value       VARCHAR
);

CREATE UNIQUE INDEX section_resume_uuid_fkey
  ON section (resume_uuid, type);