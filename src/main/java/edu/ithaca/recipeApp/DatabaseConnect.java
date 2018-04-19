package edu.ithaca.recipeApp;

//import com.sun.xml.internal.bind.v2.model.core.ID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseConnect {
  private ArrayList<String> ingreds;

  //TODO do we want to add or modify current filters?
  public void setFilter(ArrayList<String> ingreds){
    this.ingreds = ingreds;
  }
  public void viewRecipe(int ID){
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    // load the sqlite-JDBC driver using the current class loader
    try{
      Class.forName("org.sqlite.JDBC");
    }
    catch (Exception e){
      e.printStackTrace();
    }



    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
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
    // load the sqlite-JDBC driver using the current class loader
    try{
      Class.forName("org.sqlite.JDBC");
    }
    catch (Exception e){
      e.printStackTrace();
    }



    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:/Users/jonburger/Google_Drive/recipeapp/src/test/resources/db/recipes.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      StringBuilder listQuery = new StringBuilder();
      listQuery.append("SELECT ID, TITLE FROM RECIPES EXCEPT SELECT DISTINCT RECIPE_ID, NAME FROM (SELECT RECIPES.ID AS RECIPE_ID, RECIPES.TITLE AS NAME, INGREDIENT_ID, INGREDIENTS.NAME AS INGREDIENT FROM RECIPES JOIN RECIPE_TO_INGREDIENT ON RECIPES.ID = RECIPE_TO_INGREDIENT.RECIPE_ID JOIN INGREDIENTS ON RECIPE_TO_INGREDIENT.INGREDIENT_ID = INGREDIENTS.ID WHERE NOT (");
      if(ingreds != null){
        for (int i = 0; i < ingreds.size(); i++) {
          listQuery.append(" INGREDIENT LIKE '");
          listQuery.append(ingreds.get(i));
          if(i==ingreds.size()-1) {
            listQuery.append("'));)");
          }
          else{
            listQuery.append("' OR");
          }
      }
      }
      else{
          listQuery.append("1));)");
        }
      System.out.println(listQuery.toString());
      ResultSet rs = statement.executeQuery(listQuery.toString());
      while(rs.next())
      {
        // read the result set
        System.out.print(rs.getInt("ID")+".");
        System.out.println(rs.getString("title"));
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
  // load the sqlite-JDBC driver using the current class loader
  public static void main(String[] args) throws ClassNotFoundException
  {
    Class.forName("org.sqlite.JDBC");
  }
}

