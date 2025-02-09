CREATE TABLE IF NOT EXISTS companies_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(60) UNIQUE NOT NULL,
    name VARCHAR(60) NOT NULL,
    updated_at TIMESTAMP NOT NULL
);