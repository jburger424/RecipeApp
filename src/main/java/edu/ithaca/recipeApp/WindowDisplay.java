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

    public WindowDisplay(int id, String title, int calories, int servings, ArrayList<Ingredient> ingredients, String[] steps){
        this.id = id;
        this.calories = calories;
        this.servings = servings;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.img = img;
    }

    public void saveImage() throws Exception {
        Image image = null;
        try {
            URL url = new URL("http://www.mkyong.com/image/mypic.jpg");
            //image = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Image getImage() throws Exception{

        //Chicken noodle soup
        if (id ==1){
            URL url = new URL("https://hips.hearstapps.com/clv.h-cdn.co/assets/16/49/1481052520-gettyimages-91642973.jpg?crop=0.9895953757225433xw:1xh;center,top&resize=980:*");
            img = ImageIO.read(url);
        }
        //Salad
        else if (id ==2){
            URL url = new URL("http://www.healthguide.net/wp-content/uploads/2013/12/salad.jpg");
            img = ImageIO.read(url);
        }
        //Carrots
        else if (id ==22){
            URL url = new URL("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2003/9/29/0/ig1a09_roasted_carrots.jpg.rend.hgtvcom.616.462.suffix/1393645736360.jpeg");
            img = ImageIO.read(url);
        }
        //Apples
        else if (id ==23){
            URL url = new URL("http://media.foodnetwork.ca/recipetracker/dmm/G/R/Grilled_Apples_with_Spiced_Chantilly_Cream_003.jpg");
            img = ImageIO.read(url);
        }

        return img;
    }

    public void makeWindow(){
        try {
            JFrame frame = new JFrame("Recipe");
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());


            JLabel titleLabel = new JLabel("<html>Title: " + title + ". <br/></html>", SwingConstants.CENTER);
            JLabel calLabel = new JLabel("Calories: " + calories + ". \n");
            JLabel servLabel = new JLabel("Servings: " + servings + ". \n");

            String ingrLabelText = "Ingredients: ";
            for (int i = 0; i < ingredients.size(); i++) {
                ingrLabelText += ingredients.get(i) + "\n\n";
            }
            JLabel ingrLabel = new JLabel(ingrLabelText);

            String stepsLabelText = "Steps: ";
            for (int i = 0; i < steps.length; i++) {
                stepsLabelText += steps[i] + ", \n";
            }
            JLabel stepsLabel = new JLabel(stepsLabelText);

            Image image = getImage();

            JLabel jLabelImg = new JLabel();
            jLabelImg.setIcon(new ImageIcon(image.getScaledInstance(250, 250, 250)));


            panel.add(titleLabel);
            panel.add(calLabel);
            panel.add(servLabel);
            panel.add(ingrLabel);
            panel.add(stepsLabel);
            panel.add(jLabelImg);


            frame.add(panel);
            frame.setSize(600, 600);
            //frame.setLocationRelativeTo(null);
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }catch (java.lang.Exception e){
            System.out.println("java.lang.Exception caught");
        }
    }

    public static void main(String[] args) {
        ArrayList<Ingredient> ingr = new ArrayList<Ingredient>();
        Ingredient i1 = new Ingredient();
//        ingr.add("Chicken");
//        ingr.add("Cheese");
//        ingr.add("Marinara sauce");
        String [] steps = {"Cook chicken", "Add sauce", "Add cheese"};

        //String img = "http://www.geniuskitchen.com/recipe/chicken-parmesan-19135";
        Image image = null;
        try {
            URL url = new URL("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2011/8/4/1/GL0509_chicken-parmigiana_s4x3.jpg.rend.hgtvcom.616.462.suffix/1371600392952.jpeg");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        WindowDisplay wd = new WindowDisplay(22, "Chicken", 400, 2, ingr, steps);
        wd.makeWindow();

    }
}
