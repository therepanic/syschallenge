CREATE TABLE IF NOT EXISTS users_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(73),
    registered_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS users_linked_social_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    type VARCHAR(255) NOT NULL,
    verification VARCHAR(255) NOT NULL
);