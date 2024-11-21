CREATE TABLE voting (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    creator_user_id BIGINT NOT NULL
);

CREATE TABLE candidate (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    votes INTEGER DEFAULT 0,
    voting_id BIGINT REFERENCES voting(id) ON DELETE CASCADE
);

CREATE TABLE vote (
    id BIGSERIAL PRIMARY KEY,
    voting_id BIGINT REFERENCES voting(id) ON DELETE CASCADE,
    candidate_id BIGINT REFERENCES candidate(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL
);
