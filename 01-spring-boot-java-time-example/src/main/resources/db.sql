-- Events Table with TIMESTAMP for Instant
CREATE TABLE events
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_name   VARCHAR(255),
    description  VARCHAR(255),
    event_time   TIMESTAMP,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    cancelled_at TIMESTAMP,
    location     VARCHAR(255),
    status       VARCHAR(255)
);

-- Publications Table with INT for Year
CREATE TABLE publications
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(255),
    author           VARCHAR(255),
    isbn             VARCHAR(255),
    publication_year INT,
    copyright_year   INT,
    publisher        VARCHAR(255),
    page_count       INT,
    added_at         TIMESTAMP,
    last_modified_at TIMESTAMP,
    rating DOUBLE
);

-- Audit Logs Table with both TIMESTAMP and INT Year
CREATE TABLE audit_logs
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type      VARCHAR(255),
    entity_id        BIGINT,
    action           VARCHAR(255),
    changed_by       VARCHAR(255),
    change_timestamp TIMESTAMP,
    audit_year       INT,
    old_value        VARCHAR(255),
    new_value        VARCHAR(255),
    description      VARCHAR(255)
);