package edu.ithaca.recipeApp;

import javax.security.auth.callback.TextOutputCallback;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Favorites {

    /**
     * This function adds a recipe by its id as a favorite for the corresponding user's id
     * @param userID
     * @param recipeID
     */
    public static void addFavorite(int userID, int recipeID){
        boolean rowExists = userToRecipeExists(userID, recipeID);
        Connection connection = null;

        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";

        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);

            try{
                Statement stmt = connection.createStatement();
                if(!rowExists){
                    String selectquery = "INSERT INTO USER_TO_RECIPE (USER_ID,RECIPE_ID, DID_SAVE) VALUES (" + userID + "," + recipeID + ","+ 1 + ")";
                    stmt.execute(selectquery);
                }
                //else update
                else{
                    String selectquery = "UPDATE USER_TO_RECIPE SET DID_SAVE=1 WHERE USER_ID='"+userID+"' AND RECIPE_ID='"+recipeID+"'";
                    stmt.executeUpdate(selectquery);
                }

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

    /**
     * This function queries the database and returns all of the recipes a user has marked a favorites.
     * @param userID
     * @return
     */
    public static String viewFavorites(int userID){
        //System.out.println("called");
        Connection connection = null;
        String favoritesHead = "Favorites: \n";
        String favorites = "None";
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";
        ArrayList<String> rowArray = new ArrayList<String>();

        Scanner reader = new Scanner(System.in);

        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);

            try{

                Statement stmt = connection.createStatement();
                String selectquery = "SELECT * FROM USER_TO_RECIPE JOIN RECIPES ON RECIPE_ID=RECIPES.ID WHERE USER_ID = " + userID + " AND DID_SAVE = " + 1;

                ResultSet rs = stmt.executeQuery(selectquery);
                while(rs.next()){
                    if(favorites.equals("None")) favorites = "";
                    favorites+=rs.getInt("RECIPE_ID")+". ";
                    favorites+=rs.getString("TITLE")+"\n";
                }

            }
            catch(SQLException s){
                System.out.println(s);
            }
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return favoritesHead+favorites;

    }

    public static boolean userToRecipeExists(int userID, int recipeID){
        Connection connection = null;
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);
            try {
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT RATING FROM USER_TO_RECIPE WHERE USER_ID = " + userID + " AND RECIPE_ID = " + recipeID+" LIMIT 1";
                ResultSet rs = stmt.executeQuery(selectquery);
                if(rs.next()){
                    return true;
                }
            } catch (SQLException s) {
                System.out.println(s);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        addFavorite(2,3);
        viewFavorites(2);
    }
}
