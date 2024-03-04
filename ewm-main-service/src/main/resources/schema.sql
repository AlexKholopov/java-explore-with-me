CREATE TABLE users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   email VARCHAR(255),
   name VARCHAR(255),
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE category (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255),
   CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   annotation VARCHAR(255),
   description VARCHAR(255),
   location VARCHAR(255),
   event_date VARCHAR(255),
   title VARCHAR(255),
   paid BOOLEAN NOT NULL,
   request_moderation BOOLEAN NOT NULL,
   participant_limit INTEGER NOT NULL,
   category BIGINT,
   CONSTRAINT pk_events PRIMARY KEY (id)
);

ALTER TABLE events ADD CONSTRAINT FK_EVENTS_ON_CATEGORY FOREIGN KEY (category) REFERENCES category (id);