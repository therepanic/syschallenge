CREATE TABLE IF NOT EXISTS companies_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(30) NOT NULL,
    name VARCHAR(50) NOT NULL,
    updated_at TIMESTAMP NOT NULL
);