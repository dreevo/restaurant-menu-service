DROP TABLE IF EXISTS food;
CREATE TABLE food
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    description varchar(255)          NOT NULL,
    ref         varchar(255) UNIQUE   NOT NULL,
    price       float8                NOT NULL,
    version     integer               NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_date timestamp NOT NULL
);