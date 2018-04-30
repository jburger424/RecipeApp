package edu.ithaca.recipeApp;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class WindowDisplay {
    int id;
    int calories;
    int servings;
    String title;
    String[] ingredients;
    String[] steps;

    public WindowDisplay(){
        this.id = id;
        this.calories = calories;
        this.servings = servings;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public static void getImage(String imageUrl) throws Exception {
        imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
        String destinationFile = "image.jpg";

        saveImage(imageUrl, destinationFile);
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    public void makeWindow(){
        JFrame frame = new JFrame("Recipe");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());


        JLabel titleLabel = new JLabel("Title: "+ title);
        JLabel calLabel = new JLabel("Calories: "+ calories);
        JLabel servLabel = new JLabel("Servings: "+ servings);

        String ingrLabelText = "";
        for (int i = 0; i < ingredients.length; i++) {
            ingrLabelText += ingredients[i] + "\n";
        }
        JLabel ingrLabel = new JLabel(ingrLabelText);

        String stepsLabelText = "";
        for (int i = 0; i < ingredients.length; i++) {
            stepsLabelText += ingredients[i] + "\n";
        }
        JLabel stepsLabel = new JLabel(stepsLabelText);

        panel.add(titleLabel);
        panel.add(calLabel);
        panel.add(servLabel);
        panel.add(ingrLabel);
        panel.add(stepsLabel);



        frame.add(panel);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void getDatabaseInfo(int ID){
//        ArrayList<Ingredient> ingredients = new ArrayList<>();
        // load the sqlite-JDBC driver using the current class loader
        try{
            Class.forName("org.sqlite.JDBC");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            // create a database connection
            int ingrCount = 0;
            connection = DriverManager.getConnection("jdbc:sqlite:src/test/resources/db/recipes.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from RECIPE_TO_INGREDIENT WHERE RECIPE_ID="+ID);
            while(rs.next()) {
                int ingredID = rs.getInt("INGREDIENT_ID");
                String ingredQuant = rs.getString("QUANTITY");
                Ingredient tempIngred = new Ingredient();
                tempIngred.setID(ingredID);
                tempIngred.setQuantity(ingredQuant);
                ingrCount = Integer.parseInt(ingredQuant);
                ingredients[ingrCount-1] = tempIngred.toString();
            }
            for (int i = 0; i < ingrCount; i++){
                //rs = statement.executeQuery("select * from INGREDIENTS WHERE ID="+Ingredient.getID());
                //i.setName(rs.getString("NAME"));
            }
            rs = statement.executeQuery("select * from RECIPES WHERE ID="+ID);
            while(rs.next())
            {

                title = rs.getString("title");
//                System.out.println("Ingredients:");
//                for(Ingredient i: ingredients){
//                    System.out.println("\t"+i.toString());
//                }
                ingredients = rs.getString("Ingredient").split("\\r?\\n|\\r");
                servings = rs.getInt("SERVINGS");
                calories = rs.getInt("CALS_PER_SERVING");
                steps = rs.getString("steps").split("\\r?\\n|\\r");
//                for(int i = 0; i < steps.length; i++){
//                    steps[i] = ("\t"+);
//                }
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }


    }

    public void main(String[] args) {
        makeWindow();
    }
}
