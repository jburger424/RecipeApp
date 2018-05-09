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

  public DatabaseConnect(){
    ingreds = new ArrayList<>();
    tags = new ArrayList<>();
    userId = -1;
    connection = null;
    try{
      Class.forName("org.sqlite.JDBC");
    }
    catch (Exception e){
      e.printStackTrace();
    }

  }

  public void setFilter(ArrayList<String> ingreds, ArrayList<String> tags){
    this.ingreds = ingreds;
    this.tags = tags;
  }

  public WindowDisplay viewRecipe(int ID){
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

  public boolean userLoggedIn(){
    return userId != -1;
  }

  public boolean logInUser(String username, String password){
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      ResultSet rs = statement.executeQuery("select * from USER WHERE USERNAME='"+username+"' AND PASSWORD='"+password+"'");
      if(rs.next()){
        this.userId = rs.getInt("ID");
        return true;
      }
      else{
        return false;
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

