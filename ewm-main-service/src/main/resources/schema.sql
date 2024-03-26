DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilation_event CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   email VARCHAR(255) UNIQUE,
   name VARCHAR(255),
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255) UNIQUE,
   CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   pinned BOOLEAN NOT NULL,
   title VARCHAR(255),
   CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   annotation VARCHAR,
   description VARCHAR,
   created_on TIMESTAMP WITHOUT TIME ZONE,
   location VARCHAR(255),
   event_date TIMESTAMP WITHOUT TIME ZONE,
   title VARCHAR(255),
   paid BOOLEAN NOT NULL,
   request_moderation BOOLEAN NOT NULL,
   participant_limit INTEGER NOT NULL,
   initiator BIGINT,
   category BIGINT,
   state VARCHAR(255),
   available BOOLEAN NOT NULL,
   CONSTRAINT pk_events PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   created TIMESTAMP WITHOUT TIME ZONE,
   event BIGINT,
   requester BIGINT,
   status VARCHAR(255),
   CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_event (
  compilation_id BIGINT NOT NULL,
   event_id BIGINT NOT NULL,
   CONSTRAINT pk_compilation_event PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   last_update TIMESTAMP WITHOUT TIME ZONE,
   comment VARCHAR(255),
   updated BOOLEAN,
   commenter_id BIGINT,
   event_id BIGINT,
   CONSTRAINT pk_comments PRIMARY KEY (id)
);


ALTER TABLE comments ADD CONSTRAINT FK_COMMENTS_ON_COMMENTERID FOREIGN KEY (commenter_id) REFERENCES users (id);
ALTER TABLE comments ADD CONSTRAINT FK_COMMENTS_ON_EVENTID FOREIGN KEY (event_id) REFERENCES events (id);
ALTER TABLE compilation_event ADD CONSTRAINT fk_comeve_on_compilation FOREIGN KEY (compilation_id) REFERENCES compilations (id);
ALTER TABLE compilation_event ADD CONSTRAINT fk_comeve_on_event FOREIGN KEY (event_id) REFERENCES events (id);
ALTER TABLE events ADD CONSTRAINT FK_EVENTS_ON_CATEGORY FOREIGN KEY (category) REFERENCES category (id);
ALTER TABLE events ADD CONSTRAINT FK_EVENTS_ON_INITIATOR FOREIGN KEY (initiator) REFERENCES users (id);
ALTER TABLE requests ADD CONSTRAINT FK_REQUESTS_ON_EVENT FOREIGN KEY (event) REFERENCES events (id);
ALTER TABLE requests ADD CONSTRAINT FK_REQUESTS_ON_PARTICIPANT FOREIGN KEY (requester) REFERENCES users (id);

CREATE INDEX category_name ON category(lower(name));

CREATE INDEX events_annotation ON events(lower(annotation));
CREATE INDEX events_description ON events(lower(description));
CREATE INDEX events_created_on ON events(created_on);
CREATE INDEX events_event_date ON events(created_on);
CREATE INDEX events_title ON events(lower(title));
CREATE INDEX events_participant_limit ON events(participant_limit);

CREATE INDEX compilations_title ON compilations(lower(title));
