package edu.ithaca.recipeApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RateRecipe {
    public void addRating(int rating, String ID){
        Connection connection = null;

        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";

        String driverName = "com.mysql.jdbc.Driver";
        ArrayList<String> rowArray = new ArrayList<String>();

        Scanner reader = new Scanner(System.in);


        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);

            try{

//                System.out.println("Enter recipe id to rate: ");
//                System.out.print(">_");
//                int recipeId = reader.nextInt();
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT * FROM RECIPES WHERE ID="+ID;
                ResultSet rs = stmt.executeQuery(selectquery);

                while(rs.next()){
                    System.out.println("Rating: "+ rs.getInt("id") + "\t" + rs.getString("title"));
                }

//                System.out.println("What would you like to Rate? ");
//                System.out.println("TITLE, IMAGE, SOURCE, STEPS, SERVINGS, or CALS_PER_SERVING?");
//                System.out.print(">_");

//                rs = stmt.executeQuery(selectquery);
//                String col = reader.nextLine();
//                col = reader.nextLine();
//                System.out.println("CURRENT: "+rs.getString(col));

//                System.out.println("Enter new rating: ");
//                System.out.print(">_");
//                String newString = reader.nextLine();

                String editQuery = "UPDATE RECIPES SET "+"rating"+"='"+rating+"' WHERE ID="+ID;
                stmt.executeUpdate(editQuery);
                rs = stmt.executeQuery(selectquery);

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
                String selectquery = "SELECT rating FROM RECIPES WHERE ID=" + ID;
                ResultSet rs = stmt.executeQuery(selectquery);
                while (rs.next()){
                    counter++;
                    avg+=rs.getInt("rating");
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
        test.addRating(10,"1");
    }
}

