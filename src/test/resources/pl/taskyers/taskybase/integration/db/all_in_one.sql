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

INSERT INTO users(username, password, name, surname, email)
VALUES ('u1', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'U', 'S', 'u1@email.com');
INSERT INTO verification_tokens(user_id, token)
VALUES (1, 'tested-token');
INSERT INTO password_recovery_tokens(user_id, token)
VALUES (1, 'tested-token');
INSERT INTO users(username, password, name, surname, email, enabled)
VALUES ('enabled', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester', 'Good',
        'enabled@gmail.com', 1);
INSERT INTO users(username, password, name, surname, email, enabled)
VALUES ('notEnabled', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester', 'Bad',
        'notEnabled@gmail.com', 0);
INSERT INTO users(username, password, name, surname, email, enabled)
VALUES ('userWithoutProjects', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester', 'Good',
        'userWithoutProjects@gmail.com', 1),
       ('userWith4Projects', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester', 'Good',
        'userWith4Projects@gmail.com', 1),
       ('userWith5Projects', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester', 'Good',
        'userWith5Projects@gmail.com', 1),
       ('userWith9Projects', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester', 'Good',
        'userWith9Projects@gmail.com', 1);
INSERT INTO projects(owner_id, name, description, creation_date)
VALUES (1, 'test1', 'test1', '2019-11-05 15:00'),
       (1, 'test12', 'test12', '2019-11-05 15:10'),
       (1, 'test13', 'test13', '2019-11-05 15:15'),
       (1, 'test14', 'test14', '2019-11-05 15:20'),
       (1, 'test15', 'test15', '2019-11-05 15:25'),
       (1, 'test16', 'test16', '2019-11-05 15:30'),
       (1, 'test17', 'test17', '2019-11-05 15:35'),
       (1, 'test18', 'test18', '2019-11-05 15:40'),
       (1, 'test19', 'test19', '2019-11-05 15:45');
INSERT INTO project_user(project_id, user_id)
VALUES (1, 5),
       (2, 5),
       (3, 5),
       (4, 5),
       (1, 6),
       (2, 6),
       (3, 6),
       (4, 6),
       (5, 6),
       (1, 7),
       (2, 7),
       (3, 7),
       (4, 7),
       (5, 7),
       (6, 7),
       (7, 7),
       (8, 7),
       (9, 7);
INSERT INTO roles (`key`, description)
VALUES ('settings.manage.users', 'User is able to manage manage users in project settings'),
       ('settings.project.edit', 'User is able to edit project name and description'),
       ('settings.project.delete', 'User is able to delete project');