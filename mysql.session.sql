
-- USE TaskManager;

-- CREATE TABLE Users (
--     UserID INT PRIMARY KEY AUTO_INCREMENT,
--     Username VARCHAR(50) NOT NULL,
--     Password VARCHAR(50) NOT NULL
-- );

-- CREATE TABLE Tasks (
--     TaskID INT PRIMARY KEY AUTO_INCREMENT,
--     UserID INT,
--     TaskName VARCHAR(100) NOT NULL,
--     Description TEXT,
--     DueDate DATE,
--     Priority ENUM('Low', 'Medium', 'High'),
--     Status ENUM('Pending', 'In Progress', 'Completed'),
--     FOREIGN KEY (UserID) REFERENCES Users(UserID)
-- -- );
-- insert into Users (Username , Password) values ("tebibu","tebibu123");

-- select * from Users

-- ALTER TABLE Users
--    ADD COLUMN SecurityQuestion VARCHAR(400),
--    ADD COLUMN SecurityAnswer VARCHAR(400);

-- ALTER TABLE Users
--   ADD COLUMN FullName VARCHAR(255);

--  ALTER TABLE Users
--    ADD CONSTRAINT Username_unique UNIQUE (Username);

-- select * from Users;

-- DESCRIBE Users;

-- ALTER TABLE Users
-- MODIFY COLUMN  SecurityQuestion text NOT NULL,
-- MODIFY COLUMN  SecurityAnswer  text NOT NULL,
-- MODIFY COLUMN FullName VARCHAR(255) NOT NULL;

-- select * from Users

-- -- select * from Tasks;
-- CREATE TABLE Project (
--     ProjectName VARCHAR(255) PRIMARY KEY  ,
--     ProjectID INT UNIQUE AUTO_INCREMENT
-- );

-- ALTER TABLE Tasks
-- ADD COLUMN ProjectName VARCHAR(255),
-- ADD CONSTRAINT FK_ProjectName
-- FOREIGN KEY (ProjectName) REFERENCES Project(ProjectName);

-- select * from Tasks;

-- -- 
-- ALTER TABLE Tasks
-- ADD COLUMN Time VARCHAR(255);

-- ALTER TABLE Project
--  ADD COLUMN UserID int,
--  ADD CONSTRAINT FK_UserID
--  FOREIGN KEY (UserID) REFERENCES Users(UserID);

-- select * from Tasks;


