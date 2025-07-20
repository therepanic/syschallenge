CREATE TYPE problem_difficulty AS ENUM ('EASY', 'MEDIUM', 'HARD');
CREATE TABLE IF NOT EXISTS problems_table(
    id SERIAL PRIMARY KEY,
    title VARCHAR(80) NOT NULL,
    difficulty problem_difficulty NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS problem_prompts_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    problem_id SERIAL UNIQUE NOT NULL,
    generate_text TEXT NOT NULL,
    think_text TEXT NOT NULL,

    FOREIGN KEY (problem_id) REFERENCES problems_table(id)
);

CREATE TYPE problem_submission_status AS ENUM ('PENDING', 'PROCESSED');
CREATE TABLE IF NOT EXISTS problem_submissions_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    problem_id SERIAL NOT NULL,
    status problem_submission_status NOT NULL,
    created_at TIMESTAMP NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users_table(id),
    FOREIGN KEY (problem_id) REFERENCES problems_table(id)
);

CREATE TYPE problem_submission_message_side AS ENUM ('USER', 'ASSISTANT');
CREATE TABLE IF NOT EXISTS problem_submission_messages_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    problem_submission_id UUID NOT NULL,
    side problem_submission_message_side NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,

    FOREIGN KEY (problem_submission_id) REFERENCES problem_submissions_table(id)
);