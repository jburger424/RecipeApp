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


        //        String destinationFile = "image.jpg";
//
//        BufferedImage c = ImageIO.read(imageUrl);
//        ImageIcon image = new ImageIcon(c);
//        jXImageView1.setImage(image);
//
//        saveImage(imageUrl, destinationFile);
    }

    public void getImage() throws Exception{
//        URL url = new URL(imageUrl);
//        InputStream is = url.openStream();
//        OutputStream os = new FileOutputStream(destinationFile);
//
//        byte[] b = new byte[2048];
//        int length;
//
//        while ((length = is.read(b)) != -1) {
//            os.write(b, 0, length);
//        }
//        is.close();
//        os.close();

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
        //Chicken
        else if (id ==22){
            URL url = new URL("https://www.grandmamolasses.com/wp-content/uploads/2013/04/IMG_9296-1024x641.jpg");
            img = ImageIO.read(url);
        }
        //Peach Pie
        else if (id ==23){
            URL url = new URL("https://images-gmi-pmc.edge-generalmills.com/bf18ade2-a8e8-4158-9e82-a9d23671b8f5.jpg");
            img = ImageIO.read(url);
        }

        //return img;
    }

    public void makeWindow(){
        JFrame frame = new JFrame("Recipe");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());


        JLabel titleLabel = new JLabel("<html>Title: " + title + "<br/></html>", SwingConstants.CENTER);
        JLabel calLabel = new JLabel("Calories: "+ calories + "\n");
        JLabel servLabel = new JLabel("Servings: "+ servings + "\n");

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

        img = getImage();
        JLabel jLabelImg = new JLabel();
        jLabelImg.setIcon(new ImageIcon(img));


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


        WindowDisplay wd = new WindowDisplay(123, "Chicken Parm", 400, 2, ingr, steps);
        wd.makeWindow();



    }
}
