INSERT INTO users(username, password, name, surname, email, enabled)
VALUES ('test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Adam', 'Gadam', 'agadam@gmail.com', 1),
       ('test1', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Marcinek', 'Darek',
        'mdarek@gmail.com', 1),
       ('test2', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'Kubus', 'Marjusz',
        'kmarjusz@gmail.com', 1);
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
       (2, 3);