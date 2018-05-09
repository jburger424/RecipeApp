package edu.ithaca.recipeApp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class rateRecipesTest {

    @Test
    void userRating() {
        RateRecipe.addRating(3,1,8);
        assertEquals(1,RateRecipe.getUsersRating(3,8));
    }
    @Test
    void avgRating(){
        assertEquals(1.0,RateRecipe.getAverage(8));
    }



}