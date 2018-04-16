package edu.ithaca.recipeApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class editRecipe {

    public void edit(){
        Connection connection = null;
        String url = "jdbc:sqlite:C:/Users/Conor/IdeaProjects/RecipeApp/db/recipes.db";
        //String url = "jdbc:mysql://localhost:3306/";
        String dbName = "C:/Users/Conor/IdeaProjects/RecipeApp/db/recipes.db";
        String driverName = "com.mysql.jdbc.Driver";
        String userName = "abarrett";
        String password = "abarrett1";
        ArrayList<String> rowArray = new ArrayList<String>();

        Scanner reader = new Scanner(System.in);


        try{
            //Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);

            try{

                System.out.println("Enter recipe id to edit: ");
                System.out.print(">_");
                int recipeId = reader.nextInt();
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT * FROM RECIPES WHERE ID="+recipeId;
                ResultSet rs = stmt.executeQuery(selectquery);


                while(rs.next()){
                    System.out.println("Editing: "+ rs.getInt("id") + "\t" + rs.getString("title"));
                }

                System.out.println("What would you like to edit? ");
                System.out.println("TITLE, IMAGE, SOURCE, STEPS, SERVINGS, or CALS_PER_SERVING?");
                System.out.print(">_");

                rs = stmt.executeQuery(selectquery);
                String col = reader.nextLine();
                col = reader.nextLine();
                System.out.println("CURRENT: "+rs.getString(col));

                System.out.println("Enter new value: ");
                System.out.print(">_");
                String newString = reader.nextLine();
                String editQuery = "UPDATE RECIPES SET "+col+"='"+newString+"' WHERE ID="+recipeId;
                stmt.executeUpdate(editQuery);
                rs = stmt.executeQuery(selectquery);
                System.out.println("NEW: "+rs.getString(col));



            }
            catch(SQLException s){
                System.out.println(s);
            }
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


    public static void main(String[] args){

        editRecipe rec = new editRecipe();
        rec.edit();

    }
}
