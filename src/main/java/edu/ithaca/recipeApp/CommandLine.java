package edu.ithaca.recipeApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Isaak Hill RecipeApp 3/25/18.
 */
public class CommandLine {
    private Scanner userScan;
    public String cmd;
    public String[] cmdArray;
    public String[] dietFilters = new String[5];
    public String[] ingredientFilters = new String[5];
    public DatabaseConnect dbConnect = new DatabaseConnect();

    public CommandLine() {
        this.userScan=new Scanner(System.in);
        this.dietFilters[0]="None";
        this.ingredientFilters[0]="None";

    }


    public void run() throws IOException {

        System.out.println(
                "\nRecipeApp \n" +
                        "\nDietary filters:");
        for (int i = 0; i < dietFilters.length; i++) {
            if(dietFilters[i]!=null){System.out.println(dietFilters[i]);}
        }
        System.out.println(
        "\n\nIngredient filters:");
        for (int i = 0; i < ingredientFilters.length; i++) {
            if(ingredientFilters[i]!=null){System.out.println(ingredientFilters[i]);}
        }
        while(true) {

            System.out.println(
                        "\nAvailable commands are: \n" +
                        "1) List Recipes\n" +
                        "2) View Recipe\n" +
                        "3) Edit Recipe\n" + "4) Add Recipe\n" +

                        "5) Filter Recipes\n" +
                                "6) Print Recipe\n" +
                        "7) Help\n" +
                        "8) Quit");


            System.out.print(">_");
            cmd = userScan.nextLine();
            cmdArray = cmd.split(" ");
            switch (cmdArray[0]) {
                case "List":
                case "list":
                case "l":
                case "1":
                    list();
                    break;

                case "help":
                case "Help":
                case "h":
                case "7":
                    help();
                    break;

                case "view":
                case "View":
                case "v":
                    if(cmdArray.length==1){
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        try{
                            view(cmdArray[1]);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                case "2":
                    view();
                    break;

                case "Add":
                case "add":
                case "a":
                case "4":
                    add();
                    break;

                case "edit":
                case "Edit":
                case "e":
                case "3":
                    edit();
                    break;
                case "filter":
                case "Filter":
                case "f":
                case "5":
                    if (cmdArray.length == 1 ) {
                        filter();
                    } else {
                        filter(cmdArray);
                    }
                    break;
                case "print":
                case "Print":
                case "p":
                    if(cmdArray.length==1){
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        try{
                            print(cmdArray[1]);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                case "6":
                    print();
                    break;
                case "quit":
                case "Quit":
                case "q":
                case "8":
                    System.out.println("Thank you for using RecipeApp!\n");
                    return;
                default:
                    System.out.println("Command not recognized, please try again...\n");
                    break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                System.out.println("Really not sure how this happened...");
            }
        }
    }



    public void list() {


        //To be linked to a function for listing recipes
        System.out.println("Listing recipes...");
        System.out.println(
                        "Dietary filters currently applied:");
        for (int i = 0; i < dietFilters.length; i++) {
            if(dietFilters[i]!=null){System.out.println(dietFilters[i]);}
        }
        System.out.println(
                "\nIngredient filters currently applied:");
        for (int i = 0; i < ingredientFilters.length; i++) {
            if(ingredientFilters[i]!=null){System.out.println(ingredientFilters[i]);}
        }
        dbConnect.listRecipes();
    }
    public void add(){

    }
    public void help(){
        System.out.println("\nHelp: \n" +
                "\nAvailable commands are: \n" +
                "1) List Recipes (Lists all recipes based on current filters)\n" +
                "2) View Recipe (Views details about an individual recipe)\n" +
                "3) Edit Recipe (Allows for editing an individual recipe)\n" +
                "4) Add Recipe (Adds a new recipe)\n" +
                "5) Filter Recipes (Applies filters to the main list of recipes)\n" +
                "6) Help (Prints this menu)\n" +
                "7) Quit (Quits the program)");
    }

    public void view() throws IOException {
        list();
        System.out.println("Which recipe would you like to view?");

        while(true){
            System.out.print(">_");
            String recipe = userScan.nextLine();
            view(recipe);
            return;
        }
    }

    private void view(String ID) throws IOException{
        //To be linked to a function to list details of a single recipe based on a title input
        System.out.println("Viewing recipe "+ ID);
        dbConnect.viewRecipe(Integer.parseInt(ID));
    }
    private void edit(){
        EditRecipe rec = new EditRecipe();
        rec.edit();
    }

    public void print() throws IOException{
        list();
        System.out.println("Which recipe would you like to print?");

        while(true){
            System.out.print(">_");
            String recipe = userScan.nextLine();
            print(recipe);
            return;
        }

    }

    public void print(String ID) throws IOException{
        System.out.println("Printing recipe "+ ID);
        //TODO
    }
    public void filter(){
        System.out.println("What ingredients would you like to use? (Enter all the ingredients you would like to use, seperated by commas)\n" +
                "If you would like to clear the filters, type \"none\"");
        String ingredients = userScan.nextLine();
        if(ingredients.equals("none")){
            System.out.println("Clearing ingredient filters...");
            ingredientFilters[0]="none";
            for (int i = 1; i < ingredientFilters.length; i++) {
                ingredientFilters[i]="";
            }
            ingredients = "";
        } else {
            ingredientFilters = ingredients.split(",\\s*");
        }
        System.out.println("Do you have any dietary restrictions? If so, please list them here in the same fashion.\n" +
                "Again, you can clear the dietary filters by entering \"none\"");
        String diet = userScan.nextLine();
        if(diet.equals("none")){
            System.out.println("Clearing dietary filters...");
            dietFilters[0]="none";
            for (int i = 1; i < dietFilters.length; i++) {
                dietFilters[i]="";
            }
            diet = "";
        } else {
            dietFilters = diet.split(",\\s*");
        }
        dbConnect.setFilter(new ArrayList<>(Arrays.asList(ingredientFilters)));
    }

    /**
     * Deprecated commandline filter code. Kept in for legacy uses.
     * parses single letter arguments and following ingredients/dietary concerns
     * Updates dietFilters and ingredientFilters with the user input.
     * Clears all filters on inputting "-c"
     * @param args
     */
    public void filter(String[] args){
        if(args.length>=3) {
            switch (args[1]) {
                case "-d":
                    dietFilters = args[2].split(",");
                    break;
                case "-i":
                    ingredientFilters = args[2].split(",");
                    break;
                default:
                    System.out.println("\nNo filter provided. Available filters are " +
                            "-d: for dietary filtering,\n" +
                            "-i: for ingredient filtering, and\n" +
                            "-c: to clear filters");

                    break;

            }
            if(args.length==5) {
                switch (args[3]) {
                    case "-d":
                        dietFilters = args[4].split(",");
                        break;
                    case "-i":
                        ingredientFilters = args[4].split(",");
                        break;
                    default:
                        System.out.println("\nNo filter provided. Available filters are " +
                                "-d: for dietary filtering," +
                                "-i: for ingredient filtering, and\n" +
                                "-c: to clear filters");
                        break;

                }
            }
        } else if(args.length==2 && args[1].equals("-c")){
            System.out.println("Clearing filters...");
            dietFilters[0]="none";
            for (int i = 1; i < dietFilters.length; i++) {
                dietFilters[i]="";
            }
            ingredientFilters[0]="none";
            for (int i = 1; i < ingredientFilters.length; i++) {
                ingredientFilters[i]="";
            }
        } else {
            System.out.println("Invalid format. Please enter a valid filter, followed by a comma seperated list of arguments");
        }
        dbConnect.setFilter(new ArrayList<>(Arrays.asList(ingredientFilters)));
    }
    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        cl.run();
    }
}
