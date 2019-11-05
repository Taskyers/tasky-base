CREATE TABLE IF NOT EXISTS users
(
    user_id  int primary key auto_increment,
    username varchar(200) unique,
    password binary(60) not null,
    email    varchar(200) unique,
    name     varchar(200),
    surname  varchar(200),
    enabled  boolean default false
);
CREATE TABLE IF NOT EXISTS verification_tokens
(
    verification_token_id int primary key auto_increment,
    user_id               int not null references users (user_id),
    token                 varchar(200) unique
);
CREATE TABLE IF NOT EXISTS password_recovery_tokens
(
    password_recovery_token_id int primary key auto_increment,
    user_id                    int not null references users (user_id),
    token                      varchar(200) unique
);
CREATE TABLE IF NOT EXISTS projects
(
    project_id    int primary key auto_increment,
    owner_id      int      not null references users (user_id),
    name          varchar(40) unique,
    description   varchar(50),
    creation_date datetime not null
);
CREATE TABLE IF NOT EXISTS project_user
(
    project_id int,
    user_id    int,
    primary key (project_id, user_id),
    foreign key (project_id) references projects (project_id),
    foreign key (user_id) references users (user_id)
);