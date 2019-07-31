DROP DATABASE IF EXISTS project1;
CREATE DATABASE project1;
\c project1;

CREATE TABLE user_accounts(
   user_id serial PRIMARY KEY,
   username VARCHAR (50) UNIQUE NOT NULL,
   password VARCHAR (50) NOT NULL,
   status varchar(20)
  
);

CREATE TABLE requests(
   id serial,
   username VARCHAR (50) ,
   amount VARCHAR (50) ,
   comment text ,
   image VARCHAR (50) ,
   status VARCHAR (50),
    FOREIGN KEY (username) REFERENCES user_accounts(username)
);
