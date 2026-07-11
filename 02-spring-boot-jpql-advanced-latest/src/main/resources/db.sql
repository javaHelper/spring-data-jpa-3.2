-- Products Table
CREATE TABLE products
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255),
    description VARCHAR(255),
    code        VARCHAR(255),
    category    VARCHAR(255),
    price       DECIMAL(19, 2),
    stock       INT,
    status      VARCHAR(255),
    supplier    VARCHAR(255)
);

-- Customers Table
CREATE TABLE customers
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    phone      VARCHAR(255),
    address    VARCHAR(255),
    city       VARCHAR(255),
    country    VARCHAR(255),
    status     VARCHAR(255),
    total_spent DOUBLE
);

-- Orders Table
CREATE TABLE orders
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id    BIGINT,
    order_date     DATE,
    status         VARCHAR(255),
    total_amount   DECIMAL(19, 2),
    payment_status VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);

-- Order Items Table
CREATE TABLE order_items
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT,
    product_id  BIGINT,
    quantity    INT,
    unit_price  DECIMAL(19, 2),
    total_price DECIMAL(19, 2),
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);
