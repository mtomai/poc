drop table if exists USERS;
create table USERS (id serial, name varchar(255), last_name varchar(255), age integer, gender character, date_of_birth varchar(10));

drop table if exists PERMISSION;
create table PERMISSION (id serial,username varchar(255), password varchar(255), role varchar(255));

drop table if exists OPERATIONS;
create table OPERATIONS (role varchar(255), operation varchar(255));
