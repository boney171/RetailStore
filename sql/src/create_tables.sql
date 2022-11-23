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
