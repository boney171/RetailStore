SELECT * 
FROM Users 
WHERE userID = '1' AND type = 'admin';

SELECT updateNumber, storeID, productName 
from ProductUpdates 
where storeID IN 
(SELECT S.storeID 
FROM  Store S, Users U 
WHERE S.managerID = U.userID AND U.userID = '10') 
order by updatedOn DESC LIMIT 5;

SELECT O.productName, Count(*) as countOfOrders 
from Orders O  
GROUP BY O.productName 
Order By countOfOrders desc limit 5;

Select O.storeID , O.customerID, U.name , Count(*) as NumberofOrders 
From Orders O, Users U 
where U.userID = O.customerID AND O.storeID IN 
(SELECT S.storeID FROM  Store S, Users U WHERE S.managerID = U.userID AND U.userID = '20') 
GROUP BY O.storeID, O.customerID, U.name ORDER BY COUNT(*) desc LIMIT 5;


