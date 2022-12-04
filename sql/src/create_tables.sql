DROP TABLE IF EXISTS Store CASCADE;
DROP TABLE IF EXISTS Product CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Warehouse CASCADE;
DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS ProductSupplyRequests CASCADE;
DROP TABLE IF EXISTS ProductUpdates CASCADE;

CREATE TABLE Users ( userID serial,
                     name char(50) NOT NULL,
                     password char(11) NOT NULL,    
					 latitude decimal(8,6) NOT NULL,
                     longitude decimal(9,6) NOT NULL,
                     type char(10) NOT NULL,  -- type can be 'customer', 'manager', 'admin' 
                     PRIMARY KEY(userID)
);


CREATE TABLE Store ( storeID integer, 
                     name char(30) NOT NULL,
                     latitude decimal(8, 6) NOT NULL,
                     longitude decimal(9, 6) NOT NULL,
                     managerID integer NOT NULL,
					 dateEstablished date,
		             PRIMARY KEY(storeID), 
                     FOREIGN KEY(managerID) REFERENCES Users(userID)
);

CREATE TABLE Product ( storeID integer NOT NULL, 
                       productName char(30) NOT NULL,
                       numberOfUnits integer NOT NULL,
                       pricePerUnit float NOT NULL,
                       PRIMARY KEY(storeID, productName), 
                       FOREIGN KEY(storeID) REFERENCES Store(storeID)
		               ON DELETE CASCADE
);

CREATE TABLE Warehouse ( WarehouseID integer,
                         area integer,
                         latitude decimal(8,6) NOT NULL,
                         longitude decimal(9,6)  NOT NULL,
                         PRIMARY KEY(WarehouseID));

CREATE TABLE Orders ( 
					 orderNumber serial NOT NULL,       
					 customerID integer NOT NULL,
                     storeID integer NOT NULL,
                     productName char(30) NOT NULL, 
                     unitsOrdered integer NOT NULL, 
                     orderTime timestamp NOT NULL,
                     PRIMARY KEY(orderNumber),
                     FOREIGN KEY(customerID) REFERENCES Users(userID),
                     FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName)
);

CREATE TABLE ProductSupplyRequests (  
							   requestNumber serial NOT NULL,
							   managerID integer NOT NULL,    --User ID of the Manager who makes the supply request
							   warehouseID integer NOT NULL,
                               storeID integer NOT NULL,
                               productName char(30) NOT NULL, 
							   unitsRequested integer NOT NULL,
                               PRIMARY KEY(requestNumber),
							   FOREIGN KEY(managerID) REFERENCES Users(userID), 
                               FOREIGN KEY(warehouseID) REFERENCES Warehouse(warehouseID),
                               FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName)
);

CREATE TABLE ProductUpdates (
	 						updateNumber serial,	
							managerID integer NOT NULL,
                        	storeID integer NOT NULL,
                       		productName char(30) NOT NULL, 
                            updatedOn timestamp NOT NULL,
                        	PRIMARY KEY(updateNumber),
                        	FOREIGN KEY(managerID) REFERENCES Users(userID),
                        	FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName)
);



CREATE OR REPLACE FUNCTION calculate_distance(lat1 decimal, long1 decimal, lat2 decimal, long2 decimal)
RETURNS decimal AS $dist$
BEGIN RETURN sqrt((lat1 - lat2) * (lat1 - lat2) + (long1 - long2) * (long1 - long2));
END;
$dist$ LANGUAGE plpgsql;

/* Trigger part */
/*

DROP SEQUENCE IF EXISTS order_num;
CREATE SEQUENCE order_num START WITH 501;

DROP SEQUENCE IF EXISTS update_num;
CREATE SEQUENCE update_num START WITH 51;

DROP SEQUENCE IF EXISTS user_num;
CREATE SEQUENCE user_num START WITH 101;

CREATE OR REPLACE LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_order_number()
RETURNS "trigger" AS
$BODY$
BEGIN
	NEW.orderNumber = nextval('order_num');
	return NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION set_update_number()
RETURNS "trigger" AS
$BODY$
BEGIN
        NEW.updateNumber = nextval('order_num');
        return NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION set_user_number()
RETURNS "trigger" AS
$BODY$
BEGIN
        NEW.userID = nextval('order_num');
        return NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;


DROP TRIGGER IF EXISTS set_order_number_trigger ON Orders;
DROP Trigger IF exists set_update_number_trigger ON ProductUpdates;
DROP Trigger IF exists set_user_number_trigger ON Users;

CREATE TRIGGER set_order_number_trigger
BEFORE INSERT
ON Orders
FOR EACH ROW
EXECUTE PROCEDURE set_order_number();

CREATE TRIGGER set_update_number_trigger
BEFORE INSERT
ON ProductUpdates
FOR EACH ROW
EXECUTE PROCEDURE set_update_number();

CREATE TRIGGER set_user_number_trigger
BEFORE INSERT
ON Users
FOR EACH ROW
EXECUTE PROCEDURE set_user_number();
*/
