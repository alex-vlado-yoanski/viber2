-- Примерни потребители

-- id=2
INSERT INTO users(name, password)
	VALUES ('kiril', 'parolata');

-- id=3
INSERT INTO users(name, password)
	VALUES ('stoqnka', 'burkan');

-- Примерна покана (приета)
-- Проверка с SELECT user_invitations.id,status,create_date,u1.name,u2.name FROM user_invitations JOIN users u1 ON user_invitations.sender_id = u1.id JOIN users u2 ON user_invitations.receiver_id = u2.id ORDER BY user_invitations.id;

INSERT INTO user_invitations(status, sender_id, receiver_id, create_date)
	VALUES(2, 2, 3, '2021-11-10 02:32:35');

-- Примерни съобщения
-- Проверка с SELECT messages.id,text,file_path,create_date,users.name FROM messages JOIN users ON messages.sent_by=users.id ORDER BY messages.id;

INSERT INTO messages(text, file_path, create_date, sent_by)
	VALUES('zdr kp', '', '2021-11-11 12:00:00', 2) ;
INSERT INTO chats(message_id) VALUES (1);

INSERT INTO messages(text, file_path, create_date, sent_by)
	VALUES('n6t ti', '', '2021-11-11 12:01:32', 3);
INSERT INTO chats(message_id) VALUES (2);

INSERT INTO messages(text, file_path, create_date, sent_by)
	VALUES('nz qm bob', '', '2021-11-11 12:01:58', 2);
INSERT INTO chats(message_id) VALUES (3);

INSERT INTO messages(text, file_path, create_date, sent_by)
	VALUES ('4ek mlk', '', '2021-11-11 12:03:02', 2);
INSERT INTO chats(message_id) VALUES (4);

-- Примерна група
-- Проверка с SELECT user_chats.id,users.name,messages.text FROM user_chats JOIN users ON user_id = users.id JOIN messages ON chat_id = messages.id;

INSERT INTO user_chats(user_id, chat_id)
	VALUES (2, 1);

INSERT INTO user_chats(user_id, chat_id)
	VALUES (3, 2);

INSERT INTO user_chats(user_id, chat_id)
	VALUES (2, 3);

INSERT INTO user_chats(user_id, chat_id)
	VALUES (2, 4);
