CREATE TABLE hits (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   app VARCHAR(255),
   uri VARCHAR(255),
   ip VARCHAR(255),
   timestamp TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_hits PRIMARY KEY (id)
);