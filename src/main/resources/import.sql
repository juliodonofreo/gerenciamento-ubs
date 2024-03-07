INSERT INTO tb_user (nome, email, senha, usuario_tipo) VALUES ('Vitor', 'vitormatheusfv@gmail.com', '$2a$12$xkt3ljT21SVhrhfJSw.6nuqpLHLEuD0DTDFF8FCoLwGFJ8seFE3tO', 1);

INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_USER');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 2);