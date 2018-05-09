package edu.ithaca.recipeApp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditRecipe {

    /**
     * Asks user for input on which recipe element they would like to modify
     */
    public static void edit(){
        Connection connection = null;
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";
        String driverName = "com.mysql.jdbc.Driver";
        //ArrayList<String> rowArray = new ArrayList<String>();
        Scanner reader = new Scanner(System.in);

        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);
            try{
                System.out.println("Enter recipe id to edit:");
                System.out.print(">_");
                String currentRecipeId = reader.nextLine();
                Statement stmt = connection.createStatement();
                String getRecipe = "SELECT * FROM RECIPES WHERE ID="+currentRecipeId;
                ResultSet rs = stmt.executeQuery(getRecipe);

                String input;
                System.out.println("You are editing '" + rs.getString("title")+"'.");
                boolean editorRunning = true;
                while(editorRunning) {

                    ResultSet result;
                    Statement statement = connection.createStatement();

                    System.out.println("\nModification options: \n1)Title \n2)Ingredients \n3)Servings \n4)Calories \n5)Instructions \n6)Tags \n7)Image \n8)Author \n9)Finish");
                    System.out.println("Enter an option number:");
                    System.out.print(">_");
                    input = reader.nextLine();

                    String actual, str;
                    switch(input){
                        case "1":
                            actual = "TITLE";
                            str = "title";
                            break;
                        case "3":
                            actual = "SERVINGS";
                            str = "servings";
                            break;
                        case "4":
                            actual = "CALS_PER_SERVING";
                            str = "calories per serving";
                            break;
                        case "7":
                            actual = "IMAGE";
                            str = "image url";
                            break;
                        case "8":
                            actual = "SOURCE";
                            str = "author";
                            break;
                        default:
                            actual = "default";
                            str = "";
                            break;
                    }

                    if(!actual.equals("default")){
                        System.out.println("Enter new "+str+":");
                        System.out.print(">_");
                        String newVal = reader.nextLine();
                        String updateNewValue = "UPDATE RECIPES SET "+actual+"='"+newVal+"' WHERE ID="+currentRecipeId;
                        statement.executeUpdate(updateNewValue);
                        System.out.println("Edition submitted");

                    }else if(input.equals("2")){

                        // EDIT INGREDIENTS
                        editIngredients(currentRecipeId,statement,reader);

                        System.out.println("Edition submitted");

                    }else if(input.equals("5")){
                        String currentInstructions = "SELECT STEPS FROM RECIPES WHERE ID ="+currentRecipeId;
                        result = statement.executeQuery(currentInstructions);
                        System.out.println("\nCurrent instructions: ");
                        while(result.next()){
                            System.out.println(result.getString("STEPS"));
                        }
                        System.out.println(" ");

                        System.out.println("Enter new instructions: ");
                        System.out.println("Type 'done' on a new line when finished");
                        System.out.println("  or 'cancel' to exit without saving");

                        // EDIT INSTRUCTIONS
                        editInstructions(currentRecipeId,statement,reader);

                        System.out.println("Edition submitted");

                    }else if(input.equals("6")){


                        // EDIT TAGS
                        editTags(currentRecipeId,statement,reader);

                        System.out.println("Edition submitted");

                    }else if(input.equals("9")){
                        editorRunning = false;
                    }
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

    /**
     * Asks the user which ingredient element they would like to modify and how they would like to modify it, then changes the database accordingly
     * @param currentRecipeId The ID of the recipe that is being modified
     * @param statement The SQList statement object belonging to the current SQLite connection
     * @param reader The system input reader
     */
    private static void editIngredients(String currentRecipeId, Statement statement, Scanner reader){
        try{
            ResultSet result;
            String input;
            boolean editorRunning = true;
            while(editorRunning){
                String currentIngredients = "SELECT R.ID, I.NAME, R.QUANTITY FROM RECIPE_TO_INGREDIENT R INNER JOIN INGREDIENTS I ON R.INGREDIENT_ID=I.ID WHERE R.RECIPE_ID="+currentRecipeId;
                result = statement.executeQuery(currentIngredients);
                System.out.println("\nCurrent ingredients... ");
                while(result.next()){
                    System.out.println("  " + result.getInt("ID") + " " + result.getString("NAME") + " " + result.getString("QUANTITY"));
                }
                System.out.println(" ");

                System.out.println("Available options: \n1)Add  2)Edit  3)Remove  4)Return to edits");
                System.out.print(">_");
                input = reader.nextLine();

                if(input.equals("1")){
                    System.out.println("Provide ingredient information...");
                    System.out.println("Item Name:");
                    System.out.print(">_");
                    String item = reader.nextLine();
                    System.out.println("Item Quantity:");
                    System.out.print(">_");
                    String quantity = reader.nextLine();

                    String addIngredient = "insert into INGREDIENTS (NAME) select '" + item + "' where not exists(select * from INGREDIENTS where NAME = '" + item + "')";
                    statement.executeUpdate(addIngredient);

                    addIngredient = "select * from INGREDIENTS where NAME='"+item+"'";
                    result = statement.executeQuery(addIngredient);

                    addIngredient = "insert into RECIPE_TO_INGREDIENT (INGREDIENT_ID, QUANTITY, RECIPE_ID) values (" + result.getInt("ID") + ", '" + quantity + "'," + currentRecipeId + ")";
                    statement.executeUpdate(addIngredient);

                }else if(input.equals("2")){
                    boolean found = false;
                    while(!found){
                        System.out.println("ID of an ingredient to edit ('q' to quit):");
                        System.out.print(">_");
                        String editItem = reader.nextLine();
                        String findIngredient = "SELECT R.ID, I.NAME FROM RECIPE_TO_INGREDIENT R INNER JOIN INGREDIENTS I ON R.INGREDIENT_ID=I.ID WHERE R.RECIPE_ID="+currentRecipeId+" AND R.ID="+editItem;
                        result = statement.executeQuery(findIngredient);
                        if(editItem.equals("q") || editItem.equals("quit") || editItem.equals("Quit") || editItem.equals("QUIT")){
                            found = true;
                        }else if(result.next()){
                            System.out.println("Enter a new quantity:");
                            System.out.print(">_");
                            String quantity = reader.nextLine();
                            String editIngredientQuantity = "UPDATE RECIPE_TO_INGREDIENT SET QUANTITY='"+quantity+"' WHERE ID="+result.getInt("ID");
                            statement.executeUpdate(editIngredientQuantity);
                            found = true;
                        }else{
                            System.out.println("ID not found, please try again.");
                        }
                    }
                }else if(input.equals("3")){
                    boolean found = false;
                    while(!found){
                        System.out.println("ID of an ingredient to remove ('q' to quit):");
                        System.out.print(">_");
                        String removeItem = reader.nextLine();
                        String findIngredient = "SELECT R.ID, I.NAME FROM RECIPE_TO_INGREDIENT R INNER JOIN INGREDIENTS I ON R.INGREDIENT_ID=I.ID WHERE R.RECIPE_ID="+currentRecipeId+" AND R.ID="+removeItem;
                        result = statement.executeQuery(findIngredient);
                        if(removeItem.equals("q") || removeItem.equals("quit") || removeItem.equals("Quit") || removeItem.equals("QUIT")){
                            found = true;
                        }else if(result.next()){
                            System.out.println("Ingredient removed from recipe.");
                            String removeIngredient = "DELETE FROM RECIPE_TO_INGREDIENT WHERE ID="+result.getInt("ID");
                            statement.executeUpdate(removeIngredient);
                            found = true;
                        }else{
                            System.out.println("ID not found, please try again.");
                        }
                    }
                }else if(input.equals("4")){
                    editorRunning = false;
                }else{
                    System.out.println("Invalid option, please try again");
                }
            }
        }catch(SQLException s){
            System.out.println(s);
        }
    }

    /**
     * Asks user to enter a new set of instructions and then updates the database
     * @param currentRecipeId The ID of the recipe that is being modified
     * @param statement The SQList statement object belonging to the current SQLite connection
     * @param reader The system input reader
     */
    private static void editInstructions(String currentRecipeId, Statement statement, Scanner reader){
        try{
            StringBuilder instructions = new StringBuilder();
            String instruction = "void";
            Boolean start = true;
            while(!instruction.equals("done") && !instruction.equals("cancel")){
                System.out.print("... >_");
                instruction = reader.nextLine();
                if(!instruction.equals("done") && !instruction.equals("cancel")) {
                    if (!start) {
                        instructions.append("\n");
                    }
                    instructions.append(instruction);
                }
                start = false;
            }
            if(!instruction.equals("cancel")){
                String changeInstructions = "UPDATE RECIPES SET STEPS='"+instructions.toString()+"' WHERE ID="+currentRecipeId;
                statement.executeUpdate(changeInstructions);
            }
        }catch(SQLException s){
            System.out.println(s);
        }
    }

    /**
     * Asks user what kind of modification they would like to make to the tags, promps user to make that modification and then updates the database
     * @param currentRecipeId The ID of the recipe that is being modified
     * @param statement The SQList statement object belonging to the current SQLite connection
     * @param reader The system input reader
     */
    private static void editTags(String currentRecipeId, Statement statement, Scanner reader){
        try {
            ResultSet result;
            String input;
            boolean editorRunning = true;
            while (editorRunning) {
                String currentTags = "SELECT R.ID, T.NAME FROM RECIPE_TO_TAG R INNER JOIN TAGS T ON R.TAG_ID=T.ID WHERE R.RECIPE_ID="+currentRecipeId;
                result = statement.executeQuery(currentTags);
                System.out.println("\nCurrent tags...");
                while(result.next()){
                    System.out.println(result.getInt("ID") + "  " + result.getString("NAME"));
                }
                System.out.println(" ");
                System.out.println("Available options: \n1)Add  2)Remove  3)Return to edits");
                System.out.print(">_");
                input = reader.nextLine();

                if (input.equals("1")) {
                    System.out.println("Enter a new tag name:");
                    System.out.print(">_");
                    String tagName = reader.nextLine();

                    String tagsInsert = "insert into TAGS (NAME) select '" + tagName + "' where not exists(select * from TAGS where NAME = '" + tagName + "')";
                    statement.executeUpdate(tagsInsert);
                    tagsInsert = "select * from TAGS where NAME = '" + tagName + "'";
                    result = statement.executeQuery(tagsInsert);
                    tagsInsert = "insert into RECIPE_TO_TAG" + "(RECIPE_ID, TAG_ID)" + "values (" + currentRecipeId + ", " + result.getInt("ID") + ")";
                    statement.executeUpdate(tagsInsert);

                } else if (input.equals("2")) {
                    boolean found = false;
                    while (!found) {
                        System.out.println("Tag id you would like to remove ('q' to quit):");
                        System.out.print(">_");
                        String removeTag = reader.nextLine();
                        String findTag = "SELECT R.ID, T.NAME FROM RECIPE_TO_TAG R INNER JOIN TAGS T ON R.TAG_ID=T.ID WHERE R.RECIPE_ID=" + currentRecipeId + " AND R.ID='" + removeTag +"'";
                        result = statement.executeQuery(findTag);
                        if (removeTag.equals("q") || removeTag.equals("quit") || removeTag.equals("Quit") || removeTag.equals("QUIT")) {
                            found = true;
                        } else if (result.next()) {
                            System.out.println("Tag removed");
                            String tagRemover = "DELETE FROM RECIPE_TO_TAG WHERE ID=" + result.getInt("ID");
                            statement.executeUpdate(tagRemover);
                            found = true;
                        } else {
                            System.out.println("ID not found, please try again.");
                        }
                    }
                } else if (input.equals("3")) {
                    editorRunning = false;
                } else {
                    System.out.println("invalid option, please try again.");
                }
            }
        }catch (SQLException s){
            System.out.println(s);
        }
    }


    public static void main(String[] args){

        EditRecipe rec = new EditRecipe();
        rec.edit();

    }
}
