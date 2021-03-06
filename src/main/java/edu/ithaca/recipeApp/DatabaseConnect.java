package edu.ithaca.recipeApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseConnect {
  private ArrayList<String> ingreds, tags;
  public int userId;
  private Connection connection;
  private RateRecipe rater;

    /**
     * This function establishes a connection to the database
     */
  public DatabaseConnect(){
    ingreds = new ArrayList<>();
    tags = new ArrayList<>();
    userId = -1;
    connection = null;
    rater = new RateRecipe();
    try{
      Class.forName("org.sqlite.JDBC");
    }
    catch (Exception e){
      e.printStackTrace();
    }

  }

    /**
     * This function sets the active filter based on ingredients and tags
     * @param ingreds
     * @param tags
     */
  public void setFilter(ArrayList<String> ingreds, ArrayList<String> tags){
    this.ingreds = ingreds;
    this.tags = tags;
  }

    /**
     * This function creates and opens a window with all of the recipes information and and image in it
     * @param ID
     * @return
     */
  public WindowDisplay viewRecipe(int ID){
    int userRating = rater.getUsersRating(this.userId, ID);
    float avgRating = rater.getAverage(ID);
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    WindowDisplay wd;
    ResultSet rs;
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      rs = statement.executeQuery("select * from RECIPE_TO_INGREDIENT WHERE RECIPE_ID="+ID);
      while(rs.next())
      {
        int ingredID = rs.getInt("INGREDIENT_ID");
        String ingredQuant = rs.getString("QUANTITY");
        Ingredient tempIngred = new Ingredient();
        tempIngred.setID(ingredID);
        tempIngred.setQuantity(ingredQuant);
        ingredients.add(tempIngred);
      }
      for (Ingredient i:ingredients){
        rs = statement.executeQuery("select * from INGREDIENTS WHERE ID="+i.getID());
        i.setName(rs.getString("NAME"));
      }
      rs = statement.executeQuery("select * from RECIPES WHERE ID="+ID);
      while(rs.next()) {
        // read the result set
        System.out.println("\n*" + rs.getString("title") + "*");
        System.out.println("-----------------------");
        if (userRating > -1) {
          System.out.println("Your Rating: "+userRating);
        }
        if (avgRating > 0) {
          System.out.println("Average Rating: "+avgRating);
        }
        System.out.println("Ingredients:");
        for (Ingredient i : ingredients) {
          System.out.println("\t" + i.toString());
        }
        System.out.println("Servings: " + rs.getInt("SERVINGS"));
        System.out.println("Calories/Serving: " + rs.getInt("CALS_PER_SERVING"));
        System.out.println("Steps:");
        String[] steps = rs.getString("steps").split("\\r?\\n|\\r");
        for (String step : steps) {
          System.out.println("\t" + step);
        }
        System.out.println();
        wd = new WindowDisplay(ID, //ID
            rs.getString("title"), //Title
            rs.getInt("SERVINGS"),  //Servings
            rs.getInt("CALS_PER_SERVING"), //Calories per serving
            ingredients, //Ingredients
            rs.getString("steps").split("\\r?\\n|\\r"),
            rs.getString("image")
        ); //Steps
        return wd;
      }
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      closeConnection();
    }
    return null;
  }

    /**
     *
     * @param recipeId
     * @return
     */
  public boolean recipeContainsAllTags(int recipeId){
    int numTags = tags.size();
    if(numTags == 0) return true;
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      StringBuilder query = new StringBuilder();
      query.append("SELECT COUNT(TAGS.NAME) AS NUM_MATCHING_TAGS FROM RECIPE_TO_TAG JOIN TAGS ON TAGS.ID=RECIPE_TO_TAG.TAG_ID WHERE RECIPE_ID=");
      query.append(recipeId);
      query.append(" AND (");
      for (int i = 0; i < numTags; i++) {
        query.append(" TAGS.NAME = '"+tags.get(i)+"'");
        if(i == numTags - 1) query.append(" );");
        else query.append(" OR");
      }
      ResultSet rs = statement.executeQuery(query.toString());
      if(rs.next()){
        int numMatching = rs.getInt("NUM_MATCHING_TAGS");
        return numMatching == numTags;
      }

    }
    catch(SQLException e){
      e.printStackTrace();
    }
    finally
    {
      closeConnection();
    }
    return false;
  }

    /**
     * This function lists all of the recipes in the database
     */
  public void listRecipes(){
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      StringBuilder listQuery = new StringBuilder();
      listQuery.append("SELECT NAME, RECIPE_ID, COUNT(NAME) AS MATCHING_INGREDS FROM(SELECT RECIPES.ID AS RECIPE_ID, RECIPES.TITLE AS NAME, INGREDIENT_ID, INGREDIENTS.NAME AS INGREDIENT FROM RECIPES JOIN RECIPE_TO_INGREDIENT ON RECIPES.ID=RECIPE_TO_INGREDIENT.RECIPE_ID JOIN INGREDIENTS ON RECIPE_TO_INGREDIENT.INGREDIENT_ID=INGREDIENTS.ID WHERE");
      if(ingreds.size() > 0){
        for (int i = 0; i < ingreds.size(); i++) {
          listQuery.append(" INGREDIENT LIKE '");
          listQuery.append(ingreds.get(i));
          if(i==ingreds.size()-1) {

          }
          else{
            listQuery.append("' OR");
          }
      }
      }
      else{
        listQuery.append(" INGREDIENT LIKE '%");
      }
      String orderBy = ingreds.size() == 0 ? "RECIPE_ID ASC;" : "MATCHING_INGREDS ASC;";
      listQuery.append("') GROUP BY NAME ORDER BY "+orderBy);
      ResultSet rs = statement.executeQuery(listQuery.toString());
      while(rs.next())
      {
        int recipeId = rs.getInt("RECIPE_ID");
        String recipeName = rs.getString("NAME");
        if(recipeContainsAllTags(recipeId)){
          System.out.print(recipeId+".");
          System.out.println(recipeName);
        }
      }
    }
    catch(SQLException e) {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      closeConnection();
    }
  }


    /**
     * This function queries the database for the logged in users ID based on their username and password
     * @param username
     * @param password
     * @return
     */
  public int getUserID(String username, String password){
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      ResultSet rs = statement.executeQuery("select * from USER WHERE USERNAME='"+username+"' AND PASSWORD='"+password+"'");
      if(rs.next()){
        int userId = rs.getInt("ID");
        return userId;
      }
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    finally
    {
      closeConnection();
    }
    return -1;
  }

    /**
     * This method indicates if a user is logged in or not by returning a boolean
     * @param username
     * @param password
     * @return
     */
  public boolean logInUser(String username, String password){
    int userID = getUserID(username, password);
    if(userID != -1){
      this.userId = userID;
      return true;
    }
    return false;
  }

    /**
     * this function indicates if a user exits or not based on a boolean value
     * @param username
     * @return
     */
  public boolean userExists(String username){
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      ResultSet rs = statement.executeQuery("select * from USER WHERE USERNAME='"+username+"'");
      return rs.next();
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    finally
    {
      closeConnection();
    }
    return false;
  }

    /**
     * This function adds a user to the database if they do not already exist
     * @param username
     * @param password
     * @return
     */
  public boolean addUser(String username, String password){
    if(userExists(username)){
      System.out.println("ERROR: User '"+username+"' already exists");
      return false;
    }
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
      String statement = "INSERT INTO USER(username, password) VALUES(?,?)";
      PreparedStatement preparedStatement = connection.prepareStatement(statement);
  //    statement.setQueryTimeout(30);  // set timeout to 30 sec.
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      preparedStatement.executeUpdate();
      return true;
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      closeConnection();
    }
    return false;
  }


  public void closeConnection(){
    try
    {
      if(connection != null){
        connection.close();
        connection = null;
      }

    }
    catch(SQLException e)
    {
      // connection close failed.
      System.err.println(e);
    }
  }


  // load the sqlite-JDBC driver using the current class loader
  public static void main(String[] args) throws ClassNotFoundException
  {
    Class.forName("org.sqlite.JDBC");
    DatabaseConnect db = new DatabaseConnect();
    System.out.println(db.addUser("jon","1234"));
  }
}

