-- !Ups
create table tasks
(
 id serial not null,
 name varchar(100) not null,
 description varchar(100),
 PRIMARY KEY (id)
);

-- !Downs
DROP TABLE IF EXISTS tasks