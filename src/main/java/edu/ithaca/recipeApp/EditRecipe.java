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

                    System.out.println("\nWhat would you like to edit? (title, ingredients, servings, calories, instructions, tags, image, author)");
                    System.out.println("Enter 'done' to exit");
                    System.out.print(">_");
                    input = reader.nextLine();

                    String actual;
                    switch(input){
                        case "title":
                            actual = "TITLE";
                            break;
                        case "servings":
                            actual = "SERVINGS";
                            break;
                        case "calories":
                            actual = "CALS_PER_SERVING";
                            break;
                        case "image":
                            actual = "IMAGE";
                            break;
                        case "author":
                            actual = "SOURCE";
                            break;
                        default:
                            actual = "default";
                            break;
                    }

                    if(!actual.equals("default")){
                        System.out.print("Enter new "+input+": >_");
                        String newVal = reader.nextLine();
                        String editQuery = "UPDATE RECIPES SET "+actual+"='"+newVal+"' WHERE ID="+recipeId;
                        statement.executeUpdate(editQuery);
                        System.out.println("Edition submitted");

                    }else if(input.equals("ingredients")){
                        String getIngreds = "SELECT R.ID, I.NAME, R.QUANTITY FROM RECIPE_TO_INGREDIENT R INNER JOIN INGREDIENTS I ON R.INGREDIENT_ID=I.ID WHERE R.RECIPE_ID="+recipeId;
                        result = statement.executeQuery(getIngreds);
                        while(result.next()){
                            System.out.println(result.getInt("ID") + ", " + result.getString("NAME") + ", " + result.getString("QUANTITY"));
                        }
                        System.out.println("Enter 'done' when finished");
                        List<Ingredient> ingredients = new ArrayList<>();
                        AddRecipe.inputIngredients(ingredients,reader);
                        String delIng = "DELETE FROM RECIPE_TO_INGREDIENT WHERE RECIPE_ID="+recipeId;
                        statement.executeQuery(delIng);
                        int ingNum = ingredients.size();
                        for(int i = 0; i < ingNum; i++) {
                            String ins = "insert into INGREDIENTS (NAME) select '"
                                    + ingredients.get(i).getName()
                                    + "' where not exists(select * from INGREDIENTS where NAME = '"
                                    + ingredients.get(i).getName() + "')";

                            statement.executeUpdate(ins);
                            ins = "select * from INGREDIENTS where NAME = '" + ingredients.get(i).getName() + "'";
                            result = statement.executeQuery(ins);
                            System.out.println("RECIPE TO INGREDIENT");
                            ins = "insert into RECIPE_TO_INGREDIENT"
                                    + "(INGREDIENT_ID, QUANTITY, RECIPE_ID)"
                                    + "values (" + result.getInt("ID") + ", "
                                    + ingredients.get(i).getQuantity() + ","
                                    + recipeId + ")";
                            statement.executeUpdate(ins);

                        }
                    }


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
