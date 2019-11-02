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
    project_id  int primary key auto_increment,
    owner_id    int not null references users (user_id),
    name        varchar(40) unique,
    description varchar(50)
);
CREATE TABLE IF NOT EXISTS project_user
(
    project_id int,
    user_id    int,
    primary key (project_id, user_id),
    foreign key (project_id) references projects (project_id),
    foreign key (user_id) references users (user_id)
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
       ('userWith6Projects', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester', 'Good',
        'userWith6Projects@gmail.com', 1);
INSERT INTO projects(owner_id, name, description)
VALUES (1, 'test1', 'test1'),
       (1, 'test12', 'test12'),
       (1, 'test13', 'test13'),
       (1, 'test14', 'test14'),
       (1, 'test15', 'test15'),
       (1, 'test16', 'test16');
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
       (6, 7);