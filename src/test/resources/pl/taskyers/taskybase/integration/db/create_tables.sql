CREATE TABLE IF NOT EXISTS users
(
    user_id  int primary key auto_increment,
    username varchar(200) unique ,
    password binary(60) not null,
    email    varchar(200) unique,
    name     varchar(200),
    surname  varchar(200),
    enabled boolean default false
);
CREATE TABLE IF NOT EXISTS verification_tokens
(
    verification_token_id int primary key auto_increment,
    user_id int not null references users(user_id),
    token varchar(200) unique
);
CREATE TABLE IF NOT EXISTS password_recovery_tokens
(
    password_recovery_token_id int primary key auto_increment,
    user_id int not null references users(user_id),
    token varchar(200) unique
);