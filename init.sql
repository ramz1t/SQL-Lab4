-- Active: 1747658630254@@127.0.0.1@3306

USE `lab4`;

CREATE TABLE `employee` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `address` VARCHAR(255),
    `city` VARCHAR(255),
    `first_name` VARCHAR(255),
    `last_name` VARCHAR(255),
    `postal_code` VARCHAR(255),
    PRIMARY KEY (`id`)
);

CREATE TABLE `customer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `address` VARCHAR(255),
    `birth_date` DATE,
    `city` VARCHAR(255),
    `first_name` VARCHAR(255),
    `last_name` VARCHAR(255),
    `postal_code` VARCHAR(255),
    PRIMARY KEY (`id`)
);

CREATE TABLE `furniture` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `color` VARCHAR(255),
    `comment` VARCHAR(255),
    `name` VARCHAR(255),
    `price` DOUBLE,
    `purchase_date` DATE,
    `shelf_nbr` INT,
    `weight` DOUBLE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `order_head` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_date` DATE,
    `customer_id` BIGINT,
    `employee_id` BIGINT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
    FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
);

CREATE TABLE `order_line` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `furniture_id` BIGINT,
    `order_id` BIGINT,
    `quantity` INT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`furniture_id`) REFERENCES `furniture` (`id`),
    FOREIGN KEY (`order_id`) REFERENCES `order_head` (`id`)
);

INSERT INTO
    `employee` (
        `id`,
        `address`,
        `city`,
        `first_name`,
        `last_name`,
        `postal_code`
    )
VALUES (
        1,
        'Vägen 123',
        'Stockholm',
        'Erik',
        'Johansson',
        '111 22'
    ),
    (
        2,
        'Stigen 456',
        'Göteborg',
        'Anna',
        'Svensson',
        '222 33'
    ),
    (
        3,
        'Allén 789',
        'Malmö',
        'Lars',
        'Nilsson',
        '333 44'
    );

INSERT INTO
    `customer` (
        `id`,
        `address`,
        `birth_date`,
        `city`,
        `first_name`,
        `last_name`,
        `postal_code`
    )
VALUES (
        1,
        'Granvägen 10',
        '1990-06-01',
        'Uppsala',
        'Maria',
        'Andersson',
        '444 55'
    ),
    (
        2,
        'Björkgatan 23',
        '1982-04-15',
        'Västerås',
        'Johan',
        'Larsson',
        '555 66'
    ),
    (
        3,
        'Kungsgatan 5',
        '1975-12-09',
        'Örebro',
        'Emma',
        'Karlsson',
        '666 77'
    );

INSERT INTO
    `furniture` (
        `id`,
        `color`,
        `comment`,
        `name`,
        `price`,
        `purchase_date`,
        `shelf_nbr`,
        `weight`
    )
VALUES (
        1,
        'Blå',
        'En klassisk stol',
        'Stol',
        299.99,
        '2024-01-01',
        1,
        7.5
    ),
    (
        2,
        'Vit',
        'Bekväm och rymlig',
        'Soffa',
        4999.50,
        '2024-01-02',
        2,
        35.2
    ),
    (
        3,
        'Brun',
        'Ekträ',
        'Bord',
        1299.99,
        '2024-01-03',
        3,
        15.7
    );

INSERT INTO
    `order_head` (
        `id`,
        `order_date`,
        `customer_id`,
        `employee_id`
    )
VALUES (1, '2024-04-10', 1, 1),
    (2, '2024-04-11', 2, 2),
    (3, '2024-04-12', 3, 3);

INSERT INTO
    `order_line` (
        `id`,
        `furniture_id`,
        `order_id`,
        `quantity`
    )
VALUES (1, 1, 1, 4),
    (2, 2, 2, 1),
    (3, 3, 3, 2),
    (4, 2, 1, 1),
    (5, 1, 2, 2),
    (6, 3, 2, 1),
    (7, 1, 3, 3),
    (8, 3, 1, 2),
    (9, 2, 3, 1);