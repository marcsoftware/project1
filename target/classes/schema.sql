DROP DATABASE IF EXISTS project1;
CREATE DATABASE project1;
\c project1;

CREATE TABLE user_accounts(
   user_id serial PRIMARY KEY,
   username VARCHAR (50) UNIQUE NOT NULL,
   password VARCHAR (50) NOT NULL,
   realname varchar(100),
   email varchar(100),
   rank varchar(20)
  
);

insert into user_accounts (username,password,rank,realname,email) values('admin','97e1e59c0375e0f55c10d4314db20466','admin','martin washington','martin@email.com');
insert into user_accounts (username,password,rank,realname,email) values('manager','97e1e59c0375e0f55c10d4314db20466','admin','connie coolidge','connie@email');
insert into user_accounts (username,password,rank,realname,email) values('bob','97e1e59c0375e0f55c10d4314db20466','admin','bob jefferson','bob@gmail.com');
insert into user_accounts (username,password,rank,realname,email) values('marc','97e1e59c0375e0f55c10d4314db20466','employee','marc melcher','marc@gmail.com');
insert into user_accounts (username,password,rank,realname,email) values('quintin','97e1e59c0375e0f55c10d4314db20466','employee','quintin newman','quintin@yahoo.com');
insert into user_accounts (username,password,rank,realname,email) values('randy','97e1e59c0375e0f55c10d4314db20466','employee','randy marvel','randy@gmail.com');
CREATE TABLE requests(
   id serial,
   username VARCHAR (50) ,
   amount VARCHAR (50) ,
   comment text ,
   image VARCHAR (50) ,
   status VARCHAR (50),
   manager VARCHAR (50),
    FOREIGN KEY (username) REFERENCES user_accounts(username)
);



