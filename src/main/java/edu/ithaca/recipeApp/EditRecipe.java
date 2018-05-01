package edu.ithaca.recipeApp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditRecipe {

    public void edit(){
        Connection connection = null;
        String url = "jdbc:sqlite:src/test/resources/db/recipes.db";
        String driverName = "com.mysql.jdbc.Driver";
        ArrayList<String> rowArray = new ArrayList<String>();
        Scanner reader = new Scanner(System.in);

        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url);
            try{
                System.out.print("Enter recipe id to edit: >_");
                String recipeId = reader.nextLine();
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT * FROM RECIPES WHERE ID="+recipeId;
                ResultSet rs = stmt.executeQuery(selectquery);

                String input = "void";
                System.out.println("You are editing " + rs.getString("title"));
                while(!input.equals("done")) {

                    ResultSet result;
                    Statement statement = connection.createStatement();

                    System.out.println("\nModification options: \n1)Title \n2)Ingredients \n3)Servings \n4)Calories \n5)Instructions \n6)Tags \n7)Image \n8)Author \n9)Finish");
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
                        System.out.print("Enter new "+str+": >_");
                        String newVal = reader.nextLine();
                        String editQuery = "UPDATE RECIPES SET "+actual+"='"+newVal+"' WHERE ID="+recipeId;
                        statement.executeUpdate(editQuery);
                        System.out.println("Edition submitted");

                    }else if(input.equals("2")){
                        String getIngreds = "SELECT R.ID, I.NAME, R.QUANTITY FROM RECIPE_TO_INGREDIENT R INNER JOIN INGREDIENTS I ON R.INGREDIENT_ID=I.ID WHERE R.RECIPE_ID="+recipeId;
                        result = statement.executeQuery(getIngreds);
                        while(result.next()){
                            System.out.println(result.getInt("ID") + ", " + result.getString("NAME") + ", " + result.getString("QUANTITY"));
                        }


                        String cont = "y";
                        while(cont.equals("y")){
                            System.out.println("Available options: \n1)Add\n2)Edit\n3)Remove\n4)Return to edits");
                            System.out.print("What would you like to do?: ");
                            input = reader.nextLine();
                            if(input.equals("1")){
                                System.out.println("New ingredient");
                                System.out.print("Item name: ");
                                String item = reader.nextLine();
                                System.out.print("Item Quantity: ");
                                String quantity = reader.nextLine();

                                String addIngredient = "insert into INGREDIENTS (NAME) select '" + item + "' where not exists(select * from INGREDIENTS where NAME = '" + item + "')";
                                statement.executeUpdate(addIngredient);

                                addIngredient = "select * from INGREDIENTS where NAME='"+item+"'";
                                result = statement.executeQuery(addIngredient);

                                addIngredient = "insert into RECIPE_TO_INGREDIENT (INGREDIENT_ID, QUANTITY, RECIPE_ID) values (" + result.getInt("ID") + ", '" + quantity + "'," + recipeId + ")";
                                statement.executeUpdate(addIngredient);

                            }else if(input.equals("2")){
                                System.out.print("Enter the ingredient ID you would like to change: ");
                                boolean found = false;
                                while(!found){
                                    String editItem = reader.nextLine();
                                    String findIngredient = "SELECT R.ID, I.NAME FROM RECIPE_TO_INGREDIENT R INNER JOIN INGREDIENTS I ON R.INGREDIENT_ID=I.ID WHERE R.RECIPE_ID="+recipeId+" AND R.ID="+editItem;
                                    result = statement.executeQuery(findIngredient);
                                    if(editItem.equals("q")){
                                        found = true;
                                    }else if(result.next()){
                                        System.out.print("Enter a new quantity: ");
                                        String quantity = reader.nextLine();
                                        String editIngredientQuantity = "UPDATE RECIPE_TO_INGREDIENT SET QUANTITY='"+quantity+"' WHERE ID="+result.getInt("ID");
                                        statement.executeUpdate(editIngredientQuantity);
                                        found = true;
                                    }else{
                                        System.out.print("ID not found, please try again ('q' to cancel): ");
                                    }

                                }
                            }else if(input.equals("3")){

                                System.out.print("Enter the ingredient ID you would like to remove: ");
                                boolean found = false;
                                while(!found){
                                    String removeItem = reader.nextLine();
                                    String findIngredient = "SELECT R.ID, I.NAME FROM RECIPE_TO_INGREDIENT R INNER JOIN INGREDIENTS I ON R.INGREDIENT_ID=I.ID WHERE R.RECIPE_ID="+recipeId+" AND R.ID="+removeItem;
                                    result = statement.executeQuery(findIngredient);
                                    if(removeItem.equals("q")){
                                        found = true;
                                    }else if(result.next()){
                                        System.out.println("Item removed");
                                        String removeIngredient = "DELETE FROM RECIPE_TO_INGREDIENT WHERE ID="+result.getInt("ID");
                                        statement.executeUpdate(removeIngredient);
                                        found = true;
                                    }else{
                                        System.out.print("ID not found, please try again ('q' to cancel): ");
                                    }
                                }
                            }else if(input.equals("4")){
                                cont = "n";
                            }else{
                                System.out.println("invalid option, please try again");
                            }
                            if(!input.equals("4")){
                                System.out.print("Make another modification? (y or n): ");
                                cont = reader.nextLine();
                            }
                        }
                        System.out.println("Edition submitted");

                    }else if(input.equals("5")){
                        System.out.println("Enter new instructions, when you are finished type 'done' on a new line");
                        System.out.println(("Type 'cancel' to undo changes"));
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
                            String changeInstructions = "UPDATE RECIPES SET STEPS='"+instructions.toString()+"' WHERE ID="+recipeId;
                            statement.executeUpdate(changeInstructions);
                        }
                        System.out.println("Edition submitted");
                    }else if(input.equals("6")){
                        String getTags = "SELECT R.ID, T.NAME FROM RECIPE_TO_TAG R INNER JOIN TAGS T ON R.TAG_ID=T.ID WHERE R.RECIPE_ID="+recipeId;
                        result = statement.executeQuery(getTags);
                        System.out.println("Current Tags...");
                        while(result.next()){
                            System.out.println(result.getInt("ID") + ", " + result.getString("NAME"));
                        }

                        String cont = "y";
                        while(cont.equals("y")) {
                            System.out.println("Available options: \n1)Add\n2)Remove\n3)Return to edits");
                            System.out.print("What would you like to do?: ");
                            input = reader.nextLine();
                            if (input.equals("1")) {
                                System.out.print("Enter a new tag name: ");
                                String tagName = reader.nextLine();

                                String tagsInsert = "insert into TAGS (NAME) select '" + tagName + "' where not exists(select * from TAGS where NAME = '" + tagName + "')";
                                statement.executeUpdate(tagsInsert);
                                tagsInsert = "select * from TAGS where NAME = '" + tagName + "'";
                                result = statement.executeQuery(tagsInsert);

                                tagsInsert = "insert into RECIPE_TO_TAG" + "(RECIPE_ID, TAG_ID)" + "values (" + recipeId + ", " + result.getInt("ID") + ")";
                                statement.executeUpdate(tagsInsert);

                            } else if (input.equals("2")) {
                                System.out.print("Enter the tag ID you would like to remove: ");
                                boolean found = false;
                                while(!found){
                                    String removeTag = reader.nextLine();
                                    String findTag = "SELECT R.ID, T.NAME FROM RECIPE_TO_TAG R INNER JOIN TAGS T ON R.TAG_ID=T.ID WHERE R.RECIPE_ID="+recipeId+" AND R.ID="+removeTag;
                                    result = statement.executeQuery(findTag);
                                    if(removeTag.equals("q")){
                                        found = true;
                                    }else if(result.next()){
                                        System.out.println("Tag removed");
                                        String tagRemover = "DELETE FROM RECIPE_TO_TAG WHERE ID="+result.getInt("ID");
                                        statement.executeUpdate(tagRemover);
                                        found = true;
                                    }else{
                                        System.out.print("ID not found, please try again ('q' to cancel): ");
                                    }

                                }


                            } else if (input.equals("3")) {
                                cont = "n";
                            } else {

                                System.out.println("invalid option, please try again");

                            }
                            if (!input.equals("3")) {
                                System.out.print("Make another modification? (y or n): ");
                                cont = reader.nextLine();
                            }
                        }
                    }
                    System.out.println("Edition submitted");

                }


                /*
                rs = stmt.executeQuery(selectquery);
                String col = reader.nextLine();
                col = reader.nextLine();
                System.out.println("CURRENT: "+rs.getString(col));

                System.out.println("Enter new value: ");
                System.out.print(">_");
                String newString = reader.nextLine();
                String editQuery = "UPDATE RECIPES SET "+col+"='"+newString+"' WHERE ID="+recipeId;
                stmt.executeUpdate(editQuery);
                rs = stmt.executeQuery(selectquery);
                System.out.println("NEW: "+rs.getString(col));
                */

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


    public static void main(String[] args){

        EditRecipe rec = new EditRecipe();
        rec.edit();

    }
}
