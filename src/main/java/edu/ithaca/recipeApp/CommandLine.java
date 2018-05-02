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
    private String cmd;
    private String loginCmd;
    private String username;
    private String password;
    private String[] cmdArray;
    private String[] dietFilters = new String[0];
    private String[] ingredientFilters = new String[0];
    private DatabaseConnect dbConnect = new DatabaseConnect();
    private boolean loggedin = false;

    private CommandLine() {
        this.userScan = new Scanner(System.in);
    }


    public void run() throws IOException {

        while (true) {
            System.out.println(
                    "Welcome to RecipeApp\n" +
                            "1) Log In\n" +
                            "2) Create Account");
            System.out.print(">_");
            loginCmd = userScan.nextLine();
            switch (loginCmd) {
                case "1":
                        System.out.println("Enter username:");
                        System.out.print(">_");
                        username = userScan.nextLine();
                        if (dbConnect.userExists(username)) {
                            System.out.println("Enter password:");
                            System.out.print(">_");
                            password = userScan.nextLine();
                            if (dbConnect.logInUser(username, password)) {
                                this.loggedin = true;
                                menu();
                                break;
                            } else {
                                System.out.println("Username and password do not match");
                                break;
                            }
                        } else {
                            System.out.println("Username does not exist");
                            break;
                        }

                case "2":
                    System.out.println("Enter username:");
                    System.out.print(">_");
                    username = userScan.nextLine();
                    System.out.println("Enter password:");
                    System.out.print(">_");
                    password = userScan.nextLine();
                    dbConnect.addUser(username, password);
                    break;
                default:
                    System.out.println("Command not recognized, please try again...\n");
                    break;
            }
        }
    }

    public void menu() throws IOException {
        System.out.println(
                "\nRecipeApp \n" +
                        "\nDietary filters:");
        for (int i = 0; i < dietFilters.length; i++) {
            if (dietFilters[i] != null) {
                System.out.println(dietFilters[i]);
            }
        }
        System.out.println(
                "\n\nIngredient filters:");
        for (int i = 0; i < ingredientFilters.length; i++) {
            if (ingredientFilters[i] != null) {
                System.out.println(ingredientFilters[i]);
            }
        }

        while (true) {

            System.out.println(
                    "\nAvailable commands are: \n" +
                            "1) List Recipes\n" +
                            "2) View Recipe\n" +
                            "3) Edit Recipe\n" + "4) Add Recipe\n" +

                            "5) Filter Recipes\n" +
                            "6) Print Recipe\n" +
                            "7) Favorite Recipe\n" +
                            "8) Help\n" +
                            "9) Quit");


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
                case "8":
                    help();
                    break;

                case "view":
                case "View":
                case "v":
                    if (cmdArray.length == 1) {
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        try {
                            view(cmdArray[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                case "2":
                    try {
                        view();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    if (cmdArray.length == 1) {
                        filter();
                    } else {
                        filter(cmdArray);
                    }
                    break;
                case "print":
                case "Print":
                case "p":
                    if (cmdArray.length == 1) {
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        try {
                            print(cmdArray[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                case "6":
                    print();
                    break;

                case "favorite":
                case "Favorite":
                case "fav":
                    if (cmdArray.length == 1) {
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        try {
                            favorite(cmdArray[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                case "7":
                    favorite();
                    break;

                case "quit":
                case "Quit":
                case "q":
                case "9":
                    System.out.println("Thank you for using RecipeApp!\n");
                    this.loggedin=false;
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
        if (dietFilters.length == 0) {
            System.out.println("None");
        }
        for (int i = 0; i < dietFilters.length; i++) {
            if(dietFilters[i]!=null){System.out.println(dietFilters[i]);}
        }
        System.out.println(
                "\nIngredient filters currently applied:");
        if (ingredientFilters.length == 0) {
            System.out.println("None");
        }
        for (int i = 0; i < ingredientFilters.length; i++) {
            if(ingredientFilters[i]!=null){System.out.println(ingredientFilters[i]);}
        }
        dbConnect.listRecipes();
    }
    public void add(){
        AddRecipe.addRecipe();
    }
    public void help(){
        System.out.println("\nHelp: \n" +
                "\nAvailable commands are: \n" +
                "1) List Recipes (Lists all recipes based on current filters)\n" +
                "2) View Recipe (Views details about an individual recipe)\n" +
                "3) Edit Recipe (Allows for editing an individual recipe)\n" +
                "4) Add Recipe (Adds a new recipe)\n" +
                "5) Filter Recipes (Applies filters to the main list of recipes)\n" +
                "6) Print Recipe (Opens a new window with the recipe details and image)\n" +
                "7) Favorite Recipe (Adds recipe to favorites)" +
                "8) Help (Prints this menu)\n" +
                "9) Quit (Quits the program)");
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
        EditRecipe.edit();
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
        DatabaseConnect db2 = new DatabaseConnect();
        WindowDisplay wd = db2.viewRecipe(Integer.parseInt(ID));
        wd.makeWindow();
    }

    public void favorite() throws IOException{
        list();
        System.out.println("Which recipe would you like to favorite?");

        while(true){
            System.out.print(">_");
            String recipe = userScan.nextLine();
            favorite(recipe);
            return;
        }

    }

    public void favorite(String ID) throws IOException{
        System.out.println("Recipe "+ ID + " added to favorites");
        //TODO
    }
    public void filter(){
        System.out.println("What ingredients would you like to use? (Enter all the ingredients you would like to use, seperated by commas)\n" +
                "If you would like to clear the filters, type \"none\"");
        String ingredients = userScan.nextLine();
        if(ingredients.equals("none")){
            System.out.println("Clearing ingredient filters...");
            ingredientFilters = new String[0];
            for (int i = 1; i < ingredientFilters.length; i++) {
                ingredientFilters[i]="";
            }
        } else {
            ingredientFilters = ingredients.split(",\\s*");
        }
        System.out.println("Do you have any dietary restrictions? If so, please list them here in the same fashion.\n" +
                "Again, you can clear the dietary filters by entering \"none\"");
        String diet = userScan.nextLine();
        if(diet.equals("none")){
            System.out.println("Clearing dietary filters...");
            dietFilters = new String[0];
            for (int i = 1; i < dietFilters.length; i++) {
                dietFilters[i]="";
            }
        } else {
            dietFilters = diet.split(",\\s*");
        }
        dbConnect.setFilter(new ArrayList<>(Arrays.asList(ingredientFilters)), new ArrayList<>(Arrays.asList(dietFilters)));
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
            dietFilters = new String[0];
            for (int i = 1; i < dietFilters.length; i++) {
                dietFilters[i]="";
            }
            ingredientFilters = new String[0];
            for (int i = 1; i < ingredientFilters.length; i++) {
                ingredientFilters[i]="";
            }
        } else {
            System.out.println("Invalid format. Please enter a valid filter, followed by a comma seperated list of arguments");
        }
        dbConnect.setFilter(new ArrayList<>(Arrays.asList(ingredientFilters)), new ArrayList<>(Arrays.asList(dietFilters)));
    }
    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        cl.run();
    }
}
