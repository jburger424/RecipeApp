package edu.ithaca.recipeApp;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    ArrayList<Ingredient> ingredients;
    String[] steps;
    Image img;
    String imgURL;

    public WindowDisplay(int id, String title, int calories, int servings, ArrayList<Ingredient> ingredients, String[] steps, String imgURL) {
        this.id = id; //identification number in database
        this.calories = calories; //calories per serving
        this.servings = servings;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.img = img;
        this.imgURL = imgURL;
    }

    /**
     * Takes image URL from database and gets an image from that URL
     * If image cannot be found in URL, a default image will be assigned to the recipe
     * @return returns image associated to corresponding ID
     * @throws Exception
     */
    public Image getImage() throws Exception{
        try{
            URL url = new URL(imgURL);
            img = ImageIO.read(url);
        }
        catch(Exception e){
            URL url = new URL("https://hips.hearstapps.com/clv.h-cdn.co/assets/16/49/1481052520-gettyimages-91642973.jpg?crop=0.9895953757225433xw:1xh;center,top&resize=980:*");
            img = ImageIO.read(url);
        }

        return img;
    }

    /**
     * Makes a JFrame window that displays all of the information associated to the recipe,
     * as well as an image of that food
     */
    public void makeWindow(){
        try {
            JFrame frame = new JFrame("Recipe");
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


            JLabel titleLabel = new JLabel("<html>Title: " + title + ". <br/></html>", SwingConstants.CENTER);
            JLabel calLabel = new JLabel("<html>Calories: " + calories + ". <br/></html>", SwingConstants.CENTER);
            JLabel servLabel = new JLabel("<html>Servings: " + servings + ". <br/></html>", SwingConstants.CENTER);

            String ingrLabelText = "Ingredients: ";
            for (int i = 0; i < ingredients.size(); i++) {
                ingrLabelText += ingredients.get(i) + ", ";
            }
            JLabel ingrLabel = new JLabel("<html>" + ingrLabelText + "<br/></html>", SwingConstants.CENTER);

            String stepsLabelText = "Steps: ";
            for (int i = 0; i < steps.length; i++) {
                stepsLabelText += steps[i] + ", ";
            }
            JLabel stepsLabel = new JLabel("<html>" + stepsLabelText + "<br/></html>", SwingConstants.CENTER);

            Image image = getImage();

            JLabel jLabelImg = new JLabel(BorderLayout.CENTER);
            jLabelImg.setIcon(new ImageIcon(image.getScaledInstance(250, 250, 250), BorderLayout.CENTER));


            panel.add(titleLabel);
            panel.add(calLabel);
            panel.add(servLabel);
            panel.add(ingrLabel);
            panel.add(stepsLabel);
            panel.add(jLabelImg);


            frame.add(panel, BorderLayout.CENTER);
            frame.setSize(600, 600);
            frame.setVisible(true);
        }catch (java.lang.Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Used for testing
     * Unlike JUnit testing, this main class will make a JFrame window for manual testing
     * Helpful so you don't have to go through entire command line to test a window
     * @param args
     */
    public static void main(String[] args) {
        ArrayList<Ingredient> ingr = new ArrayList<Ingredient>();
        Ingredient i1 = new Ingredient();
        String [] steps = {"Cook chicken", "Add sauce", "Add cheese"};

        String img = "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2011/8/4/1/GL0509_chicken-parmigiana_s4x3.jpg.rend.hgtvcom.616.462.suffix/1371600392952.jpeg";


        WindowDisplay wd = new WindowDisplay(123, "Chicken", 400, 2, ingr, steps, img);
        wd.makeWindow();

    }
}
