package edu.ithaca.recipeApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RateRecipe {

    //User adds a rating
    public static void addRating(int userID, int rating, int recipeID){
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
                String selectquery = "INSERT INTO USER_TO_RECIPE (USER_ID,RECIPE_ID,DID_RATE, RATING) VALUES (" + userID + "," + recipeID +"," + 1 +","+ rating + ")";
//                 " WHERE USER_ID = " + userID +"AND DID_RATE = " + 0
                stmt.executeQuery(selectquery);

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

    //AVERAGE FROM ALL USERS ***********
    public  static int getAverage(int ID) {
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
                String selectquery = "SELECT RATING FROM USER_TO_RECIPE WHERE DID_RATE = " + 1 + " AND RECIPE_ID = " + ID;
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
        if(counter==0){
            return -1;
        }
        else {
            //System.out.println(avg/counter);
            return avg / counter;
        }
    }


    //RATING FROM A SPECIFIC USER ********WORKS**********
    public static int usersAverage(int userID, int recipeID){
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
                String selectquery = "SELECT RATING FROM USER_TO_RECIPE WHERE DID_RATE = " + 1 + " AND USER_ID = " + userID + " AND RECIPE_ID = " + recipeID;
                ResultSet rs = stmt.executeQuery(selectquery);
                while (rs.next()){
                    rating=rs.getInt("RATING");
                    avg+=rating;
                    counter++;
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


    public static void main(String[] args){
        RateRecipe test = new RateRecipe();
        //test.addRating(1,10,2);
        //test.getAverage(1);
    }
}

