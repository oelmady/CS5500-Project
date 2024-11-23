INSERT INTO customer (id, name, email, password) VALUES
(1, 'John Doe', 'john.doe@example.com', 'password1'),
(2, 'Jane Smith', 'jane.smith@example.com', 'password2');

INSERT INTO vendor (id, name, email, password) VALUES
(1, 'Vendor One', 'vendor.one@example.com', 'password3'),
(2, 'Vendor Two', 'vendor.two@example.com', 'password4');

INSERT INTO promotions (id, vendor_id, description, discount) VALUES
(1, 1, '10% off on all items', 10),
(2, 2, 'Buy one get one free', 50);