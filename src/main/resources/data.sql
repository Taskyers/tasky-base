INSERT INTO users(username, password, name, surname, email, enabled)
VALUES ('mruszkiewicz', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Marcin', 'Ruszkiewicz',
        'mruszkiewicz@gmail.com', 1),
       ('mrzeppa', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Mariusz', 'Rzeppa',
        'mrzeppa@gmail.com', 1),
       ('jsildatk', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Jakub', 'Sildatk',
        'jsildatk@gmail.com', 1);
INSERT INTO projects(owner_id, name, description, creation_date)
VALUES (1, 'tasky-base', 'Spring boot rest server for Tasky application: https://github.com/Taskyers/tasky-base',
        '2019-10-05 15:00'),
       (2, 'tasky-ui', 'Angular frontend server for Tasky application: https://github.com/Taskyers/tasky-ui',
        '2019-10-05 15:10'),
       (3, 'tasky-docs',
        'Project containing all not code related files for Tasky application (documentation, formatters, etc.): https://github.com/Taskyers/tasky-docs',
        '2019-10-05 15:15');
INSERT INTO project_user(user_id, project_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 2),
       (3, 3);
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
VALUES ('#C0C0C0', '#ffffff', 'INITIAL', 1, 'STATUS'), /* 1 */
       ('#d2dc1d', '#000000', 'IN PROGRESS', 1, 'STATUS'), /* 2 */
       ('#d2dc1d', '#000000', 'CODE REVIEW', 1, 'STATUS'), /* 3 */
       ('#d2dc1d', '#000000', 'TEST', 1, 'STATUS'), /* 4 */
       ('#00d836', '#ffffff', 'CLOSED', 1, 'STATUS'), /* 5 */
       ('#e61b05', '#d8eb1f', 'REOPENED', 1, 'STATUS'), /* 6 */
       ('#4c7ff9', '#ffffff', 'TASK', 1, 'TYPE'), /* 7 */
       ('#e61b05', '#ffffff', 'BUG', 1, 'TYPE'), /* 8 */
       ('#34eb52', '#ffffff', 'MINOR', 1, 'PRIORITY'), /* 9 */
       ('#e06051', '#ebe834', 'MAJOR', 1, 'PRIORITY'), /* 10 */
       ('#f91a00', '#ededed', 'CRITICAL', 1, 'PRIORITY'), /* 11 */
    /* ------------------------- */
       ('#C0C0C0', '#ffffff', 'INITIAL', 2, 'STATUS'), /* 12 */
       ('#d2dc1d', '#000000', 'IN PROGRESS', 2, 'STATUS'), /* 13 */
       ('#d2dc1d', '#000000', 'CODE REVIEW', 2, 'STATUS'), /* 14 */
       ('#d2dc1d', '#000000', 'TEST', 2, 'STATUS'), /* 15 */
       ('#00d836', '#ffffff', 'CLOSED', 2, 'STATUS'), /* 16 */
       ('#e61b05', '#d8eb1f', 'REOPENED', 2, 'STATUS'), /* 17 */
       ('#4c7ff9', '#ffffff', 'TASK', 2, 'TYPE'), /* 18 */
       ('#e61b05', '#ffffff', 'BUG', 2, 'TYPE'), /* 19 */
       ('#2ce7eb', '#f70505', 'ICON REQUEST', 2, 'TYPE'), /* 20 */
       ('#debb08', '#013e70', 'GUI MOCKUP', 2, 'TYPE'), /* 21 */
       ('#34eb52', '#ffffff', 'MINOR', 2, 'PRIORITY'), /* 22 */
       ('#e06051', '#ebe834', 'MAJOR', 2, 'PRIORITY'), /* 23 */
       ('#f91a00', '#ededed', 'CRITICAL', 2, 'PRIORITY'), /* 24 */
    /* ------------------------- */
       ('#C0C0C0', '#ffffff', 'INITIAL', 3, 'STATUS'), /* 25 */
       ('#d2dc1d', '#000000', 'IN PROGRESS', 3, 'STATUS'), /* 26 */
       ('#d2dc1d', '#000000', 'REVIEW', 3, 'STATUS'), /* 27 */
       ('#00d836', '#ffffff', 'CLOSED', 3, 'STATUS'), /* 28 */
       ('#e61b05', '#d8eb1f', 'REOPENED', 3, 'STATUS'), /* 29 */
       ('#4c7ff9', '#ffffff', 'TECHNICAL DOCUMENTATION', 3, 'TYPE'), /* 30 */
       ('#4c7ff9', '#ffffff', 'RELEASE NOTES', 3, 'TYPE'), /* 31 */
       ('#4c7ff9', '#ffffff', 'FORMATTER', 3, 'TYPE'), /* 32 */
       ('#a4ff54', '#ffffff', 'TRIVIAL', 3, 'PRIORITY'), /* 33 */
       ('#34eb52', '#ffffff', 'MINOR', 3, 'PRIORITY'), /* 34 */
       ('#e06051', '#ebe834', 'MAJOR', 3, 'PRIORITY'), /* 35 */
       ('#f91a00', '#ededed', 'CRITICAL', 3, 'PRIORITY'); /* 36 */
INSERT INTO role_linkers (user_id, project_id, role_id, checked)
VALUES (1, 1, 1, true),
       (1, 1, 2, true),
       (1, 1, 3, true),
       (1, 1, 4, true),
       (1, 1, 5, true),
       (1, 1, 6, true),
       (1, 1, 7, true),
       (1, 1, 8, true),
       (2, 1, 1, false),
       (2, 1, 2, false),
       (2, 1, 3, false),
       (2, 1, 4, true),
       (2, 1, 5, true),
       (2, 1, 6, true),
       (2, 1, 7, true),
       (2, 1, 8, false),
       (3, 1, 1, false),
       (3, 1, 2, false),
       (3, 1, 3, false),
       (3, 1, 4, true),
       (3, 1, 5, true),
       (3, 1, 6, true),
       (3, 1, 7, true),
       (3, 1, 8, true),
    /* ------------------------- */
       (1, 2, 1, false),
       (1, 2, 2, false),
       (1, 2, 3, false),
       (1, 2, 4, true),
       (1, 2, 5, true),
       (1, 2, 6, true),
       (1, 2, 7, true),
       (1, 2, 8, false),
       (2, 2, 1, true),
       (2, 2, 2, true),
       (2, 2, 3, true),
       (2, 2, 4, true),
       (2, 2, 5, true),
       (2, 2, 6, true),
       (2, 2, 7, true),
       (2, 2, 8, true),
       (3, 2, 1, false),
       (3, 2, 2, false),
       (3, 2, 3, false),
       (3, 2, 4, true),
       (3, 2, 5, true),
       (3, 2, 6, true),
       (3, 2, 7, true),
       (3, 2, 8, true),
    /* ------------------------- */
       (1, 3, 1, false),
       (1, 3, 2, false),
       (1, 3, 3, false),
       (1, 3, 4, true),
       (1, 3, 5, true),
       (1, 3, 6, true),
       (1, 3, 7, true),
       (1, 3, 8, false),
       (2, 3, 1, false),
       (2, 3, 2, false),
       (2, 3, 3, false),
       (2, 3, 4, true),
       (2, 3, 5, true),
       (2, 3, 6, true),
       (2, 3, 7, true),
       (2, 3, 8, true),
       (3, 3, 1, true),
       (3, 3, 2, true),
       (3, 3, 3, true),
       (3, 3, 4, true),
       (3, 3, 5, true),
       (3, 3, 6, true),
       (3, 3, 7, true),
       (3, 3, 8, true);
INSERT INTO sprints(project_id, name, start, end)
VALUES (1, 'Tasky base september 2019', '2019-09-01', '2019-09-30'), /* 1 */
       (1, 'Tasky base october 2019', '2019-10-01', '2019-10-30'), /* 2 */
       (1, 'Tasky base november 2019', '2019-11-01', '2019-11-30'), /* 3 */
       (1, 'Tasky base december 2019', '2019-12-01', '2019-12-31'), /* 4 */
       (1, 'Tasky base january 2020', '2020-01-02', '2020-01-29'), /* 5 */
       (2, 'Tasky ui november 2019', '2019-11-03', '2019-11-26'), /* 6 */
       (2, 'Tasky ui december 2019', '2019-12-02', '2019-12-22'), /* 7 */
       (2, 'Tasky ui january 2020', '2020-01-10', '2020-01-25'); /* 8 */
/* (1, 'Tasky base january 2020', CURRENT_DATE - INTERVAL 2 DAY, CURRENT_DATE); */
INSERT INTO tasks(creation_date, update_date, description, fix_version, `key`, name, assignee_id,
                  creator_id, priority_id, project_id, sprint_id, status_id, type_id, resolution)
VALUES ('2019-01-10 12:23', '2019-01-13 15:00',
        'After clicking `Assign to me` button nothing happens and you can see exception in logs', '1.0.1',
        'TASKY-BASE-23421', 'Assign to me throws NPE', 1, 2, 11, 1, 5, 2,
        8, 'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00',
        'Create task without sprint and it will redirect you on the details page. There will be empty content and you can see exception in logs',
        '1.0.1',
        'TASKY-BASE-121',
        'Error on task details page when task has no sprint',
        null, 1, 11, 1, 5, 1,
        8, 'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00',
        'There is `NO_ACTION_REQUIRED` instead of `No action required`',
        '1.0.1',
        'TASKY-BASE-1231',
        'Wrong display of resolution types',
        null, 3, 9, 1, 5, 1,
        8, 'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00',
        'New resolution type: `Duplicated` needs to be added',
        '1.0.1',
        'TASKY-BASE-21',
        'New resolution type',
        null, 1, 10, 1, 5, 1,
        7, 'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 1', '1.0.1', 'TASKY-BASE-1111', 'Fill 1', 3, 1, 10, 1, 5, 3, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 2', '1.0.1', 'TASKY-BASE-1211', 'Fill 2', 3, 1, 10, 1, 5, 4, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 3', '1.0.1', 'TASKY-BASE-1121', 'Fill 3', 1, 1, 11, 1, 5, 5, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 4', '1.0.1', 'TASKY-BASE-114562', 'Fill 4', 1, 1, 10, 1, 5, 6, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 5', '1.0.1', 'TASKY-BASE-18761', 'Fill 5', 3, 1, 9, 1, 5, 4, 8,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 6', '1.0.1', 'TASKY-BASE-1911', 'Fill 6', 3, 1, 11, 1, 5, 4, 8,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 7', '1.0.1', 'TASKY-BASE-14621', 'Fill 7', 1, 1, 11, 1, 5, 5, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 8', '1.0.1', 'TASKY-BASE-1342', 'Fill 8', 1, 1, 9, 1, 5, 6, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 9', '1.0.1', 'TASKY-BASE-111', 'Fill 9', 3, 1, 9, 1, 5, 1, 8,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 10', '1.0.1', 'TASKY-BASE-211', 'Fill 10', 3, 1, 10, 1, 5, 2, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 11', '1.0.1', 'TASKY-BASE-621', 'Fill 11', 1, 1, 9, 1, 5, 4, 7,
        'UNRESOLVED'),
       ('2019-01-11 11:23', '2019-01-16 15:00', 'Fill 12', '1.0.1', 'TASKY-BASE-1102', 'Fill 12', 3, 1, 10, 1, 5, 6, 8,
        'UNRESOLVED'),
       ('2019-01-03 07:32', '2019-01-06 13:11', '', '1.0.1', 'TASKY-UI-2332',
        'Mockup for sprint board view', 2, 3, 23, 2, 8, 13,
        21, 'UNRESOLVED'),
       ('2019-01-18 15:20', '2019-01-21 14:35', '', '1.0', 'TASKY-DOCS-13443', 'DOCUMENTATION FOR TASKY', 3, 3, 33, 3,
        null, 28,
        30, 'DONE');
INSERT INTO comments(content, user_id, task_id, creation_date)
VALUES ('Analysis: when assignee is not set, application wants to compare it with other user entity.', 1, 1,
        '2019-01-13 15:00'),
       ('Marcin, please estimate how long time will it take. It is very important.', 2, 1, '2019-01-13 15:39'),
       ('It should take 1 md.', 1, 1, '2019-01-13 16:11'),
       ('Documentation has been added', 3, 18, '2019-01-21 14:35');
INSERT INTO task_user(task_id, user_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 2),
       (2, 3),
       (3, 3);