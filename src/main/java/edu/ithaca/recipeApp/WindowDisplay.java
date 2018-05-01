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
    ArrayList<String> ingredients;
    String[] steps;
    Image img;

    public WindowDisplay(int id, String title, int calories, int servings, ArrayList<String> ingredients, String[] steps, Image img){
        this.id = id;
        this.calories = calories;
        this.servings = servings;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.img = img;
    }

    public void getImage(String imageUrl) throws Exception {
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

    public void saveImage(String imageUrl, String destinationFile) throws IOException {
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
        ArrayList<String> ingr = new ArrayList<String>();
        ingr.add("Chicken");
        ingr.add("Cheese");
        ingr.add("Marinara sauce");
        String [] steps = {"Cook chicken", "Add sauce", "Add cheese"};

        //String img = "http://www.geniuskitchen.com/recipe/chicken-parmesan-19135";
        Image image = null;
        try {
            URL url = new URL("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2011/8/4/1/GL0509_chicken-parmigiana_s4x3.jpg.rend.hgtvcom.616.462.suffix/1371600392952.jpeg");
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        WindowDisplay wd = new WindowDisplay(123, "Chicken Parm", 400, 2, ingr, steps, image);
        wd.makeWindow();



    }
}
