package edu.ithaca.recipeApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RateRecipe {

    /**
     * adds a rating from a specific user
     * @param userID
     * @param rating
     * @param recipeID
     */
    public static void addRating(int userID, int rating, int recipeID){
        int currentRating = getUsersRating(userID, recipeID);
        Connection connection = null;

        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";
        ArrayList<String> rowArray = new ArrayList<String>();

        Scanner reader = new Scanner(System.in);

        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);

            try{

                Statement stmt = connection.createStatement();
                //if it doesn't exist in db, insert
                if(currentRating == -1){
                    String selectquery = "INSERT INTO USER_TO_RECIPE (USER_ID,RECIPE_ID,DID_RATE, RATING) VALUES (" + userID + "," + recipeID +"," + 1 +","+ rating + ")";
                    stmt.execute(selectquery);
                }
                //else update
                else{
                    String selectquery = "UPDATE USER_TO_RECIPE SET RATING="+rating+", DID_RATE=1 WHERE USER_ID='"+userID+"' AND RECIPE_ID='"+recipeID+"'";
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
     * gets the average rating for a recipe
     * @param recipeID
     * @return
     */
    public static float getAverage(int recipeID) {
        Connection connection = null;
        float avg = -1;
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";
        ArrayList<String> rowArray = new ArrayList<String>();

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);
            try {
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT avg(RATING) as avg_rating FROM USER_TO_RECIPE WHERE DID_RATE = " + 1 + " AND RECIPE_ID = " + recipeID;
                ResultSet rs = stmt.executeQuery(selectquery);
                if (rs.next()){
                    avg=rs.getFloat("avg_rating");
                }
            } catch (SQLException s) {
                System.out.println(s);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avg;
    }

    /**
     * gets users rating for a recipe
     * @param userID
     * @param recipeID
     * @return
     */
    public static int getUsersRating(int userID, int recipeID){
        int rating = -1;
        Connection connection = null;
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);
            try {
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT RATING FROM USER_TO_RECIPE WHERE DID_RATE = " + 1 + " AND USER_ID = " + userID + " AND RECIPE_ID = " + recipeID+" LIMIT 1";
                ResultSet rs = stmt.executeQuery(selectquery);
                if(rs.next()){
                    rating = rs.getInt("RATING");
                }
            } catch (SQLException s) {
                System.out.println(s);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rating;
    }


    public static void main(String[] args){
        RateRecipe test = new RateRecipe();
        //test.addRating(1,10,2);
        //test.getAverage(1);
    }
}

