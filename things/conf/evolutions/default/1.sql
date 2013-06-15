# Things Schema

# --- !Ups

CREATE SEQUENCE thing_id_seq;

CREATE TABLE thing (
    id integer NOT NULL DEFAULT nextval('thing_id_seq'),
    name varchar(64) not null,
    quantity int not null,
    created_on timestamp default CURRENT_TIMESTAMP,
    last_updated_on timestamp default CURRENT_TIMESTAMP,
    primary key (id)
);
-- CREATE TABLE thing (
--    id integer NOT NULL DEFAULT nextval('thing_id_seq'),
--    uuid varchar(36) not null,
--    name varchar(64) not null,
--    quantity int not null,
--    location varchar(64),
--    description varchar(1024),
--    created_on timestamp default CURRENT_TIMESTAMP,
--    last_updated_on timestamp default CURRENT_TIMESTAMP,
--    primary key (id)
--);

# --- !Downs

DROP TABLE thing;
DROP SEQUENCE thing_id_seq;
