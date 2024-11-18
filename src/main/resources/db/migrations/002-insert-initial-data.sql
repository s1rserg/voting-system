-- Insert initial data for voting
INSERT INTO voting (title, description, active, creator_user_id)
VALUES ('Presidential Election', 'Election for the new president', true, 1);

-- Insert initial candidates
INSERT INTO candidate (name, votes, voting_id)
VALUES ('Candidate A', 1, 1), ('Candidate B', 0, 1);

-- Insert a test vote
INSERT INTO vote (voting_id, candidate_id, user_id)
VALUES (1, 1, 2);
