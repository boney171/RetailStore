COPY Users
FROM 'users.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE users_userID_seq RESTART 101;

COPY Store
FROM 'stores.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Product
FROM 'products.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Warehouse
FROM 'warehouse.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Orders
FROM 'orders.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE orders_orderNumber_seq RESTART 501;


COPY ProductSupplyRequests
FROM 'productSupplyRequests.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productsupplyrequests_requestNumber_seq RESTART 11;

COPY ProductUpdates
FROM 'productUpdates.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productupdates_updateNumber_seq RESTART 51;
