DROP INDEX IF EXISTS userIDs_users;
DROP INDEX IF EXISTS names_users;
DROP INDEX IF EXISTS passwords_users;
DROP INDEX IF EXISTS type_users;

DROP INDEX IF EXISTS managerID_store;
DROP INDEX IF EXISTS storeID_store;

DROP INDEX IF EXISTS storeIDs_product;
DROP INDEX IF EXISTS productName_product;
DROP INDEX IF EXISTS numberOfUnits_product;
DROP INDEX IF EXISTS pricePerUnit_product;

DROP INDEX IF EXISTS warehouseID_warehouse;

DROP INDEX IF EXISTS customerID_order;
DROP INDEX IF EXISTS productName_order;
DROP INDEX IF EXISTS storeID_order;
DROP INDEX IF EXISTS orderNumber_order;

DROP INDEX IF EXISTS productName_supplyRequest;
DROP INDEX IF EXISTS storeID_supplyRequest;
DROP INDEX IF EXISTS warehouseID_supplyRequest;
DROP INDEX IF EXISTS managerID_supplyRequest;
DROP INDEX IF EXISTS requestNumber_suplyRequest;

DROP INDEX IF EXISTS updateNumber_productUpdates;
DROP INDEX IF EXISTS managerID_productUpdates;
DROP INDEX IF EXISTS storeID_productUpdates;
DROP INDEX IF EXISTS productName_productUpdates;


CREATE INDEX userIDs_users
ON Users
USING btree (UserID);
CREATE INDEX names_users
ON Users
USING btree (name);
CREATE INDEX passwords_user
ON Users
USING btree(password);
CREATE INDEX type_users
ON Users
USING btree(type);

-----------------------------------
-- Store indexes
CREATE INDEX storeID_store
ON Store
USING btree(storeID);
CREATE INDEX managerID_store
ON Store
USING btree(managerID);

-----------------------------------
-- Product indexes
CREATE INDEX storeIDs_product
ON Product
USING btree(storeID);
CREATE INDEX productName_product
ON Product
USING btree(productName);
CREATE INDEX numberOfUnits_product
ON Product
USING btree(numberOfUnits);
CREATE INDEX pricePerUnit_product
ON Product
USING btree(pricePerUnit);
----------------------------------
-- Product updates indexes
CREATE INDEX updateNumber_productUpdates
ON ProductUpdates
USING btree(updateNumber);
CREATE INDEX managerID_productUpdates
ON ProductUpdates
USING btree(managerID);
CREATE INDEX storeID_productUpdates
ON ProductUpdates
USING btree(storeID);
CREATE INDEX productName_productUpdates
ON ProductUpdates
USING btree(productName);

------------------------------------
-- Order indexes 
CREATE INDEX storeID_order
ON Orders
USING btree(storeID);
CREATE INDEX productName_order
ON Orders
USING btree(productName);
CREATE INDEX customerID_order
ON Orders
USING btree(customerID);
CREATE INDEX orderNumber_order
ON Orders
USING btree(orderNumber);


-----------------------------------
-- Warehouse indexes
CREATE INDEX warehouseID_warehouse
ON Warehouse 
USING btree(WarehouseID);



-----------------------------------
-- Supply request indexes
CREATE INDEX productName_supplyRequest
ON ProductSupplyRequests
using btree(productName);
CREATE INDEX storeID_supplyRequest
ON ProductSupplyRequests
using btree(storeID);
CREATE INDEX warehouseID_supplyRequest
ON ProductSupplyRequests
using btree(warehouseID);
CREATE INDEX managerID_supplyRequest
ON ProductSupplyRequests
using btree(managerID);
CREATE INDEX requestNumber_supplyRequest
ON ProductSupplyRequests
using btree(requestNumber);

