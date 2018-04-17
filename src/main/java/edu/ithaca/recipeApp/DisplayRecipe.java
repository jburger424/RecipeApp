package edu.ithaca.recipeApp;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DisplayRecipe {

    /**
     * Connects to the database of recipes and corresponding cooking information
     */
    public static ArrayList<String> connect(String recipeTitle) {
        Connection conn = null;
        ArrayList<String> rowArray = new ArrayList<String>();

        try {
            // db parameters
            String url = "jdbc:sqlite:recipes.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String selectQuery = "SELECT * FROM `thisTable`"; // WHERE `uniqueID` = 12345";
            ResultSet rs = stmt.executeQuery(selectQuery);

            System.out.println("Connection to SQLite has been established.");

            while(rs.next()){
                if (rs.getNString(1)== recipeTitle){
                    rowArray.add(rs.getString(1));
                    rowArray.add(rs.getString(2));
                    rowArray.add(rs.getString(3));
                    rowArray.add(rs.getString(4));
                    rowArray.add(rs.getString(5));
                    rowArray.add(rs.getString(6));
                    rowArray.add(rs.getString(7));
                    rowArray.add(rs.getString(8));
                }
                //System.out.println(rowArray);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return rowArray;
    }

//    //TODO
//    //Modify this function so that it no longer has to connect to server
//    //Should just query through database to find right recipe and return all info
//    public static ArrayList<String> getAllRecipeInfo(String recipeTitle){
//        Connection connection = null;
//        String url = "jdbc:mysql://localhost:3306/";
//        String dbName = "******";
//        String driverName = "com.mysql.jdbc.Driver";
//        String userName = "abarrett";
//        String password = "abarrett1";
//        ArrayList<String> rowArray = new ArrayList<String>();
//
//        try{
//            Class.forName(driverName).newInstance();
//            connection = DriverManager.getConnection(url+dbName, userName, password);
//
//            try{
//                Statement stmt = connection.createStatement();
//                String selectquery = "SELECT * FROM `thisTable` WHERE `uniqueID` = 12345";
//                ResultSet rs = stmt.executeQuery(selectquery);
//
//
//                while(rs.next()){
//                    if (rs.getNString(1)== recipeTitle){
//                        rowArray.add(rs.getString(1));
//                        rowArray.add(rs.getString(2));
//                        rowArray.add(rs.getString(3));
//                        rowArray.add(rs.getString(4));
//                        rowArray.add(rs.getString(5));
//                        rowArray.add(rs.getString(6));
//                        rowArray.add(rs.getString(7));
//                        rowArray.add(rs.getString(8));
//                    }
//                    //System.out.println(rowArray);
//                }
//            }
//            catch(SQLException s){
//                System.out.println(s);
//            }
//            connection.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return rowArray;
//    }



    /**
     * Prints out all of the information corresponding to the proper recipe
     * @param arr all of the information for the recipe
     * @pre all elements of the array are in the proper order
     */
    public static void printAllRecipeInfo(ArrayList<String> arr){
        System.out.println("Recipe: " + arr.get(0));
        System.out.println("Dietary Restrictions: " + arr.get(1));
        System.out.println("Ingredients: " + arr.get(2));
        System.out.println("Difficulty: " + arr.get(3));
        System.out.println("Time (to prepare recipe): " + arr.get(4));
        System.out.println("Instructions: " + arr.get(5));
        System.out.println("Favorite (Y/N): " + arr.get(6));
        System.out.println("Wishlist (Y/N): " + arr.get(7));

    }

    public static void main(String[] args) {
        //connect();
        ArrayList<String> arr = connect("test");
        printAllRecipeInfo(arr);
    }
}
