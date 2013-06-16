# Things Schema

# --- !Ups

CREATE SEQUENCE thing_id_seq;

CREATE TABLE thing (
    id integer NOT NULL DEFAULT nextval('thing_id_seq'),
    name varchar(64) not null,
    quantity int not null,
    description varchar(1024),
    location varchar(64),
    last_seen_on timestamp,
    created_on timestamp default CURRENT_TIMESTAMP,
    last_updated_on timestamp default CURRENT_TIMESTAMP,
    primary key (id)
);

# --- !Downs

DROP TABLE thing;
DROP SEQUENCE thing_id_seq;
