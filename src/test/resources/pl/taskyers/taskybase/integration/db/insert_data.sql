INSERT INTO users(username, password, name, surname, email)
VALUES ('u1', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'U', 'S', 'u1@email.com');
INSERT INTO verification_tokens(user_id, token)
VALUES (1, 'tested-token');
INSERT INTO password_recovery_tokens(user_id, token)
VALUES (1, 'tested-token');
INSERT INTO project_invitation_tokens(user_id, project_id, token)
VALUES (1, 1, 'tested-token');
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
        'userWith9Projects@gmail.com', 1),
       ('userWithNoPermissionToUpdateProject', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWithNoPermissionToUpdateProject@gmail.com', 1),
       ('userWithNoPermissionToDeleteProject', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWithNoPermissionToDeleteProject@gmail.com', 1);
INSERT INTO projects(owner_id, name, description, creation_date)
VALUES (1, 'test1', 'test1', '2019-11-05 15:00'),
       (1, 'test12', 'test12', '2019-11-05 15:10'),
       (1, 'test13', 'test13', '2019-11-05 15:15'),
       (1, 'test14', 'test14', '2019-11-05 15:20'),
       (1, 'test15', 'test15', '2019-11-05 15:25'),
       (1, 'test16', 'test16', '2019-11-05 15:30'),
       (1, 'test17', 'test17', '2019-11-05 15:35'),
       (1, 'test18', 'test18', '2019-11-05 15:40'),
       (1, 'test19', 'test19', '2019-11-05 15:45'),
       (1, 'projectToBeDeleted', 'projectToBeDeleted', '2019-11-12 15:45');
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
       (9, 7),
       (1, 8),
       (1, 9);
INSERT INTO roles (`key`, description)
VALUES ('settings.manage.users', 'User is able to manage manage users in project settings'),
       ('settings.edit.project', 'User is able to edit project name and description'),
       ('settings.delete.project', 'User is able to delete project'),
       ('project.invite.others', 'User is able to invite others to project');
INSERT INTO role_linkers (user_id, project_id, role_id, checked)
VALUES (1, 1, 1, true),
       (1, 1, 2, true),
       (1, 1, 3, true),
       (1, 1, 4, true),
       (1, 8, 2, false),
       (1, 9, 3, false),
       (1, 10, 3, true);