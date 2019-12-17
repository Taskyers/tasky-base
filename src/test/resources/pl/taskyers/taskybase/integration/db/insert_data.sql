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
        'userWithNoPermissionToDeleteProject@gmail.com', 1),
       ('userWith1Project', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWith1Project@gmail.com', 1),
       ('userWithSeveralProjects', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWithSeveralProjects@gmail.com', 1),
       ('userWithRolesToBeUpdated', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWithRolesToBeUpdated@gmail.com', 1),
       ('userWithRolesToBeUpdated1', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWithRolesToBeUpdated1@gmail.com', 1),
       ('userWithRolesToBeUpdated2', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWithRolesToBeUpdated2@gmail.com', 1),
       ('userWithRolesToBeUpdated3', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Tester',
        'Good',
        'userWithRolesToBeUpdated3@gmail.com', 1);
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
       (1, 'projectToBeDeleted', 'projectToBeDeleted', '2019-11-12 15:45'),
       (1, 'test10', 'test10', '2019-11-05 15:55'),
       (1, 'toBeUpdated', 'xd', '2019-11-05 16:55');
INSERT INTO project_user(project_id, user_id)
VALUES (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (1, 5),
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
       (1, 9),
       (11, 10),
       (1, 11),
       (2, 11),
       (3, 11),
       (4, 11),
       (5, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15);
INSERT INTO roles (`key`, description)
VALUES ('settings.manage.users', 'User is able to manage manage users in project settings'),
       ('settings.edit.project', 'User is able to edit project name and description'),
       ('settings.delete.project', 'User is able to delete project'),
       ('project.invite.others', 'User is able to invite others to project'),
       ('settings.manage.statuses', 'User is able to manage task statuses'),
       ('settings.manage.types', 'User is able to manage task types'),
       ('settings.manage.priorities', 'User is able to manage task priorities'),
       ('settings.manage.sprints', 'User is able to manage sprints');
INSERT INTO role_linkers (user_id, project_id, role_id, checked)
VALUES (1, 1, 1, true),
       (1, 1, 2, true),
       (1, 1, 3, true),
       (1, 1, 4, true),
       (1, 1, 6, true),
       (1, 1, 7, true),
       (1, 1, 8, true),
       (1, 8, 2, false),
       (1, 9, 3, false),
       (1, 10, 3, true),
       (1, 12, 2, true),
       (1, 1, 5, true),
       (2, 1, 4, false),
       (8, 1, 2, false),
       (8, 1, 3, true),
       (9, 1, 2, true),
       (9, 1, 3, false),
       (1, 2, 1, true),
       (1, 11, 1, true),
       (1, 5, 1, true),
       (12, 1, 1, true),
       (12, 1, 2, true),
       (12, 1, 3, true),
       (12, 1, 4, true),
       (13, 1, 1, true),
       (13, 1, 2, true),
       (13, 1, 3, true),
       (13, 1, 4, true),
       (14, 1, 1, true),
       (14, 1, 2, true),
       (14, 1, 3, true),
       (14, 1, 4, true),
       (15, 1, 1, false),
       (15, 1, 2, false),
       (15, 1, 3, false),
       (15, 1, 4, false),
       (5, 1, 1, true);
INSERT INTO entry_entities (background_color, text_color, value, project_id, entry_type)
VALUES ('green', 'white', 'test1', 1, 'STATUS'),
       ('green', 'white', 'test2', 1, 'PRIORITY'),
       ('green', 'white', 'test3', 1, 'TYPE'),
       ('green', 'white', 'entryToBeUpdated', 1, 'PRIORITY'),
       ('green', 'white', 'entryToBeUpdated1', 1, 'STATUS'),
       ('green', 'white', 'entryToBeDeleted', 1, 'STATUS'),
       ('green', 'white', 'test4', 2, 'PRIORITY'),
       ('green', 'white', 'test5', 2, 'STATUS'),
       ('green', 'white', 'test6', 2, 'TYPE');
INSERT INTO sprints(project_id, end, name, start)
VALUES (1, '2019-11-05', 'test', '2019-11-10'),
       (1, '2019-11-05', 'test1', '2019-11-05'),
       (1, '2019-11-05', 'test2', '2019-11-05'),
       (1, '2019-11-05', 'toBeUpdated', '2019-11-05'),
       (1, '2019-11-05', 'toBeDeleted', '2019-11-05'),
       (1, CURRENT_DATE, 'current', CURRENT_DATE - INTERVAL 2 DAY),
       (2, '2019-11-05', 'test', '2019-11-05'),
       (2, CURRENT_DATE, 'current', CURRENT_DATE - INTERVAL 2 DAY);
INSERT INTO tasks(creation_date, description, fix_version, `key`, name, assignee_id,
                  creator_id, priority_id, project_id, sprint_id, status_id, type_id)
VALUES ('2019-12-13 15:00', 'Test 1', '1.0', 'PROJECT-1', 'Testing tasks1', 1, 1, 1, 1, 1, 1, 1),
       ('2019-12-13 15:10', 'Test 2', '1.0', 'PROJECT-2', 'Testing tasks2', 1, 1, 1, 1, 1, 1, 1),
       ('2019-12-13 15:20', 'Test 3', '1.0', 'PROJECT-3', 'Testing tasks3', 1, 1, 1, 1, 1, 1, 1),
       ('2019-12-13 15:20', 'Test 3', '1.0', 'PROJECT-3', 'Testing tasks1', 1, 1, 1, 2, 1, 1, 1);