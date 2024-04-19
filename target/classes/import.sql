INSERT INTO TB_USER(name, email, cpf,password, birth_date, user_type) VALUES ('alex green', 'alex@gmail.com', '1234567899', '$2a$10$NJjgkAd1krsANVEAs7qf.eyXZtgXEFfuixeBNPNZ29C9vdQwMPahK', TIMESTAMP WITH TIME ZONE '1990-01-01', 1)
INSERT INTO TB_USER(name, email, password, specialization, user_type) VALUES ('maria', 'maria@gmail.com', '$2a$10$NJjgkAd1krsANVEAs7qf.eyXZtgXEFfuixeBNPNZ29C9vdQwMPahK', 0, 2)

INSERT INTO TB_APPOINTMENT(date, diagnosis, doctor_id, patient_id) VALUES (TIMESTAMP WITH TIME ZONE '2021-01-01', 'Febre amarela', 1, 2);

