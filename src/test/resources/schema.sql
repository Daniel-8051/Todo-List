-- Set schema for h2 db
CREATE SCHEMA IF NOT EXISTS spring_db;
SET SCHEMA spring_db;

-- Create todo_item table for testing
CREATE TABLE todo_item
(
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title varchar(100),
    details varchar(250),
    deadline date
);