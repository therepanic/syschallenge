CREATE TYPE user_role AS ENUM ('DEFAULT', 'ADMIN');
CREATE TABLE IF NOT EXISTS users_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(30) NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL,
    password VARCHAR(73),
    role user_role NOT NULL,
    registered_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS users_photo_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    object_key VARCHAR(70) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users_table(id)
);

CREATE TYPE user_linked_social_type AS ENUM ('GOOGLE', 'GITHUB');
CREATE TABLE IF NOT EXISTS users_linked_social_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    type user_linked_social_type NOT NULL,
    verification VARCHAR(100) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users_table(id)
);

CREATE TYPE user_basic_info_gender AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TABLE IF NOT EXISTS users_basic_info_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    name VARCHAR(30) NOT NULL,
    summary VARCHAR(512),
    birthday TIMESTAMP,
    gender user_basic_info_gender,

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