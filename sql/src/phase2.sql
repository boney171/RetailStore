DROP TABLE IF EXISTS Store CASCADE;
DROP TABLE IF EXISTS Product CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Manager CASCADE;
DROP TABLE IF EXISTS Warehouse CASCADE;
DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS ProductSupplies CASCADE;
DROP TABLE IF EXISTS Manages CASCADE;
DROP TABLE IF EXISTS ProductUpdates CASCADE;
DROP TABLE IF EXISTS ProductRequests CASCADE;

CREATE TABLE Users ( userID integer,
                     password char(11) NOT NULL,
                     name char(50) NOT NULL,
                     email char(50), 
                     PRIMARY KEY(userID)
);

CREATE TABLE Customer ( userID integer,
                        creditScore integer,
                        latitude decimal(8,6) NOT NULL,
                        longitude decimal(9,6) NOT NULL, 
                        PRIMARY KEY(userID), 
                        FOREIGN KEY(userID) REFERENCES Users(userID)
);

CREATE TABLE Manager (  userID integer,
                        degree char(20),
                        salary integer NOT NULL, 
                        PRIMARY KEY(userID), 
                        FOREIGN KEY(userID) REFERENCES Users(userID)
);

CREATE TABLE Store ( storeID integer, 
                     name char(30) NOT NULL,
                     latitude decimal(8, 6) NOT NULL,
                     longitude decimal(9, 6) NOT NULL,
                     dateEstablished date,
                     managerUserID integer NOT NULL,
		     PRIMARY KEY(storeID), 
                     FOREIGN KEY(managerUserID) REFERENCES Manager(userID)
);

CREATE TABLE Product ( storeID integer NOT NULL, 
                       productName char(30) NOT NULL,
                       numberOfUnits integer NOT NULL,
                       pricePerUnit integer NOT NULL,
                       description char(100),
                       imageURL char(30),
                       PRIMARY KEY(storeID, productName), 
                       FOREIGN KEY(storeID) REFERENCES Store(storeID)
		       ON DELETE CASCADE
);

CREATE TABLE Warehouse ( WarehouseID integer,
                         area integer,
                         latitude decimal(8,6) NOT NULL,
                         longitude decimal(9,6)  NOT NULL,
                         PRIMARY KEY(WarehouseID));

CREATE TABLE Orders (customerID integer NOT NULL,
                     storeID integer NOT NULL,
                     productName char(30) NOT NULL, 
                     unitsOrdered integer NOT NULL, 
                     orderDate date NOT NULL,
                     PRIMARY KEY(customerID, storeID, productName),
                     FOREIGN KEY(customerID) REFERENCES Customer(userID),
                     FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName)
);

CREATE TABLE ProductSupplies ( warehouseID integer NOT NULL,
                               storeID integer NOT NULL,
                               productName char(30) NOT NULL, 
                               PRIMARY KEY(warehouseID, storeID, productName),
                               FOREIGN KEY(warehouseID) REFERENCES Warehouse(warehouseID),
                               FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName)
);

CREATE TABLE ProductUpdates (   managerID integer NOT NULL,
                        	storeID integer NOT NULL,
                       		productName char(30) NOT NULL, 
                        	PRIMARY KEY(managerID, storeID, productName),
                        	FOREIGN KEY(managerID) REFERENCES Manager(userID),
                        	FOREIGN KEY(storeID, productName) REFERENCES Product(storeID, productName)
);

CREATE TABLE ProductRequests ( managerID integer NOT NULL, 
               	               storeID integer NOT NULL, 
                               productName char(30) NOT NULL, 
                               warehouseID integer NOT NULL, 
                               unitsRequested integer NOT NULL, 
                               PRIMARY KEY(managerID, storeID, productName, warehouseID), 
                               FOREIGN KEY(managerID) REFERENCES Manager(userID), 
			       FOREIGN KEY(warehouseID, storeID, productName) REFERENCES ProductSupplies(warehouseID, storeID, productName));
