package edu.ithaca.recipeApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AddRecipe {

    public static Recipe addRecipe(){

        Scanner reader = new Scanner(System.in);
        Recipe recipe = new Recipe();
        List<Ingredient> ingredients= new ArrayList<>();
        StringBuilder instructions = new StringBuilder();
        List<String> tags = new ArrayList<>();
        List<String> prompts = new ArrayList<>();
        createPrompts(prompts);
        recipe.setServings("0");
        recipe.setCalsperserving("0");

        System.out.println("Follow prompt to create a new recipe...");
        System.out.println("Type these optional commands at any point:");
        System.out.println("   'cancel' to quit without saving");
        System.out.println("   'draft' to quit and continue later\n");
        System.out.println("   'skip' to skip an option");

        String input = "void";
        int prompt = 0;

        while(!input.equals("draft") && prompt < 8){
            System.out.print(prompts.get(prompt));
            switch (prompt){
                case 1:
                    input = inputIngredients(ingredients, reader);
                    if(!input.equals("skip")) {
                        recipe.setIngredients(ingredients);
                    }
                    break;
                case 4:
                    input = inputInstructions(instructions, reader);
                    if(!input.equals("skip")) {
                        recipe.setRecipe(instructions.toString());
                    }
                    break;
                case 5:
                    input = inputTags(tags, reader);
                    if(!input.equals("skip")) {
                        recipe.setTags(tags);
                    }
                    break;
                default:
                    input = reader.nextLine();
                    break;
            }
            if(input.equals("cancel"))return null;
            if(!input.equals("draft") && !input.equals("skip")){
                processInput(prompt, input, recipe);
            }
            prompt++;
        }

        insertRecipeToDatabase(recipe);

        return recipe;
    }

    public static void insertRecipeToDatabase(Recipe recipe){

        System.out.println("Recipe received...");

        System.out.println(recipe.getRecipe());

        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(10);

            String stmt = "insert into RECIPES"
                    + "(TITLE, SERVINGS, CALS_PER_SERVING, STEPS, IMAGE, SOURCE)"
                    + "values ('" + recipe.getTitle() + "', "
                    + Integer.parseInt(recipe.getServings()) + ", "
                    + Integer.parseInt(recipe.getCalsperserving()) + ", '"
                    + recipe.getRecipe() + "', '"
                    + recipe.getImage() + "', '"
                    + recipe.getSource() + "')";

            statement.executeUpdate(stmt);

            stmt = "select * from RECIPES where TITLE='"+recipe.getTitle()+"' and SOURCE='"+recipe.getSource()+"'";
            result = statement.executeQuery(stmt);
            int recipeID = result.getInt("ID");

            List<Ingredient> ingredients = recipe.getIngredients();
            int ingNum = ingredients.size();
            for(int i = 0; i < ingNum; i++){

                System.out.println("CHECKING INGREDIENTS");

                stmt = "insert into INGREDIENTS (NAME) select '"
                        + ingredients.get(i).getName()
                        + "' where not exists(select * from INGREDIENTS where NAME = '"
                        + ingredients.get(i).getName() + "')";

                statement.executeUpdate(stmt);

                stmt = "select * from INGREDIENTS where NAME = '" + ingredients.get(i).getName() + "'";
                result = statement.executeQuery(stmt);

                System.out.println("RECIPE TO INGREDIENT");

                stmt = "insert into RECIPE_TO_INGREDIENT"
                        + "(INGREDIENT_ID, QUANTITY, RECIPE_ID)"
                        + "values (" + result.getInt("ID") + ", "
                        + ingredients.get(i).getQuantity() + ","
                        + recipeID + ")";
                statement.executeUpdate(stmt);
            }

            int tagNum = recipe.getTags().size();
            for(int i = 0; i < tagNum; i++){

                System.out.println("CHECKING TAGS");

                stmt = "insert into TAGS (NAME) select '"
                        + recipe.getTags().get(i)
                        + "' where not exists(select * from TAGS where NAME = '"
                        + recipe.getTags().get(i) + "')";
                statement.executeUpdate(stmt);
                stmt = "select * from TAGS where NAME = '" + recipe.getTags().get(i) + "'";
                result = statement.executeQuery(stmt);

                System.out.println("RECIPE TO TAG");

                stmt = "insert into RECIPE_TO_TAG"
                        + "(RECIPE_ID, TAG_ID)"
                        + "values (" + recipeID + ", "
                        + result.getInt("ID") + ")";
                statement.executeUpdate(stmt);
            }
            statement.close();
            result.close();
            connection.close();
        }
        catch(SQLException se){
            System.err.println(se.getMessage());
        }
        System.out.println("Insert Complete");
    }

    public static boolean validInteger(String input){
        boolean valid = false;
        try{
            Integer.parseInt(input);
            valid = true;
        }catch(NumberFormatException ne){

        }
        return valid;
    }

    public static void processInput(int prompt, String input, Recipe recipe){
        switch (prompt){
            case 0:
                recipe.setTitle(input);
                break;
            case 2:
                recipe.setServings(input);
                break;
            case 3:
                recipe.setCalsperserving(input);
                break;
            case 6:
                recipe.setImage(input);
                break;
            case 7:
                recipe.setSource(input);
                break;
            default:
                break;
        }
    }

    public static String inputTags(List<String> tags, Scanner reader){
        String tag = "void";
        while(!tag.equals("done")) {
            System.out.print("... >_");
            tag = reader.nextLine();
            if (tag.equals("cancel") || tag.equals("draft") || tag.equals("skip")) return tag;
            if (!tag.equals("done")) {
                tags.add(tag);
            }
        }
        return tag;
    }

    public static String inputInstructions(StringBuilder instructions, Scanner reader){
        String instruction = "void";
        Boolean start = true;
        while(!instruction.equals("done")){
            System.out.print("... >_");
            instruction = reader.nextLine();
            if(!instruction.equals("done")) {
                if (!start) {
                    instructions.append("\n");
                }
                instructions.append(instruction);
            }
            if(instruction.equals("cancel") || instruction.equals("draft") || instruction.equals("skip")) return instruction;
            start = false;
        }
        return instruction;
    }

    public static String inputIngredients(List<Ingredient> ingredients, Scanner reader){
        String ingredientName = "void", quantity;
        int num = 1;
        while(!ingredientName.equals("done")){
            System.out.print("... Ingredient "+num+": >_");
            ingredientName = reader.nextLine();
            if(ingredientName.equals("cancel") || ingredientName.equals("draft") || ingredientName.equals("skip")) return ingredientName;
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName);
            if(!ingredientName.equals("done")){
                ingredients.add(ingredient);
                System.out.print("... Quantity: >_");
                quantity = reader.nextLine();
                if(quantity.equals("cancel") || quantity.equals("draft") || quantity.equals("skip")) return quantity;
                ingredient.setQuantity(quantity);
            }
            num++;
        }

        return ingredientName;
    }


    public static void createPrompts(List<String> prompts){
        prompts.add("Recipe name: >_");
        prompts.add("Ingredients (enter 'done' when completed):\n");
        prompts.add("Servings: >_");
        prompts.add("Calories per serving: >_");
        prompts.add("Instructions (enter 'done' when completed:\n");
        prompts.add("Tags (enter 'done' when completed):\n");
        prompts.add("Image path: >_");
        prompts.add("Creator: >_");
    }


    public static void main(String[] args){
        Recipe recipe = addRecipe();


    }
}
