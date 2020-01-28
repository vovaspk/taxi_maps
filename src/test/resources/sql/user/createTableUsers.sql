CREATE TABLE IF NOT EXISTS user (
   id INT NOT NULL AUTO_INCREMENT primary key,
   user_name VARCHAR(50) NOT NULL,
   email VARCHAR(20) NOT NULL,
   password VARCHAR(60),
   active BIT
);

CREATE TABLE IF NOT EXISTS user_role (
   user_id INT NOT NULL primary key,
   roles VARCHAR(50) NOT NULL

);
alter table user_role
  add constraint user_role_user_fk
  foreign key (user_id) references user (id);

INSERT INTO user (id, user_name, email, password, active)
VALUES
(1, 'vova', 'vova', '$2a$08$SEG3LpMLrSmNJ7lDHp9Yo.RqP6XOziBjQhlup74ZrIMxfkS58E7AW', true ),
(2, 'John', 'John.P@gmail.com', '2', true);
-- pass 1, 2
-- INSERT INTO user_role(user_id, roles) VALUES (1, 'USER');
-- INSERT INTO user_role(user_id, roles) VALUES (2, 'USER');