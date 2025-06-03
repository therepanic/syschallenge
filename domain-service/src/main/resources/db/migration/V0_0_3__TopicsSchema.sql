CREATE TABLE IF NOT EXISTS topics_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(50) NOT NULL
);