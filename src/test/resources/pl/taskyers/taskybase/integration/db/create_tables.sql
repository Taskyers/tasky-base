CREATE TABLE IF NOT EXISTS users
(
    user_id  int primary key auto_increment,
    username varchar(200) unique ,
    password binary(60),
    email    varchar(200) unique,
    name     varchar(200),
    surname  varchar(200)
);