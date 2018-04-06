package edu.ithaca.recipeApp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListRecipes {

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



    }


    public static void main(String[] args)throws IOException{
        System.out.println("Hello World");
        ListRecipes listRecipes = new ListRecipes();
        listRecipes.list_all();


    }
}
