DROP DATABASE IF EXISTS tasky_integration;
CREATE DATABASE IF NOT EXISTS tasky_integration;
CREATE TABLE IF NOT EXISTS users
(
    user_id  int primary key auto_increment,
    username varchar(200) unique,
    password varchar(60),
    email    varchar(200) unique,
    name     varchar(200),
    surname  varchar(200)
);
INSERT INTO users(username, password, name, surname, email)
VALUES ("u1", "p1", "n1", "s1", "u1@email.com");