package edu.ithaca.recipeApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RateRecipe {

    //User adds a rating
    public void addRating(int userID, int rating, String recipeID){
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
                String selectquery = "INSERT INTO USER_TO_RECIPE (USER_ID,RECIPE_ID,DID_RATE, RATING, DID_SAVE) VALUES (" + userID + "," + recipeID +"," + 1 +","+ rating + "," + 1 + ")" +
                        " WHERE USER_ID = " + userID +"AND DID_RATE = " + 0 + "AND DID_SAVE = " + 0;
                ResultSet rs = stmt.executeQuery(selectquery);

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

    //AVERAGE FROM ALL USERS
    public int getAverage(String ID) {
        Connection connection = null;
        int avg = 0;
        int counter = 0;
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";
        ArrayList<String> rowArray = new ArrayList<String>();

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);
            try {
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT RATING FROM USER_TO_RECIPE WHERE DID_RATE = " + 1;
                ResultSet rs = stmt.executeQuery(selectquery);
                while (rs.next()){
                    counter++;
                    avg+=rs.getInt("RATING");
                }
            } catch (SQLException s) {
                System.out.println(s);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return avg/counter;
    }


    //RATING FROM A SPECIFIC USER
    public int usersAverage(String userID){
        int rating = 0;
        Connection connection = null;
        int avg = 0;
        int counter = 0;
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";
        ArrayList<String> rowArray = new ArrayList<String>();

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);
            try {
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT RATING FROM USER_TO_RECIPE WHERE DID_RATE = " + 1 + "AND USER_ID = " + userID + "AND DID_SAVE = " +1;
                ResultSet rs = stmt.executeQuery(selectquery);
                while (rs.next()){
                    rating=rs.getInt("RATING");
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
        //test.addRating(10,"1");
    }
}

