INSERT INTO gift_certificate (name, description, price, create_date, last_update_date, duration)
VALUES ('Certificate', 'Description', 1, '2021-10-08 11:11:11.100001', '2021-01-01 01:11:11.100001', 1),
       ('certificate 2', 'description 2', 2, '2021-10-08 11:11:11.100001', '2021-01-01 01:22:11.100001', 2),
       ('certificate 3', 'description 3', 3, '2021-10-08 11:11:11.100001', '2021-01-01 01:22:11.100001', 3);

INSERT INTO tag ("NAME") VALUES ('IT'), ('HR'), ('Java'), ('Epam');

INSERT INTO tag_certificate (certificate_id, tag_id) VALUES (1, 1), (1, 3), (2, 2);