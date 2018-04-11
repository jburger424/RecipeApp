package edu.ithaca.recipeApp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListRecipes {

    public void list_all_from_sql(){
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "******";
        String driverName = "com.mysql.jdbc.Driver";
        String userName = "abarrett";
        String password = "abarrett1";
        ArrayList<String> rowArray = new ArrayList<String>();

        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url+dbName, userName, password);

            try{
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT * FROM `thisTable'";
                ResultSet rs = stmt.executeQuery(selectquery);


                while(rs.next()){
                    System.out.println(rs.getInt("id") + "\t" + rs.getString("title"));
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

    public List<Recipe> get_recipes() throws IOException{

        File file = new File("src/main/resources/recipe.json");
        System.out.println(file.exists());

        ObjectMapper objectMapper = new ObjectMapper();

        Cookbook cookbook = objectMapper.readValue(file, Cookbook.class);

        return cookbook.getRecipes();
    }

    public void list_all() throws IOException{

        List<Recipe> recipes= get_recipes();

        for(int i = 0; i < recipes.size(); i++){
            System.out.println("ID: " + recipes.get(i).getId() + " Name: " + recipes.get(i).getTitle());
        }
        System.out.println('\n');



    }


    public static void main(String[] args)throws IOException{
        System.out.println("Hello World");
        ListRecipes listRecipes = new ListRecipes();
        listRecipes.list_all();


    }
}
