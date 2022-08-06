CREATE TABLE drone_model
(
    id               bigint NOT NULL,
    name             VARCHAR(255),
    max_weight_limit bigint,
    PRIMARY KEY (id)
) engine=InnoDB;

INSERT INTO drone_model (id, name, max_weight_limit) VALUES (1, 'LIGHT_WEIGHT', 150);
INSERT INTO drone_model (id, name, max_weight_limit) VALUES (2, 'MIDDLE_WEIGHT', 250);
INSERT INTO drone_model (id, name, max_weight_limit) VALUES (3, 'CRUISER_WEIGHT', 350);
INSERT INTO drone_model (id, name, max_weight_limit) VALUES (4, 'HEAVY_WEIGHT', 500);
