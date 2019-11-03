INSERT INTO users(username, password, name, surname, email, enabled)
VALUES ('test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Adam', 'Gadam', 'agadam@gmail.com', 1);
INSERT INTO projects(owner_id, name, description)
VALUES (1, 'project', 'project'),
       (1, 'project2', 'project2'),
       (1, 'project3', 'project3'),
       (1, 'project4', 'project4'),
       (1, 'project5', 'project5'),
       (1, 'project6', 'project6');
INSERT INTO project_user(user_id, project_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6);