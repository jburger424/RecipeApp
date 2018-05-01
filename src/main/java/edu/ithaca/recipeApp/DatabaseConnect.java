package edu.ithaca.recipeApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class DatabaseConnect {
  private ArrayList<String> ingreds;
  private int userId;
  private Connection connection;

  public DatabaseConnect(){
    userId = -1;
    connection = null;
    try{
      Class.forName("org.sqlite.JDBC");
      this.connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
    }
    catch (Exception e){
      e.printStackTrace();
    }

  }

  public void setFilter(ArrayList<String> ingreds){
    this.ingreds = ingreds;
  }

  public void viewRecipe(int ID){
    ArrayList<Ingredient> ingredients = new ArrayList<>();

    try
    {
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      ResultSet rs = statement.executeQuery("select * from RECIPE_TO_INGREDIENT WHERE RECIPE_ID="+ID);
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
      while(rs.next())
      {
        // read the result set
        System.out.println("\n*"+rs.getString("title")+"*");
        System.out.println("-----------------------");
        System.out.println("Ingredients:");
        for(Ingredient i: ingredients){
          System.out.println("\t"+i.toString());
        }
        System.out.println("Servings: "+rs.getInt("SERVINGS"));
        System.out.println("Calories/Serving: "+rs.getInt("CALS_PER_SERVING"));
        System.out.println("Steps:");
        String[] steps = rs.getString("steps").split("\\r?\\n|\\r");
        for(String step:steps){
          System.out.println("\t"+step);
        }
        System.out.println();
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
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }

  public void listRecipes(){
    try
    {
      // create a database connection
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      StringBuilder listQuery = new StringBuilder();
      listQuery.append("SELECT NAME, RECIPE_ID, COUNT(NAME) AS MATCHING_INGREDS FROM(SELECT RECIPES.ID AS RECIPE_ID, RECIPES.TITLE AS NAME, INGREDIENT_ID, INGREDIENTS.NAME AS INGREDIENT FROM RECIPES JOIN RECIPE_TO_INGREDIENT ON RECIPES.ID=RECIPE_TO_INGREDIENT.RECIPE_ID JOIN INGREDIENTS ON RECIPE_TO_INGREDIENT.INGREDIENT_ID=INGREDIENTS.ID WHERE");
      if(ingreds != null){
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
      listQuery.append("') GROUP BY NAME ORDER BY MATCHING_INGREDS DESC;");
      ResultSet rs = statement.executeQuery(listQuery.toString());
      while(rs.next())
      {
        // read the result set
        System.out.print(rs.getInt("RECIPE_ID")+".");
        System.out.println(rs.getString("NAME"));
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
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }

  public boolean userLoggedIn(){
    return userId != -1;
  }

  public boolean logInUser(String username, String password){
    try
    {
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
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
    return false;
  }

  public boolean userExists(String username){
    try
    {
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
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
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
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
    return false;
  }


  // load the sqlite-JDBC driver using the current class loader
  public static void main(String[] args) throws ClassNotFoundException
  {
    Class.forName("org.sqlite.JDBC");
    DatabaseConnect db = new DatabaseConnect();
    System.out.println(db.addUser("jon","1234"));
  }
}

