CREATE TABLE IF NOT EXISTS users_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(30) NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL,
    password VARCHAR(73),
    registered_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS users_linked_social_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    type VARCHAR(30) NOT NULL,
    verification VARCHAR(100) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users_table(id)
);

CREATE TABLE IF NOT EXISTS users_basic_info_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    name VARCHAR(30) NOT NULL,
    summary VARCHAR(512),
    birthday TIMESTAMP,
    gender VARCHAR(6),

    FOREIGN KEY (user_id) REFERENCES users_table(id)
);

CREATE TABLE IF NOT EXISTS users_occupation_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    company VARCHAR(60) NOT NULL,
    title VARCHAR(60) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users_table(id)
);