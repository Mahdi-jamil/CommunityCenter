CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT DEFAULT nextval('user_sequence') NOT NULL,
    created_date DATE,
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255) CHECK (role IN ('USER','ADMIN')),
    username VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE SEQUENCE IF NOT EXISTS user_sequence;

CREATE TABLE IF NOT EXISTS community (
    community_id BIGINT DEFAULT nextval('community_seq') NOT NULL,
    description VARCHAR(255),
    name VARCHAR(255),
    number_of_members INT DEFAULT 1,
    creator_id BIGINT,
    PRIMARY KEY (community_id),
    FOREIGN KEY (creator_id) REFERENCES users(user_id)
);

CREATE SEQUENCE IF NOT EXISTS community_seq;

CREATE TABLE IF NOT EXISTS post (
    post_id BIGINT DEFAULT nextval('post_seq') NOT NULL,
    body VARCHAR(255),
    last_update DATE,
    title VARCHAR(255),
    votes INT DEFAULT 0,
    author_id BIGINT,
    posted_in_community_id BIGINT,
    PRIMARY KEY (post_id),
    FOREIGN KEY (author_id) REFERENCES users(user_id),
    FOREIGN KEY (posted_in_community_id) REFERENCES community(community_id)
);

CREATE SEQUENCE IF NOT EXISTS post_seq
    START WITH 1
    INCREMENT BY 2
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGINT DEFAULT nextval('comment_seq') NOT NULL,
    body VARCHAR(255),
    last_update TIMESTAMP(6),
    votes INTEGER,
    user_id BIGINT,
    parent_comment_id BIGINT,
    post_id BIGINT,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (parent_comment_id) REFERENCES comment(comment_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id)
);

CREATE SEQUENCE IF NOT EXISTS comment_seq;

CREATE TABLE IF NOT EXISTS tags (
    tag_id BIGSERIAL NOT NULL,
    tag_name VARCHAR(255),
    PRIMARY KEY (tag_id)
);

CREATE TABLE IF NOT EXISTS community_tag (
    community_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    FOREIGN KEY (community_id) REFERENCES community(community_id),
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id)
);

CREATE SEQUENCE IF NOT EXISTS community_tag_seq;

CREATE TABLE IF NOT EXISTS user_community (
    user_id BIGINT NOT NULL,
    community_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (community_id) REFERENCES community(community_id)
);

CREATE SEQUENCE IF NOT EXISTS user_community_seq;
