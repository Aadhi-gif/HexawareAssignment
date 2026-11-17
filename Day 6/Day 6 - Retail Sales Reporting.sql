/* Task 1 */

create database day6;
use day6;

CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(50),
    city VARCHAR(50),
    email VARCHAR(100)
);

INSERT INTO customers VALUES
(1, 'Arun', 'Chennai', 'arun@gmail.com'),
(2, 'Bala', 'Coimbatore', 'bala@gmail.com'),
(3, 'Charan', 'Chennai', 'charan@gmail.com'),
(4, 'Divya', 'Madurai', 'divya@gmail.com'),
(5, 'Ezhil', 'Salem', 'ezhil@gmail.com'),
(6, 'Farah', 'Trichy', 'farah@gmail.com');

CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    category VARCHAR(50),
    price DECIMAL(10,2)
);

INSERT INTO products VALUES
(101, 'Laptop', 'Electronics', 50000),
(102, 'Mobile', 'Electronics', 20000),
(103, 'Chair', 'Furniture', 3000),
(104, 'Table', 'Furniture', 5000),
(105, 'Shoes', 'Fashion', 2000);

CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

INSERT INTO orders VALUES
(201, 1, '2026-01-10'),
(202, 1, '2026-01-15'),
(203, 1, '2026-02-05'),
(204, 2, '2026-02-10'),
(205, 2, '2026-03-12'),
(206, 3, '2026-03-15'),
(207, 4, '2026-03-15');
INSERT INTO orders VALUES
(208, 1, '2026-03-20');

CREATE TABLE order_items (
    item_id INT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

INSERT INTO order_items VALUES
(1, 201, 101, 1, 50000),
(2, 201, 105, 2, 2000),
(3, 202, 102, 1, 20000),
(4, 202, 103, 2, 3000),
(5, 203, 101, 1, 50000),
(6, 204, 104, 2, 5000),
(7, 205, 102, 2, 20000),
(8, 206, 105, 3, 2000),
(9, 207, 103, 1, 3000),
(10, 207, 104, 1, 5000);

CREATE TABLE payments (
    payment_id INT PRIMARY KEY,
    order_id INT,
    payment_date DATE,
    amount DECIMAL(10,2),
    payment_status VARCHAR(20),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

INSERT INTO payments VALUES
(1, 201, '2026-01-10', 54000, 'Paid'),
(2, 202, '2026-01-15', 26000, 'Paid'),
(3, 204, '2026-02-10', 10000, 'Paid'),
(4, 205, '2026-03-12', 40000, 'Paid'),
(5, 206, '2026-03-15', 6000, 'Paid');

/*Query 1 - top 5 products by revenue */

SELECT p.product_name,SUM(oi.quantity * oi.unit_price) AS revenue FROM products p
JOIN order_items oi ON p.product_id = oi.product_id
GROUP BY p.product_id, p.product_name
ORDER BY revenue DESC
LIMIT 5;

/* Query 2 - Month-wise sales*/

SELECT DATE_FORMAT(o.order_date, '%Y-%m') AS sales_month,SUM(oi.quantity * oi.unit_price) AS monthly_sales FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
GROUP BY DATE_FORMAT(o.order_date, '%Y-%m')
ORDER BY sales_month;

/*Query 3 - Customers with more than three orders */

SELECT c.customer_name, COUNT(o.order_id) AS total_orders
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 3;

/*Query 4 - Average basket value per city*/

SELECT city, ROUND(AVG(order_value), 2) AS avg_basket_value
FROM (
    SELECT c.city, o.order_id,
           SUM(oi.quantity * oi.unit_price) AS order_value
    FROM customers c
    JOIN orders o ON c.customer_id = o.customer_id
    JOIN order_items oi ON o.order_id = oi.order_id
    GROUP BY c.city, o.order_id
) AS temp
GROUP BY city;

/*Query 5 - Orders without payment (LEFT JOIN)*/

SELECT o.order_id,o.customer_id,o.order_date
FROM orders o
LEFT JOIN payments p ON o.order_id = p.order_id
WHERE p.payment_id IS NULL;

/*Query 6 - Customers with no orders*/

SELECT c.customer_id,c.customer_name,c.city
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;

/*Query 7 - Product-category revenue share*/

SELECT p.category,SUM(oi.quantity * oi.unit_price) AS category_revenue,ROUND(SUM(oi.quantity * oi.unit_price) * 100 /(SELECT SUM(quantity * unit_price) FROM order_items), 2) AS revenue_percentage
FROM products p
JOIN order_items oi ON p	.product_id = oi.product_id
GROUP BY p.category
ORDER BY category_revenue DESC;

/*Query 8 - Find the day with the highest sales*/

SELECT DATE(o.order_date) AS sales_day,SUM(oi.quantity * oi.unit_price) AS daily_sales
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
GROUP BY DATE(o.order_date)
ORDER BY daily_sales DESC
LIMIT 1;

/*Task 2 */
/* Upper*/

SELECT
    UPPER(customer_name) AS customer_name
FROM customers;

/* Concat */

SELECT
    CONCAT('Customer: ', customer_name) AS customer_details
FROM customers;

/*DateDiff*/

SELECT
    order_id,
    order_date,
    DATEDIFF(CURDATE(), order_date) AS days_since_order
FROM orders;

/*DateFormat */

SELECT
    order_id,
    DATE_FORMAT(order_date, '%d-%M-%Y') AS formatted_date
FROM orders;

/*Round*/

SELECT
    product_id,
    ROUND(quantity * unit_price, 2) AS total_price
FROM order_items;

/* Task 3 */

SELECT
    COUNT(*) AS total_rows,
    COUNT(amount) AS non_null_payments,
    SUM(amount) AS total_payment,
    AVG(amount) AS average_payment
FROM payments;

/* Task 4 */

/*where */

SELECT * FROM orders
WHERE order_date >= '2026-01-01';

/* Having */

SELECT customer_id,COUNT(order_id) AS total_orders
FROM orders
GROUP BY customer_id
HAVING COUNT(order_id) > 3;

SELECT customer_id,COUNT(order_id) AS total_orders
FROM orders WHERE order_date >= '2026-01-01'
GROUP BY customer_id
HAVING COUNT(order_id) > 3;