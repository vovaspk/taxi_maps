INSERT INTO user (user_name, email, password, active)
VALUES
( 'Vova', 'Vova.S@gmail.com', '$2y$12$wZidw2kUu8/mRTclbc4SPeSEA2ffPAcACYdsDx6IxEaBkvGbvl6FC',true ),
( 'John', 'John.P@gmail.com', '$2y$12$JJCm7DE8XNkLQ2AU09ktuu9P.zQ/NluuVMqh/bB8XuomBSGHnDpAa', true);
-- pass 1, 2

INSERT INTO user_role(user_id, roles) VALUES (1, 'USER'),
INSERT INTO user_role(user_id, roles) VALUES (2, 'USER');