


/*
SELECT * FROM Users;

SELECT * FROM Warehouse;
*/


/*
SELECT * FROM ORDERS;

UPDATE Product SET numberOfUnits = 38  WHERE storeID ='12' AND productName = 'Egg';

SELECT * FROM Product;

*/


/*

SELECT * FROM Store;

SELECT * FROM ProductUpdates;



SELECT * FROM Orders WHERE customerID = 1 ORDER BY orderTime DESC LIMIT 5;



SELECT S.storeID FROM  Store S, Users U WHERE S.managerID = U.userID AND U.userID = 10;

SELECT * FROM Store WHERE managerID = 10;


SELECT DISTINCT O.orderNumber as orderID, U.name as customer_name, O.storeID, O.productName, O.orderTime as date FROM Orders O, Users U WHERE O.customerID = U.userID AND O.storeID IN (SELECT S.storeID FROM  Store S, Users U WHERE S.managerID = U.userID AND U.userID = 10) ORDER by O.orderTime DESC LIMIT 5;
*/

INSERT INTO USERS( name, password, latitude, longitude, type) VALUES('Tri','xyz', 30, 32,'admin');
SELECT * FROM Users;

