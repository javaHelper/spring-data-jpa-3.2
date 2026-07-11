Database
Schema (Auto-Generated)

-- Users Table
CREATE TABLE users
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name       VARCHAR(255),
    last_name        VARCHAR(255),
    username         VARCHAR(255),
    street           VARCHAR(255),
    city             VARCHAR(255),
    state            VARCHAR(255),
    zip_code         VARCHAR(255),
    country          VARCHAR(255),
    personal_email   VARCHAR(255),
    personal_phone   VARCHAR(255),
    personal_website VARCHAR(255),
    work_email       VARCHAR(255),
    work_phone       VARCHAR(255),
    work_website     VARCHAR(255),
    department       VARCHAR(255),
    job_title        VARCHAR(255)
);

-- Companies Table
CREATE TABLE companies
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255),
    street              VARCHAR(255),
    city                VARCHAR(255),
    state               VARCHAR(255),
    zip_code            VARCHAR(255),
    country             VARCHAR(255),
    email               VARCHAR(255),
    phone               VARCHAR(255),
    website             VARCHAR(255),
    registration_number VARCHAR(255),
    tax_id              VARCHAR(255),
    founded_date        DATE,
    industry            VARCHAR(255),
    employee_count      INT,
    ceo_name            VARCHAR(255)
);
