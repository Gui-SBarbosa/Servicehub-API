CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CLIENT',
    phone VARCHAR(20),
    bio TEXT,
    avatar_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT chk_users_email_low CHECK (email = LOWER(email)),
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'PROVIDER', 'CLIENT'))
);

CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    provider_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_services_provider FOREIGN KEY (provider_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT chk_services_price CHECK (price > 0),
    CONSTRAINT chk_services_category CHECK (category IN ('LIMPEZA', 'DIARISTA', 'ENCANAMENTO', 'ELETRICA', 'JARDINAGEM', 'PINTURA', 'MARCENARIA', 'PEDREIRO', 'DEDETIZACAO', 'AR_CONDICIONADO', 'CHAVEIRO', 'MONTAGEM_MOVEIS', 'MUDANCA', 'SUPORTE_TECNICO', 'MANUTENCAO_COMPUTADORES', 'DESENVOLVIMENTO_SOFTWARE', 'REDES_E_INFRAESTRUTURA', 'SEGURANCA_DIGITAL', 'RECUPERACAO_DE_DADOS', 'CONSULTORIA_TI', 'OUTROS'))
);
CREATE INDEX idx_services_provider ON services(provider_id);
CREATE INDEX idx_services_category ON services(category) WHERE is_active;

CREATE TABLE service_requests (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    scheduled_at TIMESTAMP,
    notes TEXT,
    total_price NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_requests_service FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE RESTRICT,
    CONSTRAINT fk_requests_client FOREIGN KEY (client_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT chk_requests_status CHECK (status IN ('PENDING', 'ACCEPTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT chk_requests_price CHECK (total_price > 0)
);
CREATE INDEX idx_requests_service ON service_requests(service_id);
CREATE INDEX idx_requests_client ON service_requests(client_id);
CREATE INDEX idx_requests_status ON service_requests(status);

CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    rating SMALLINT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_reviews_request UNIQUE (request_id),
    CONSTRAINT fk_reviews_request FOREIGN KEY (request_id) REFERENCES service_requests(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reviews_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT chk_reviews_rating CHECK (rating BETWEEN 1 AND 5)
);
CREATE INDEX idx_reviews_reviewer ON reviews(reviewer_id);
