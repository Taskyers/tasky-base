INSERT INTO users(username, password, name, surname, email, enabled)
VALUES ('test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Adam', 'Gadam', 'agadam@gmail.com', 1),
       ('test1', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Marcinek', 'Darek',
        'mdarek@gmail.com', 1),
       ('test2', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Kubus', 'Marjusz',
        'kmarjusz@gmail.com', 1),
       ('test3', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Darjusz', 'Marjusz',
        'dmarjusz@gmail.com', 1),
       ('test4', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Marek', 'Jarek',
        'mjarek@gmail.com', 1);
INSERT INTO projects(owner_id, name, description, creation_date)
VALUES (1, 'project', 'project', '2019-11-05 15:00'),
       (1, 'project2', 'project2', '2019-11-05 15:10'),
       (1, 'project3', 'project3', '2019-11-05 15:15'),
       (1, 'project4', 'project4', '2019-11-05 15:20'),
       (1, 'project5', 'project5', '2019-11-05 15:25'),
       (1, 'project6', 'project6', '2019-11-05 15:30'),
       (1, 'project7', 'project7', '2019-11-05 15:35'),
       (1, 'project8', 'project8', '2019-11-05 15:40'),
       (1, 'project9', 'project9', '2019-11-05 15:45');
INSERT INTO project_user(user_id, project_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (2, 1),
       (2, 2),
       (2, 3),
       (4, 1),
       (5, 1);
INSERT INTO roles (`key`, description)
VALUES ('settings.manage.users', 'User is able to manage manage users in project settings'),
       ('settings.edit.project', 'User is able to edit project name and description'),
       ('settings.delete.project', 'User is able to delete project'),
       ('project.invite.others', 'User is able to invite others to project'),
       ('settings.manage.statuses', 'User is able to manage task statuses'),
       ('settings.manage.types', 'User is able to manage task types'),
       ('settings.manage.priorities', 'User is able to manage task priorities'),
       ('settings.manage.sprints', 'User is able to manage sprints');
INSERT INTO entry_entities (background_color, text_color, value, project_id, entry_type)
VALUES ('#34eb52', '#ebe834', 'INITIAL', 1, 'STATUS'),
       ('#34eb52', '#474738', 'IN PROGRESS', 1, 'STATUS'),
       ('#34eb52', '#4e5a82', 'CODE REVIEW', 1, 'STATUS'),
       ('#084aff', '#ededed', 'TASK', 1, 'TYPE'),
       ('#e61b05', '#ededed', 'BUG', 1, 'TYPE'),
       ('#34eb52', '#ededed', 'ENHANCEMENT', 1, 'TYPE'),
       ('#ebe834', '#34eb52', 'MINOR', 1, 'PRIORITY'),
       ('#34eb52', '#ebe834', 'MAJOR', 1, 'PRIORITY'),
       ('#e61b05', '#ededed', 'CRITICAL', 1, 'PRIORITY');
INSERT INTO role_linkers (user_id, project_id, role_id, checked)
VALUES (1, 1, 1, true),
       (1, 1, 2, true),
       (1, 1, 3, true),
       (1, 1, 4, true),
       (1, 1, 5, true),
       (1, 1, 6, true),
       (1, 1, 7, true),
       (1, 1, 8, true),
       (1, 2, 1, true),
       (1, 2, 2, true),
       (1, 2, 3, true),
       (1, 3, 1, true),
       (1, 3, 2, true),
       (1, 3, 3, true),
       (1, 4, 1, true),
       (1, 4, 2, true),
       (1, 4, 3, true),
       (1, 5, 1, true),
       (1, 5, 2, true),
       (1, 5, 3, true),
       (1, 6, 1, true),
       (1, 6, 2, true),
       (1, 6, 3, true),
       (1, 7, 1, true),
       (1, 7, 2, true),
       (1, 7, 3, true),
       (1, 8, 1, true),
       (1, 8, 2, true),
       (1, 8, 3, true),
       (1, 9, 1, true),
       (1, 9, 2, true),
       (1, 9, 3, true),
       (2, 1, 1, false),
       (2, 1, 2, false),
       (2, 1, 3, false),
       (2, 2, 1, false),
       (2, 2, 2, false),
       (2, 2, 3, false),
       (2, 3, 1, false),
       (2, 3, 2, false),
       (2, 3, 3, false),
       (4, 1, 2, true),
       (5, 1, 3, true);
INSERT INTO sprints(project_id, name, start, end)
VALUES (1, 'sprint1', '2019-11-27', '2019-11-30'),
       (1, 'sprint2', '2019-12-03', '2019-12-30'),
       (1, 'sprint3', '2020-01-01', '2019-01-05');
INSERT INTO tasks(creation_date, update_date, description, fix_version, `key`, name, assignee_id,
                  creator_id, priority_id, project_id, sprint_id, status_id, type_id)
VALUES ('2019-12-13 15:00', '2019-12-13 15:00', 'Test 1', '1.0', 'PROJECT-1', 'Testing tasks1', 1, 1, 7, 1, 1, 1, 4),
       ('2019-12-13 15:10', '2019-12-13 15:00', 'Test 2', '1.0', 'PROJECT-2', 'Testing tasks2', 1, 1, 8, 1, 1, 2, 5),
       ('2019-12-13 15:20', '2019-12-13 15:00', 'Test 3', '1.0', 'PROJECT-3', 'Testing tasks3', 1, 1, 9, 1, 1, 3, 6);
INSERT INTO comments(content, user_id, task_id, creation_date)
VALUES ('Testing comments 1', 1, 1, '2019-12-13 16:00'),
       ('Testing comments 2', 1, 1, '2019-12-13 16:10'),
       ('Testing comments 3', 1, 1, '2019-12-13 16:20');