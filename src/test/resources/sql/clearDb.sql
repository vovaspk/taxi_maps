-- SET REFERENTIAL_INTEGRITY FALSE;
-- TRUNCATE TABLE users;
-- TRUNCATE TABLE user_role;
ALTER table user_role drop constraint user_role_user_fk;
drop table users;
drop table user_role;
-- DELETE FROM user WHERE id = 1;
-- DELETE FROM user WHERE id = 2;
-- DELETE FROM user_role WHERE user_id = 1;
-- DELETE FROM user_role WHERE user_id = 2;