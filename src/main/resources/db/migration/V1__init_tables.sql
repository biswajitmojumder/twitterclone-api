CREATE TABLE tbl_players
(
	id BIGINT PRIMARY KEY auto_increment,
	userName VARCHAR(100) NOT NULL UNIQUE,
	fullName VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL,
	password VARCHAR(255) NOT NULL,
	imageUrl VARCHAR(255) NOT NULL,
	createdAt BIGINT NOT NULL,
	lastModified BIGINT NOT NULL
);

CREATE TABLE tbl_tweets
(
	id BIGINT PRIMARY KEY auto_increment,
    playerId BIGINT NOT NULL,
    message varchar(200) NOT NULL,
    imageUrl varchar(500) NOT NULL,
    replyOf BIGINT NOT NULL,
    retweetOf BIGINT NOT NULL,
    createdAt BIGINT NOT NULL,
    lastModified BIGINT NOT NULL,
    FOREIGN KEY (playerId) REFERENCES tbl_players(id) ON DELETE CASCADE
);

CREATE TABLE tbl_reactions
(
	id BIGINT PRIMARY KEY auto_increment,
    tweetId BIGINT NOT NULL,
    playerId BIGINT NOT NULL,
    reactionType varchar(25) NOT NULL,
    createdAt BIGINT NOT NULL,
    lastModified BIGINT NOT NULL,
    FOREIGN KEY (playerId) REFERENCES tbl_players(id) ON DELETE CASCADE,
    FOREIGN KEY (tweetId) REFERENCES tbl_tweets(id) ON DELETE CASCADE
);

CREATE TABLE tbl_follows
(
	id BIGINT PRIMARY KEY auto_increment,
    playerId BIGINT NOT NULL,
    followerId BIGINT NOT NULL,
    createdAt BIGINT NOT NULL,
    lastModified BIGINT NOT NULL,
	FOREIGN KEY (playerId) REFERENCES tbl_players(id) ON DELETE CASCADE,
	FOREIGN KEY (followerId) REFERENCES tbl_players(id) ON DELETE CASCADE
);

CREATE TABLE tbl_tags
(
	id BIGINT PRIMARY KEY auto_increment,
    tweetId BIGINT NOT NULL,
    tagName varchar(25) NOT NULL,
    createdAt BIGINT NOT NULL,
    lastModified BIGINT NOT NULL,
	FOREIGN KEY (tweetId) REFERENCES tbl_tweets(id) ON DELETE CASCADE
);