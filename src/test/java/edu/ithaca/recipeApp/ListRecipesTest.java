package edu.ithaca.recipeApp;

import edu.ithaca.recipeApp.ListRecipes;
import edu.ithaca.recipeApp.Recipe;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListRecipesTest {

    @Test
    void get_recipes() throws IOException{
        ListRecipes listRecipes = new ListRecipes();
        List<Recipe> recipes = listRecipes.get_recipes();

        assertEquals(recipes.get(0).getId(), "1");
        assertEquals(recipes.get(0).getTitle(), "Macaroni and Cheese");

        assertEquals(recipes.get(2).getId(), "3");
        assertEquals(recipes.get(2).getTitle(), "Quiche");

    }
}