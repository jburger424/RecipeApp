package edu.ithaca.recipeApp;

import org.junit.jupiter.api.Test;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class AddRecipeTest {

    @Test
    void insertRecipeToDatabase() {

        Recipe recipe = new Recipe();
        recipe.setTitle("Test recipe 111");
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient testIngredient = new Ingredient();
        testIngredient.setName("test ingredient 1");
        testIngredient.setQuantity("test ingredient quantity 1");
        ingredients.add(testIngredient);
        recipe.setIngredients(ingredients);
        recipe.setServings("4");
        recipe.setCalsperserving("5");
        recipe.setRecipe("test recipe test");
        List<String> tags = new ArrayList<>();
        tags.add("test tag 1");
        recipe.setTags(tags);
        recipe.setSource("test owner");
        recipe.setImage("test src");

        AddRecipe.insertRecipeToDatabase(recipe);

        try{
            Connection connection;
            Statement statement;
            connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(10);
            ResultSet result;


            String que = "Select * from recipes where TITLE='Test recipe 111'";
            result = statement.executeQuery(que);
            assertEquals(result.getString("SOURCE"),"test owner");

            String del = "Delete from recipes where TITLE='Test recipe 111'";
            statement.executeUpdate(del);


        }catch(SQLException s){
            System.out.println(s);
        }


    }
}