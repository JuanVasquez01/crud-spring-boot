-- Usuarios (contraseña: password)
INSERT INTO usuario (username, password, role, correo) VALUES ('admin', '$2a$10$rPiEAgQNIT1TCoKi.Iy9wuuqI1wKg9Hs0/YyZcIoR.hCIT6JVbQdS', 'ADMIN', 'admin@coffee.com');
INSERT INTO usuario (username, password, role, correo) VALUES ('user', '$2a$10$rPiEAgQNIT1TCoKi.Iy9wuuqI1wKg9Hs0/YyZcIoR.hCIT6JVbQdS', 'USER', 'user@coffee.com');

-- Perfiles de tostado
INSERT INTO roast_profiles (name, max_temp, min_temp, time_range) VALUES ('Alto', 230.0, 190.0, 15);
INSERT INTO roast_profiles (name, max_temp, min_temp, time_range) VALUES ('Medio', 210.0, 180.0, 12);
INSERT INTO roast_profiles (name, max_temp, min_temp, time_range) VALUES ('Bajo', 190.0, 160.0, 10);

-- Ciclos de tostado
INSERT INTO roast_cycles (cliente_id, usuario_tostador_id, profile_id, coffee_amount, descripcion, start_time, end_time, status) 
VALUES (2, 1, 1, 1000, 'Tostado de prueba alto', '2023-01-01 10:00:00', '2023-01-01 10:15:00', 'COMPLETED');

INSERT INTO roast_cycles (cliente_id, usuario_tostador_id, profile_id, coffee_amount, descripcion, start_time, status) 
VALUES (2, 1, 2, 1500, 'Tostado de prueba medio', '2023-01-02 10:00:00', 'IN_PROGRESS');

-- Curvas de tostado
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 190.0, 1.0, '2023-01-01 10:01:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 200.0, 1.1, '2023-01-01 10:02:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 210.0, 1.2, '2023-01-01 10:03:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 220.0, 1.3, '2023-01-01 10:04:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 230.0, 1.4, '2023-01-01 10:05:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 225.0, 1.3, '2023-01-01 10:06:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 220.0, 1.2, '2023-01-01 10:07:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 215.0, 1.1, '2023-01-01 10:08:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 210.0, 1.0, '2023-01-01 10:09:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (1, 205.0, 0.9, '2023-01-01 10:10:00');

INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (2, 180.0, 1.0, '2023-01-02 10:01:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (2, 185.0, 1.1, '2023-01-02 10:02:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (2, 190.0, 1.2, '2023-01-02 10:03:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (2, 195.0, 1.3, '2023-01-02 10:04:00');
INSERT INTO curvas_tostado (ciclo_id, temperatura, presion, timestamp) VALUES (2, 200.0, 1.4, '2023-01-02 10:05:00');