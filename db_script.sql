
-- Подготовка за изпълнение

DROP TABLE IF EXISTS user_chats;
DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS user_invitations;
DROP TABLE IF EXISTS users;

-- Създаваме на таблица 'users'

CREATE TABLE users(
    id        	INT GENERATED ALWAYS AS IDENTITY,
    name       	VARCHAR(50) NOT NULL,
	password	VARCHAR(97) NOT NULL,
	
	CONSTRAINT pk_users PRIMARY KEY(id)
);

-- Създаваме на таблица 'user_invitations'

CREATE TABLE user_invitations(
    id        		INT GENERATED ALWAYS AS IDENTITY,
    status       	INT NOT NULL,
	sender_id		INT NOT NULL,
	receiver_id		INT NOT NULL,
	create_date		TIMESTAMP NOT NULL,
	
	CONSTRAINT pk_user_invitations PRIMARY KEY(id),
	CONSTRAINT fk_user_invitations_sender_id FOREIGN KEY(sender_id) REFERENCES users(id),
	CONSTRAINT fk_user_invitations_receiver_id FOREIGN KEY(receiver_id) REFERENCES users(id)
);

-- Създаваме на таблица 'messages'

CREATE TABLE messages(
    id        		INT GENERATED ALWAYS AS IDENTITY,
    text     		VARCHAR(256) NOT NULL,
	file_path		VARCHAR(260) NOT NULL,
	create_date		TIMESTAMP NOT NULL,
	sent_by			INT NOT NULL,
	
	CONSTRAINT pk_messages PRIMARY KEY(id),
	CONSTRAINT fk_messages_sent_by FOREIGN KEY(sent_by) REFERENCES users(id)
);

-- Създаваме на таблица 'chats'

CREATE TABLE chats(
    id        	INT GENERATED ALWAYS AS IDENTITY,
    message_id  INT NOT NULL,
	
	CONSTRAINT pk_chats PRIMARY KEY(id),
	CONSTRAINT fk_chats_message_id FOREIGN KEY(message_id) REFERENCES messages(id)
);

-- Създаваме на таблица 'user_chats'

CREATE TABLE user_chats(
    id        	INT GENERATED ALWAYS AS IDENTITY,
    user_id     INT NOT NULL,
	chat_id		INT NOT NULL,
	
	CONSTRAINT pk_user_chats PRIMARY KEY(id),
	CONSTRAINT fk_user_chats_user_id FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT fk_user_chats_chat_id FOREIGN KEY(chat_id) REFERENCES chats(id)
);

-- Инициализация на таблицa 'users'

INSERT INTO users(name, password)
VALUES('SA', 'common')
