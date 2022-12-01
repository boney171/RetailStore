/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class Retail {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Retail shop
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Retail(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Retail

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
        public static int i = 505;//Order number, will be incremented everytime a new order comes in
	public static int j = 55;//Update number, will be incremented everytime a new update comes in
	public static int u = 106;//User ID number, will be incremented everytime a new user is inserted
   public static int psr = 11;
	public static String uId;
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Retail.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      Greeting();
      Retail esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Retail object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Retail (dbname, dbport, user, "");
         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Stores within 30 miles");
                System.out.println("2. View Product List");
                System.out.println("3. Place a Order");
                System.out.println("4. View 5 recent orders");

                //the following functionalities basically used by managers
                System.out.println("5. Update Product");
                System.out.println("6. View 5 recent Product Updates Info");
                System.out.println("7. View 5 Popular Items");
                System.out.println("8. View 5 Popular Customers");
                System.out.println("9. Place Product Supply Request to Warehouse");
		System.out.println("10. View Orders (Admin And Manager Only)");
		System.out.println("11. View Users (Admin only)");
		System.out.println("12. Add A New User (Admin only)");
		System.out.println("13. Add A New Product (Admin only)");
		System.out.println("14. Remove A User (Admin only)");
		System.out.println("15. Remove A Product (Admin only)");
                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewStores(esql); break;
                   case 2: viewProducts(esql); break;
                   case 3: placeOrder(esql); break;
                   case 4: viewRecentOrders(esql); break;
                   case 5: updateProduct(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewPopularProducts(esql); break;
                   case 8: viewPopularCustomers(esql); break;
                   case 9: placeProductSupplyRequests(esql); break;
                   case 10: viewOrders(esql); break;
		   case 11: viewUsers(esql); break;
		   case 12: addUsers(esql); break;
		   case 13: addProducts(esql); break;
		   case 14: removeUsers(esql); break;
		   case 15: removeProducts(esql); break;



                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine();       //enter lat value between [0.0, 100.0]
         System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         String longitude = in.readLine();
         
         String type="Customer";

String query = String.format("INSERT INTO USERS (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
	 
	//Store user and password

         String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
	 
	int userNum = esql.executeQuery(query);
	 if (userNum > 0){
		 //These extra operations are to extract userId to be used through out the whole program
		  String query2 = String.format("SELECT userId FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
                List<List<String>> userInfo = esql.executeQueryAndReturnResult(query2);
		List<String>  info = userInfo.get(0);
		 //uId is global varible that contains the current's userId
		  uId = info.get(0);	
		return name;
	}
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   public static void viewStores(Retail esql) {
	 try{
	String query = String.format("SELECT s.storeID, s.name, calculate_distance(u.latitude, u.longitude, s.latitude, s.latitude) as dist FROM Users u, store s WHERE u.userId='%s' and calculate_distance(u.latitude, u.longitude, s.latitude, s.longitude) < 30",uId);

	int q = esql.executeQueryAndPrintResult(query);
	System.out.println("total row(s): " + q);
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }

}
   public static void viewProducts(Retail esql) {
	 try{
	System.out.println("\tEnter store ID number: ");
	String id = in.readLine();
	String query = String.format("SELECT P.productName, P.numberOfUnits, P.pricePerUnit FROM Product P WHERE P.storeID = '%s'", id);
	int q = esql.executeQueryAndPrintResult(query);		
         }catch(Exception e){
		System.err.println (e.getMessage ());
	return;
	}
}
   public static void placeOrder(Retail esql) {
	try{
        //Get Store ID
	System.out.println("\tEnter store ID number in your area:  \n\t");
        String storeId = in.readLine();
	String query1 = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND storeID IN", storeId);
        String query2 = String.format("(SELECT s.storeID  FROM Users u, store s WHERE u.userId='%s' and calculate_distance(u.latitude, u.longitude, s.latitude, s.longitude) < 30)",uId);
	query1 = query1 + query2;
	int q = esql.executeQuery(query1);
	//If user pick a store not in his/her area, make he/she pick again!
	while( q == 0)
	{
		System.out.println("\tYour selected store is not in your area, select another store in you area: \n\t");
		storeId = in.readLine();
		query1 = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND storeID IN", storeId);
        	query2 = String.format("(SELECT s.storeID  FROM Users u, store s WHERE u.userId='%s' and calculate_distance(u.latitude, u.longitude, s.latitude, s.longitude) < 30)",uId);
        	query1 = query1 + query2;
        	q = esql.executeQuery(query1);
	}
	//Get ProductName
	System.out.println("\tEnter product's name: \n\t");
	String productName = in.readLine();
	query1 = String.format("SELECT * FROM Product P WHERE P.storeID = '%s' AND P.productName = '%s'", storeId, productName);
	q = esql.executeQuery(query1);
	//If product does not exist, make he/she pick again!
	while(q == 0)
	{
		System.out.println("\tYour selected product does not exist, pick another product: \n\t");
		productName = in.readLine();
		q = esql.executeQuery(query1);
	}
	//Get numberOfUnits
	System.out.println("\tEnter the amount: \n\t");
	String amount = in.readLine();
	query1 = String.format("SELECT * FROM Product P WHERE P.storeID = '%s' AND P.productName = '%s' AND P.numberOfUnits >= '%s'", storeId, productName, amount);
	//query2 = String.format("SELECT * FROM Product P WHERE P.storeID = '%s' AND P.productName = '%s' AND P.numberOfUnits >= '%s'", storeId, productName, amount);
	q = esql.executeQuery(query1);
	//q = esql.executeQueryAndPrintResult(query2);
	//If amount exceed in-stock amount, make he/she enter a new amount
	while(q == 0)
	{	
		System.out.println("\tYour selected amount exceeded our in-stock amount, please enter a new amount: \n\t");
		amount = in.readLine();
		query1 = String.format("SELECT * FROM Product P WHERE P.storeID = '%s' AND P.productName = '%s' AND P.numberOfUnits >= '%s'", storeId, productName, amount);
		q = esql.executeQuery(query1);
	}
	System.out.println("\tCompleting your orders... Succeed!");
	//Input order into order relation
	String orderNumber = String.valueOf(i);
	i++;
	query2 = String.format("INSERT INTO Orders(orderNumber,customerID,storeID,productName,unitsOrdered,orderTime) VALUES('%s','%s','%s','%s','%s',NOW())",orderNumber,uId,storeId,productName,amount);
	esql.executeUpdate(query2);
	//Update products relationn
	 query2 = String.format("UPDATE Product SET numberOfUnits = numberOfUnits - '%s' WHERE storeID = '%s' AND productName = '%s'", amount, storeId, productName);
	 esql.executeUpdate(query2);
         }catch(Exception e){
                System.err.println (e.getMessage ());
        return;
        }
	
}
   public static void viewRecentOrders(Retail esql) {
   try{
      //Check if user is a customer
       String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'customer'",uId );
       int q = esql.executeQuery(query);
       //If user is a customer, show 5 most recent orders
       if( q == 1){
       		System.out.println("View 5 most recent orders: ");
       		query = String.format("SELECT O.storeID as ID, S.name as SName,O.productName, O.unitsOrdered as Number, O.orderTime as Date FROM Orders O,Store S WHERE O.storeID = S.storeID AND customerID = '%s' ORDER BY orderTime DESC LIMIT 5", uId);
       		q = esql.executeQueryAndPrintResult(query);
       		return;
       }
	
	//Check If user is a manager, show most 5 recent orders of his/her stores
       query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'manager'",uId );
       q = esql.executeQuery(query);
       //If user is an admin, show 5 most recent orders from her stores
       if( q == 1){ // Confirm that user is a manager
       		System.out.println("Hello manager!");
		query = String.format("SELECT DISTINCT O.orderNumber as orderID, U.name as customer_name, O.storeID, O.productName, O.orderTime as date FROM Orders O, Users U WHERE O.customerID = U.userID AND O.storeID IN (SELECT S.storeID FROM  Store S, Users U WHERE S.managerID = U.userID AND U.userID = '%s') ORDER by O.orderTime DESC LIMIT 5", uId);
		q = esql.executeQueryAndPrintResult(query);
       		return;
       }
	//Check if user is an admin, show most 5 recent orders from orders relation
       query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
       q = esql.executeQuery(query);
	if( q == 1){ // Confirm that user is an admin
                System.out.println("Hello admin!");
                query = "SELECT DISTINCT O.orderNumber as orderID, U.name as customer_name, O.storeID, O.productName, O.orderTime as date FROM Orders O, Users U WHERE O.customerID = U.userID ORDER by O.orderTime DESC LIMIT 5";
                q = esql.executeQueryAndPrintResult(query);
                return;
       }

      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
}
   public static void updateProduct(Retail esql){               
  try{
        //Check if a user is a customer or an admin or a manager
        String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'customer'",uId );
        int q = esql.executeQuery(query);
	if( q == 1){ //User is a customer, return to main menu
		System.out.println("Unauthorised user, return to main menu!");
		return;
	}
	//User is either an admin or a manager
	//Check if a user is an admin
	query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
	q = esql.executeQuery(query);
	if(q == 1 ){
	//Functionality of admin
	//Admin should be able to update any product
	System.out.println("Welcome admin, select a storeID: ");
	String storeId = in.readLine();
	query = String.format("SELECT * from Store S WHERE S.storeID = '%s'", storeId);
        q = esql.executeQuery(query);
	//Check if a selected store exists, if not, keep looping until store
	while( q == 0)
{
	System.out.println("Selected Store does not exists, select a valid storeID: ");
	storeId = in.readLine();

	query = String.format("SELECT * from Store S WHERE S.storeID = '%s'", storeId);
        q = esql.executeQuery(query);	
}	
//Get product information
	System.out.println("Select the product that you want to update: ");
        String productName = in.readLine();
	query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'", storeId, productName);
       //Check if the selected product exist
        q = esql.executeQuery(query);
       //If not exists, keep looping until get a valid product name
	 while(q == 0)
 {
        System.out.println("Your selected product does not exists, select another product: ");
        productName = in.readLine();
        query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'", storeId, productName);
        q = esql.executeQuery(query);
}

	System.out.println("\tEnter a new number of available unit: ");
        String numberOfUnits = in.readLine();

        System.out.println("\tEnter a new price per unit: ");
        String pricePerUnit = in.readLine();

        System.out.println(" Successful!");
	//Update the new information into the product relation
	query = String.format("UPDATE Product SET numberOfUnits = '%s', pricePerUnit = '%s' WHERE storeID = '%s' AND productName = '%s'", numberOfUnits, pricePerUnit, storeId, productName);
        esql.executeUpdate(query);
	//Update the new update information into the ProductUpdates relation
	query = String.format("INSERT INTO ProductUpdates(updateNumber, managerID,storeID,productName,updatedOn) VALUES('%s','%s','%s','%s',NOW())",j, uId, storeId, productName );
        esql.executeUpdate(query);
	j++;
	}//End functionality of admin
	else{ //Start functionality of the manager
	System.out.println("Hello manager, ligmad");
	System.out.println("Enter a store ID: ");
	String storeId = in.readLine();
	//Check if the this manager manages the store
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
	//q = esql.executeQueryAndPrintResult(query);
	q = esql.executeQuery(query);
	//Keep looping until the user enter a valid storeId
	while( q == 0)
{
	System.out.println("Sorry you'are not managing this store, select another store: ");
	storeId = in.readLine();	
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
 
       //q = esql.executeQueryAndPrintResult(query);
        q = esql.executeQuery(query);

}
	System.out.println("You are managing this store!");
	System.out.println("Select the product that you want to update: ");
	String productName = in.readLine();
	//Check if the product exists in the store;
	query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'", storeId, productName);
	//q = esql.executeQueryAndPrintResult(query);
	q = esql.executeQuery(query);
	while(q == 0) //Product is not inside the store, loop until get the valid product
{
	System.out.println("Your selected product does not exists, select another product: ");
	productName = in.readLine();
	query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'", storeId, productName);
	q = esql.executeQuery(query);	
}	
	//Get the numberOfUnits and pricePerUnit 
	System.out.println("\tEnter a new number of available unit: ");
	String numberOfUnits = in.readLine();

	System.out.println("\tEnter a new price per unit: ");
	String pricePerUnit = in.readLine();

	System.out.println(" Successful!");
	//Update the new information into the product relation
	query = String.format("UPDATE Product SET numberOfUnits = '%s', pricePerUnit = '%s' WHERE storeID = '%s' AND productName = '%s'", numberOfUnits, pricePerUnit, storeId, productName);
	esql.executeUpdate(query);
	//Update the new update information into the ProductUpdates relation
	query = String.format("INSERT INTO ProductUpdates(updateNumber, managerID,storeID,productName,updatedOn) VALUES('%s','%s','%s','%s',NOW())",j, uId, storeId, productName );
	esql.executeUpdate(query);
	j++;
	}//End functionality of manager
      }catch(Exception e){
                System.err.println (e.getMessage ());
        return;
        }
}
   public static void viewRecentUpdates(Retail esql) {
      try{
        //Check if a user is a customer or an admin or a manager
        String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'customer'",uId );
        int q = esql.executeQuery(query);
	if( q == 1){ //User is a customer, return to main menu
		System.out.println("Unauthorised user, return to main menu!");
		return;
	}
	//User is either an admin or a manager
	//Check if a user is an admin
	query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'manager'",uId );
	q = esql.executeQuery(query);
	System.out.println("Hello manager");
	System.out.println("Enter a store ID: ");
	String storeId = in.readLine();
	//Check if the this manager manages the store
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
	q = esql.executeQueryAndPrintResult(query);
	q = esql.executeQuery(query);
	//Keep looping until the user enter a valid storeId
	while( q == 0)
{
	System.out.println("Sorry you'are not managing this store, select another store: ");
	storeId = in.readLine();	
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
 
       //q = esql.executeQueryAndPrintResult(query);
        q = esql.executeQuery(query);

}
   System.out.println("You are managing this store!");
      query = String.format("SELECT * from ProductUpdates P, where P.storeID in (select S.storeID from Stores S, Users U where S.managerID = U.userID and U.userID = '&s' DESCS LIMIT 5", uId);
      q = esql.executeQueryAndPrintResult(query);
      }
      }
       catch(Exception e) {
         System.err.println (e.getMessage ());
      }

   }
   public static void viewPopularProducts(Retail esql) {
       try{
        //Check if a user is a customer or an admin or a manager
        String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'customer'",uId );
        int q = esql.executeQuery(query);
	if( q == 1){ //User is a customer, return to main menu
		System.out.println("Unauthorised user, return to main menu!");
		return;
	}
	//User is either an admin or a manager
	//Check if a user is an admin
	query = String.format("SELECT O.productName, count(unitsOrdered) from Orders O where O.storeID in (select S.storeID from Stores S, Users S where S.managerID = U.userID and U.userID = '&s') order by O.orderTime descs limit 5",uId);
	q = esql.executeQuery(query);
	if(q == 1 ){
	//Functionality of admin
	 System.out.println("Hello Admin");
	}
	else{
	System.out.println("Hello manager, ligmad");
	System.out.println("Enter a store ID: ");
	String storeId = in.readLine();
	//Check if the this manager manages the store
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
	q = esql.executeQueryAndPrintResult(query);
	q = esql.executeQuery(query);
	//Keep looping until the user enter a valid storeId
	while( q == 0)
{
	System.out.println("Sorry you'are not managing this store, select another store: ");
	storeId = in.readLine();	
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
 
       //q = esql.executeQueryAndPrintResult(query);
        q = esql.executeQuery(query);

}
   System.out.println("You are managing this store!");
   query = String.format("SELECT O.productName, COUNT(O.unitsOrdered) AS TOTAL_TIMES_ORDERED FROM Orders O WHERE O.storeID IN (SELECT S.storeID FROM Store S, Users U where S.managerID = U.userID and U.userID ='&s' GROUP BY O.productName ORDER BY TOTAL_TIMES_ORDERED DESC LIMIT 5",uId);
      q = esql.executeQueryAndPrintResult(query);
   }
   }
    catch(Exception e) {
         System.err.println (e.getMessage ());
      }

   }

   public static void viewPopularCustomers(Retail esql) {
      try{
        //Check if a user is a customer or an admin or a manager
        String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'customer'",uId );
        int q = esql.executeQuery(query);
	if( q == 1){ //User is a customer, return to main menu
		System.out.println("Unauthorised user, return to main menu!");
		return;
	}
	//User is either an admin or a manager
	//Check if a user is an admin
	query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
	q = esql.executeQuery(query);
	if(q == 1 ){
	//Functionality of admin
	 System.out.println("Hello Admin, suck mah D");
	}
	else{
	System.out.println("Hello manager, ligmad");
	System.out.println("Enter a store ID: ");
	String storeId = in.readLine();
	//Check if the this manager manages the store
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
	q = esql.executeQueryAndPrintResult(query);
	q = esql.executeQuery(query);
	//Keep looping until the user enter a valid storeId
	while( q == 0)
{
	System.out.println("Sorry you'are not managing this store, select another store: ");
	storeId = in.readLine();	
	query = String.format("SELECT * FROM STORE WHERE storeID = '%s' AND managerID = '%s'", storeId,uId);
 
       //q = esql.executeQueryAndPrintResult(query);
        q = esql.executeQuery(query);

}

   
   System.out.println("You are managing this store!");
   
	query = String.format("SELECT customerID, count(*) from Orders O where O.storeID in (select S.storeID from Stores S, Users S where S.managerID = U.userID and U.userID = '&s') order by O.orderTime descs limit 5",uId);
   q = esql.executeQueryAndPrintResult(query);
   }
   }
    catch(Exception e) {
         System.err.println (e.getMessage ());
      }
   }

   public static void placeProductSupplyRequests(Retail esql) {
      
try{
        //Check if a user is a customer or an admin or a manager
        String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'customer'",uId );
        int q = esql.executeQuery(query);
	if( q == 1){ //User is a customer, return to main menu
		System.out.println("Unauthorised user, return to main menu!");
		return;
	}
	//User is either an admin or a manager
	//Check if a user is an admin
	query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'manager'",uId );
	q = esql.executeQuery(query);
	
   while(q == 0){
   System.out.println("You are not Manager, please enter your UserID again Manager: ");
   uId = in.readLine();
   q = esql.executeQuery(query);
 }

	//Functionality of admin
	//Admin should be able to update any product
	System.out.println("Welcome admin, select a storeID: ");
	String storeId = in.readLine();
	query = String.format("SELECT * from Store S WHERE S.storeID = '%s'", storeId);
        q = esql.executeQuery(query);
	//Check if a selected store exists, if not, keep looping until store
	while( q == 0)
{
	System.out.println("Selected Store does not exists, select a valid storeID: ");
	storeId = in.readLine();

	query = String.format("SELECT * from Store S WHERE S.storeID = '%s'", storeId);
        q = esql.executeQuery(query);	
}	
//Get product information
	System.out.println("Please Enter the product you want to order: ");
        String productName = in.readLine();
	query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'", storeId, productName);
       //Check if the selected product exist
        q = esql.executeQuery(query);
       //If not exists, keep looping until get a valid product name
	 while(q == 0)
 {
        System.out.println("Your selected product does not exists, select another product: ");
        productName = in.readLine();
        query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'", storeId, productName);
        q = esql.executeQuery(query);
}

	System.out.println("\tPlease Enter how many units you want ordered:  ");
        String numberOfUnits = in.readLine();

        System.out.println("\tPlease Enter from which warehouse you want to order from: ");
        String pricePerUnit = in.readLine();

        System.out.println(" Successful!");
	//Update the new information into the product relation
	query = String.format("UPDATE Product SET numberOfUnits = '%s' WHERE storeID = '%s' AND productName = '%s'", numberOfUnits, storeId, productName);
        esql.executeUpdate(query);
	//Update the new update information into the ProductUpdates relation
	query = String.format("INSERT INTO productSupplyRequests(requestNumber, managerID,warehouseID, storeID,productName,unitsRequested) VALUES('%s','%s','%s','%s','&s',NOW())",psr, uId, warehouseID, storeId, productName, unitsRequested );
        esql.executeUpdate(query);
	psr++;
	}//End functionality of manager
      catch(Exception e){
                System.err.println (e.getMessage ());
        return;
        }
   }
   public static void viewOrders(Retail esql){
	 try{
        //Check if user is an admin or not
        String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
        int q = esql.executeQuery(query);
        if(q == 1){//User is an admin
 		query = "Select * FROM Orders";
                q = esql.executeQueryAndPrintResult(query);
                return;
        }
        query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'manager'",uId );
        q = esql.executeQuery(query);
 	if(q == 1){//User is an manager
	 	query = String.format("SELECT DISTINCT O.orderNumber as orderID, U.name as customer_name, O.storeID, O.productName, O.orderTime as date FROM Orders O, Users U WHERE O.customerID = U.userID AND O.storeID IN (SELECT S.storeID FROM  Store S, Users U WHERE S.managerID = U.userID AND U.userID = '%s') ORDER by O.orderTime DESC", uId);
                q = esql.executeQueryAndPrintResult(query);
                return;
        }
	System.out.println("Unauthorised user, return to main menu!");
	return;       
        }catch(Exception e){
                System.err.println (e.getMessage ());
        return;
        }

};
   public static void viewUsers(Retail esql){
	 try{
        //Check if user is an admin or not
	String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );	
        int q = esql.executeQuery(query);
	if(q == 1){//User is an admin
		 query = "Select * FROM Users";
		q = esql.executeQueryAndPrintResult(query);
		return;
	}else{
		System.out.println("Unauthorised user, return to main menu!");
		return;
	}	
         }catch(Exception e){
                System.err.println (e.getMessage ());
        return;
        }		
};
   public static void addUsers(Retail esql){
        try{
        //Check if user is an admin or not
	String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
        int q = esql.executeQuery(query);
        if(q == 1){//User is an admin
                //Enter user ID or name
                System.out.println("Enter new user's name/ID: ");
		String name = in.readLine();
		while(name.length() == 0){
			System.out.println("Name/ID can not be empty, enter a new name: ");
			name = in.readLine();
		}
		System.out.println("Enter a password (At least 3 characters long): ");
		String password = in.readLine();
		while(password.length() < 3){
			System.out.println("Your selected password is too short, enter a new password: ");
			password = in.readLine();
		}
		System.out.println("Enter latitude: ");
		String latitude = in.readLine();
		while(latitude.length() == 0){
			System.out.println("Latitude can not be empty, re-enter latitude: ");
			latitude = in.readLine();
		}
		System.out.println("Enter longitude: ");
		String longitude = in.readLine();
		while(longitude.length() == 0){
                	System.out.println("Longtitude can not be empty, re-enter longitude: ");
                	longitude = in.readLine();
                }
		System.out.println("Select the user's type (admin,customer,manager): ");
		String type = in.readLine();
		query = String.format("INSERT INTO Users(userID,name,password,latitude,longitude,type) VALUES('%s','%s','%s','%s','%s','%s')", u,name,password,latitude,longitude,type);
		u++;
		System.out.println("Successful!!");
		esql.executeUpdate(query);		
                return;
        }else{
                System.out.println("Unauthorised user, return to main menu!");
                return;
        }
         }catch(Exception e){
                System.err.println (e.getMessage ());
        return;
        }

};
   public static void addProducts(Retail esql){
	 try{
        //Check if user is an admin or not
	String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
        int q = esql.executeQuery(query);
	 if(q == 1){//User is an admin
		System.out.println("Select a storeID: ");
		String storeId = in.readLine();
		//Check if storeID exists
		query = String.format("SELECT * from Store S WHERE S.storeID = '%s'", storeId);
        	q = esql.executeQuery(query);
	        //If not, ask admin to enter a new storeId
		 while( q == 0)
{
        System.out.println("Selected Store does not exists, select a valid storeID: ");
        storeId = in.readLine();

        query = String.format("SELECT * from Store S WHERE S.storeID = '%s'", storeId);
        q = esql.executeQuery(query);
}
	System.out.println("Enter product name: ");
	String productName = in.readLine();
	System.out.println("Enter number of available units: ");
	String numberOfUnits = in.readLine();
	while(numberOfUnits.length() == 0 || isNumeric(numberOfUnits) == false){
		System.out.println("Invalid input, enter a new number of available units: ");
		numberOfUnits = in.readLine();
}
	System.out.println("Enter price per unit: ");
	String price = in.readLine();
	while(price.length() == 0 || isNumeric(price) == false){
		System.out.println("Invalid input, enter a new number of price: ");
		price = in.readLine();
	}	
	query = String.format("INSERT INTO Product(storeID, productName, numberOfUnits, pricePerUnit) VALUES('%s','%s','%s','%s')", storeId,productName,numberOfUnits,price);
	esql.executeUpdate(query);
	System.out.println("Successful!!");
                return;
        }else{
                System.out.println("Unauthorised user, return to main menu!");
                return;
        }
         }catch(Exception e){
                System.err.println (e.getMessage ());
        return;
        }

};
   public static void removeUsers(Retail esql){
	   try{
    String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
        int q = esql.executeQuery(query);
        if( q == 1){
                System.out.println("Select a userID to be removed: ");
                String id = in.readLine();

                query = String.format("SELECT * FROM USERS WHERE userID = '%s'", id);
                q = esql.executeQuery(query);
                while(q == 0){
                        System.out.println("Selected user does not exists, enter another userID: ");
                        id = in.readLine();
                        q = esql.executeQuery(query);
                }
                query = String.format("DELETE  FROM Users WHERE userID = '%s'", id);
                esql.executeUpdate(query);                                                                                                                                                                    System.out.println("Successful!");
                return;
                }else{
                System.out.println("Unauthorised user, return to main menu!");
                }     
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }	
};
   public static void removeProducts(Retail esql){
 	      try{
    String query = String.format("SELECT * FROM Users WHERE userID = '%s' AND type = 'admin'",uId );
        int q = esql.executeQuery(query);
        if( q == 1){
                //Get storeId from user
		System.out.println("Select a storeID: ");
                String id = in.readLine();

                query = String.format("SELECT * FROM Store WHERE storeID = '%s'", id);
                q = esql.executeQuery(query);
                while(q == 0){
                        System.out.println("Selected storeID does not exists, enter another storeID: ");
                        id = in.readLine();
                        q = esql.executeQuery(query);
                }
		
		System.out.println("Select a productName: ");
                String name = in.readLine();

                query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'" , id,name);
                q = esql.executeQuery(query);
                while(q == 0){
                        System.out.println("Selected product does not exists in the store, enter another product name: ");
                        id = in.readLine();
                        q = esql.executeQuery(query);
                }

                query = String.format("DELETE  FROM Product WHERE storeID = '%s' AND productName = '%s'", id, name);
                esql.executeUpdate(query);                                                                                                                                                                    System.out.println("Successful!");
                return;
                }else{
                System.out.println("Unauthorised user, return to main menu!");
                }
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
 }; 

//Helper function
	public static boolean isNumeric(String str){
        try{
        Double.parseDouble(str);
        return true;
        }catch(NumberFormatException e){
        return false;
        }
}
	 