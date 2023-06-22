CREATE TABLE journal.performance
(
    id    bigserial primary key,
    grade varchar(20)
);

CREATE TABLE journal.student
(
    id             BIGSERIAL PRIMARY KEY,
    full_name      VARCHAR(50) NOT NULL,
    date_of_birth  DATE        NOT NULL,
    performance_id INT,
    FOREIGN KEY (performance_id) REFERENCES journal.performance (id)
);

ALTER SEQUENCE journal.performance_id_seq RESTART 2;

INSERT INTO journal.performance (grade)
VALUES ('неуд'),
       ('уд'),
       ('хор'),
       ('отл');
INSERT INTO journal.student (full_name, date_of_birth, performance_id)
VALUES ('Иванов Иван Иванович', '12.06.2010', 5),
       ('Петров Петр Петрович', '06.12.2010', 3);