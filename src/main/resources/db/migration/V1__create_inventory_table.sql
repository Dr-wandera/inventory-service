CREATE TABLE inventory_table (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    seller_id VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL UNIQUE,
    product_name VARCHAR(255) NOT NULL,
    stock INT NOT NULL DEFAULT 0
);

