CREATE TABLE auth.users
(
    id                 UUID PRIMARY KEY,
    email              VARCHAR(255) NOT NULL,
    password_hash      VARCHAR(255) NOT NULL,
    display_name       VARCHAR(100) NOT NULL,
    role               VARCHAR(20)  NOT NULL,
    status             VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    avatar_url         VARCHAR(500),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP,
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'EDITOR', 'DEFAULT_USER')),
    CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'SUSPENDED', 'DELETED'))
);

CREATE UNIQUE INDEX ux_users_email ON auth.users (lower(email));