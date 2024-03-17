INSERT INTO users (user_id,created_date, email, password, role, username)
VALUES (2,'2024-03-09', 'jamilmahdi77@gmail.com', '$2a$10$uhIMXxrIxoOndzyupR8QYOZ1597bezEI20G4ARW61/QkWeRKFKMlC', 'USER', 'mahdi'),
    (3,'2024-03-09', 'admin@example.com', '$2a$10$bLucA36JLRIKqyKJdeZ4Oe73GCvqY79RcjF0WkNqpqCidRIYTyufS', 'ADMIN', 'admin');

-- mahdi pass :spring
--admin pass :adminpassword


-- Inserting more data into the community table
INSERT INTO community (description, name, creator_id)
VALUES ('This is a community for developers.', 'Developers Community', 3),
       ('This is a community for designers.', 'Designers Community', 2),
       ('This is a community for writers.', 'Writers Community', 3),
       ('This is a community for artists.', 'Artists Community', 2);

-- Inserting more data into the post table with specific IDs
INSERT INTO post (post_id, body, last_update, title, author_id, posted_in_community_id)
VALUES (1, 'This is another post about programming.', '2024-03-09', 'Programming Post', 3, 1),
       (2, 'This is a post about graphic design.', '2024-03-09', 'Design Post', 2, 2),
       (3, 'This is a post about writing stories.', '2024-03-09', 'Writing Post', 3, 3),
       (4, 'This is a post about digital art.', '2024-03-09', 'Art Post', 2, 4);

-- Inserting more data into the comment table with specific post IDs
INSERT INTO comment (body, last_update, votes, user_id, parent_comment_id, post_id)
VALUES ('This is another comment on the programming post.', CURRENT_TIMESTAMP, 0, 3, NULL, 1),
       ('This is a reply to the graphic design post.', CURRENT_TIMESTAMP, 0, 2, 1, 2),
       ('This is a comment on the writing post.', CURRENT_TIMESTAMP, 0, 3, NULL, 3),
       ('This is a reply to the digital art post.', CURRENT_TIMESTAMP, 0, 2, 2, 4);

-- Inserting more data into the tags table
INSERT INTO tags (tag_name)
VALUES ('Technology'),
       ('Visual Arts');

-- Inserting more data into the community_tag table
INSERT INTO community_tag (community_id, tag_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 2);

-- Inserting more data into the user_community table
INSERT INTO user_community (user_id, community_id)
VALUES (3, 3),
       (2, 4);
