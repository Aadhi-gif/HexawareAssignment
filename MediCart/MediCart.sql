CREATE DATABASE MediCartDB;

USE MediCartDB;

CREATE TABLE Customer (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Address VARCHAR(200)
);

CREATE TABLE Warehouse (
    WarehouseID INT AUTO_INCREMENT PRIMARY KEY,
    WarehouseName VARCHAR(100) NOT NULL,
    City VARCHAR(50) NOT NULL
);

CREATE TABLE Medicine (
    MedicineID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    RequiresPrescription BOOLEAN DEFAULT FALSE,
    Price DECIMAL(10,2) NOT NULL,
    ExpiryDate DATE NOT NULL,

    CONSTRAINT chk_medicine_price
        CHECK (Price >= 0)
);

CREATE TABLE Stock (
    StockID INT AUTO_INCREMENT PRIMARY KEY,

    MedicineID INT NOT NULL,
    WarehouseID INT NOT NULL,

    QuantityAvailable INT NOT NULL DEFAULT 0,

    CONSTRAINT chk_stock_quantity
        CHECK (QuantityAvailable >= 0),

    CONSTRAINT fk_stock_medicine
        FOREIGN KEY (MedicineID)
        REFERENCES Medicine(MedicineID),

    CONSTRAINT fk_stock_warehouse
        FOREIGN KEY (WarehouseID)
        REFERENCES Warehouse(WarehouseID),

    CONSTRAINT uq_medicine_warehouse
        UNIQUE (MedicineID, WarehouseID)
);

CREATE TABLE Prescription (
    PrescriptionID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT NOT NULL,
    DocumentPath VARCHAR(200) NOT NULL,
    VerifiedBy INT NULL,
    VerifiedOn DATE NULL,
    Status VARCHAR(20) DEFAULT 'Pending',
    UploadedOn DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_prescription_status
        CHECK (Status IN ('Pending', 'Approved', 'Rejected')),

    CONSTRAINT fk_prescription_customer
        FOREIGN KEY (CustomerID)
        REFERENCES Customer(CustomerID)
);

CREATE TABLE Orders (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT NOT NULL,
    OrderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    PrescriptionID INT NULL,
    Status VARCHAR(20) DEFAULT 'Placed',
    TotalAmount DECIMAL(10,2) DEFAULT 0,

    CONSTRAINT chk_order_status
        CHECK (
            Status IN (
                'Placed',
                'On Hold',
                'Processing',
                'Shipped',
                'Delivered',
                'Cancelled',
                'Rejected'
            )
        ),

    CONSTRAINT fk_order_customer
        FOREIGN KEY (CustomerID)
        REFERENCES Customer(CustomerID),

    CONSTRAINT fk_order_prescription
        FOREIGN KEY (PrescriptionID)
        REFERENCES Prescription(PrescriptionID)
);

CREATE TABLE OrderItem (
    OrderItemID INT AUTO_INCREMENT PRIMARY KEY,
    OrderID INT NOT NULL,
    MedicineID INT NOT NULL,
    WarehouseID INT NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10,2) NOT NULL,

    CONSTRAINT chk_order_item_quantity
        CHECK (Quantity > 0),

    CONSTRAINT chk_order_item_price
        CHECK (UnitPrice >= 0),

    CONSTRAINT fk_orderitem_order
        FOREIGN KEY (OrderID)
        REFERENCES Orders(OrderID),

    CONSTRAINT fk_orderitem_medicine
        FOREIGN KEY (MedicineID)
        REFERENCES Medicine(MedicineID),

    CONSTRAINT fk_orderitem_warehouse
        FOREIGN KEY (WarehouseID)
        REFERENCES Warehouse(WarehouseID)
);

INSERT INTO Customer
(FirstName, LastName, Email, Address)
VALUES
('Arun', 'Kumar', 'arun@gmail.com', 'Chennai'),
('Priya', 'Sharma', 'priya@gmail.com', 'Bengaluru'),
('Rahul', 'Verma', 'rahul@gmail.com', 'Hyderabad'),
('Divya', 'Raj', 'divya@gmail.com', 'Coimbatore'),
('Karthik', 'Mohan', 'karthik@gmail.com', 'Chennai'),
('Sneha', 'Reddy', 'sneha@gmail.com', 'Bengaluru'),
('Vijay', 'Krishnan', 'vijay@gmail.com', 'Madurai'),
('Ananya', 'Patel', 'ananya@gmail.com', 'Hyderabad'),
('Manoj', 'Singh', 'manoj@gmail.com', 'Pune'),
('Meera', 'Nair', 'meera@gmail.com', 'Kochi');

INSERT INTO Warehouse
(WarehouseName, City)
VALUES
('Chennai Central Warehouse', 'Chennai'),
('Bengaluru Warehouse', 'Bengaluru'),
('Hyderabad Warehouse', 'Hyderabad'),
('Coimbatore Warehouse', 'Coimbatore'),
('Madurai Warehouse', 'Madurai'),
('Pune Warehouse', 'Pune'),
('Mumbai Warehouse', 'Mumbai'),
('Delhi Warehouse', 'Delhi'),
('Kochi Warehouse', 'Kochi'),
('Trichy Warehouse', 'Trichy');

INSERT INTO Medicine
(Name, RequiresPrescription, Price, ExpiryDate)
VALUES
('Paracetamol', FALSE, 25.00, '2027-12-31'),
('Cetirizine', FALSE, 40.00, '2027-10-30'),
('Vitamin C', FALSE, 80.00, '2028-06-30'),
('ORS Sachet', FALSE, 20.00, '2027-09-30'),
('Cough Syrup', FALSE, 110.00, '2027-11-30'),
('Antibiotic A', TRUE, 150.00, '2027-08-31'),
('Prescription Medicine B', TRUE, 220.00, '2027-12-15'),
('Pain Relief Tablet', FALSE, 65.00, '2028-01-31'),
('Allergy Medicine', TRUE, 95.00, '2027-07-31'),
('Eye Drops', FALSE, 70.00, '2028-03-31');

INSERT INTO Stock
(MedicineID, WarehouseID, QuantityAvailable)
SELECT
    m.MedicineID,
    w.WarehouseID,
    100 + (m.MedicineID * 10) + w.WarehouseID
FROM Medicine m
CROSS JOIN Warehouse w;

INSERT INTO Prescription
(CustomerID, DocumentPath, VerifiedBy, VerifiedOn, Status)
VALUES
(1, 'docs/prescription_1.pdf', 101, CURDATE(), 'Approved'),
(2, 'docs/prescription_2.pdf', 102, CURDATE(), 'Pending'),
(3, 'docs/prescription_3.pdf', 103, CURDATE(), 'Approved'),
(4, 'docs/prescription_4.pdf', NULL, NULL, 'Pending'),
(5, 'docs/prescription_5.pdf', 104, CURDATE(), 'Rejected'),
(6, 'docs/prescription_6.pdf', 105, CURDATE(), 'Approved'),
(7, 'docs/prescription_7.pdf', NULL, NULL, 'Pending'),
(8, 'docs/prescription_8.pdf', 106, CURDATE(), 'Approved'),
(9, 'docs/prescription_9.pdf', NULL, NULL, 'Pending'),
(10, 'docs/prescription_10.pdf', 107, CURDATE(), 'Rejected');

INSERT INTO Orders
(CustomerID, OrderDate, PrescriptionID, Status, TotalAmount)
VALUES
(1, NOW(), 1, 'Delivered', 0),
(2, NOW(), 2, 'On Hold', 0),
(3, NOW(), 3, 'Processing', 0),
(4, NOW(), 4, 'On Hold', 0),
(5, NOW(), 5, 'Rejected', 0),
(6, NOW(), 6, 'Delivered', 0),
(7, NOW(), 7, 'Placed', 0),
(8, NOW(), 8, 'Processing', 0),
(9, NOW(), 9, 'Placed', 0),
(10, NOW(), 10, 'Rejected', 0);

INSERT INTO OrderItem
(OrderID, MedicineID, WarehouseID, Quantity, UnitPrice)
SELECT
    o.OrderID,
    m.MedicineID,
    w.WarehouseID,
    ((o.OrderID + m.MedicineID) % 5) + 1,
    m.Price
FROM Orders o
CROSS JOIN Medicine m
CROSS JOIN Warehouse w
WHERE w.WarehouseID IN (1, 2);

SELECT * FROM Customer;

SELECT * FROM Medicine;

SELECT * FROM Warehouse;

SELECT * FROM Prescription;

SELECT * FROM Orders;

SELECT * FROM OrderItem;

