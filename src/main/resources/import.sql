INSERT INTO USER (USER_ID, PASSWORD, NAME, EMAIL) VALUES ('javajigi', 'test', '재성', 'javajigi@slipp.net');
INSERT INTO USER (USER_ID, PASSWORD, NAME, EMAIL) VALUES ('sanjigi', 'test', '산지기', 'sanjigi@slipp.net');

INSERT INTO QUESTION (id, writer_id, title, contents, create_date) VALUES(1, 1, '국내에서 Ruby on Rails와 Play가 ', 'Ruby on Rails(이하 Ror)는', CURRENT_TIMESTAMP());
INSERT INTO QUESTION (id, writer_id, title, contents, create_date) VALUES(2, 1, '위승준의 글 ', '돌쇠라는 군생활중의 별명', CURRENT_TIMESTAMP());