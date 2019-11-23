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
CREATE TABLE IF NOT EXISTS project_invitation_tokens
(
    project_invitation_token_id int primary key auto_increment,
    user_id                     int not null references users (user_id),
    project_id                  int not null references projects (project_id),
    token                       varchar(200) unique
);
CREATE TABLE IF NOT EXISTS project_user
(
    project_id int,
    user_id    int,
    primary key (project_id, user_id),
    foreign key (project_id) references projects (project_id),
    foreign key (user_id) references users (user_id)
);
CREATE TABLE IF NOT EXISTS roles
(
    role_id     bigint auto_increment primary key,
    `key`       varchar(255) unique,
    description varchar(255) null
);
CREATE TABLE IF NOT EXISTS role_linkers
(
    role_linker_id bigint auto_increment primary key,
    checked        bit    null,
    project_id     bigint null references projects (project_id),
    role_id        bigint null references roles (role_id),
    user_id        bigint null references users (user_id)
);
CREATE TABLE IF NOT EXISTS status_entries
(
    status_entry_id  bigint auto_increment primary key,
    background_color varchar(255) not null,
    text_color       varchar(255) not null,
    value            varchar(255) not null,
    project_id       bigint       not null references projects (project_id)
);