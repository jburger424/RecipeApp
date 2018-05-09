package edu.ithaca.recipeApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
    private int loggedinUser;

    private CommandLine() {
        this.userScan = new Scanner(System.in);
    }


    public void viewFavorites() {
        Favorites.viewFavorites(loggedinUser);
    }


    public void rate(){
        Scanner scan = new Scanner(System.in);
        System.out.println("What recipe do you want to rate? (ID)");
        int recipeID = scan.nextInt();
        System.out.println("What rating do you want to give it? (1-10)");
        int rating = scan.nextInt();
        //System.out.println(loggedinUser);
        RateRecipe.addRating(loggedinUser,rating,recipeID);
    }

    public void viewRating() {
        Scanner scan = new Scanner(System.in);
        System.out.println("What recipe do you want to view your ratings for? (ID)");
        int recipeID = scan.nextInt();
        System.out.println(RateRecipe.usersAverage(loggedinUser, recipeID));
    }

    public void viewAvgRating() {
        Scanner scan = new Scanner(System.in);
        System.out.println("What recipe do you want to view the average rating for? (ID)");
        int recipeID = scan.nextInt();
        RateRecipe.getAverage(recipeID);
    }
    private void run() {

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
                                this.loggedinUser = dbConnect.userId;
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

    private void menu(){
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
                                "8) Rate a recipe\n" +
                                "9) View your ratings\n" +
                                "10) View average ratings\n" +
                                "11) View your favorite recipes\n" +
                        "12) Help\n" +
                        "13) Quit");


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
                case "12":
                    help();
                    break;

                case "view":
                case "View":
                case "v":
                    if (cmdArray.length == 1) {
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        view(cmdArray[1]);
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
                        print(cmdArray[1]);

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
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                case "7":
                    favorite();
                    break;

                //*******************
                case "rate":
                case "Rate":
                case "r":
                case "8":
                    rate();
                    break;
                //*******************
                case "View Ratings":
                case "View ratings":
                case "view ratings":
                case "vr":
                case "9":
                    viewRating();
                    break;
                //*******************
                //*******************
                case "View Favorites":
                case "view favorites":
                case "view Favorites":
                case "vf":
                case "11":
                    viewFavorites();
                    break;

                //*******************
                //*******************
                case "View Average":
                case "View average":
                case "view average":
                case "va":
                case "10":
                    viewAvgRating();
                    break;

                //*******************
                case "quit":
                case "Quit":
                case "q":
                case "13":
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
        System.out.println(
                "\nAvailable commands are: \n" +
                        "1) List Recipes (Lists all recipes based on current filters)\n" +
                        "2) View Recipe (Views details about an individual recipe)\n" +
                        "3) Edit Recipe (Allows for editing an individual recipe)\n"+
                        "4) Add Recipe (Adds a new recipe)\n" +
                        "5) Filter Recipes (Applies filters to the main list of recipes)\n" +
                        "6) Print Recipe (Opens a new window with the recipe details and image)\n" +
                        "7) Favorite Recipe (adds a recipe to your favorites\n" +
                        "8) Rate a recipe (rate a recipe from 1-10 on how much you liked it)\n" +
                        "9) View your ratings (view the ratings you have given a recipe)\n" +
                        "10) View average ratings (view the avergae rating for a recipe from all users)\n" +
                        "11) View your favorite recipes (view recipes in your favorites)\n" +
                        "12) Help (see details about available actions)\n" +
                        "13) Quit (quit the program)");
    }

    public void view(){
        list();
        System.out.println("Which recipe would you like to view?");

        while(true){
            System.out.print(">_");
            String recipe = userScan.nextLine();
            view(recipe);
            return;
        }
    }

    private void view(String ID){
        //To be linked to a function to list details of a single recipe based on a title input
        System.out.println("Viewing recipe "+ ID);
        dbConnect.viewRecipe(Integer.parseInt(ID));
    }
    private void edit(){
        EditRecipe.edit();
    }

    public void print(){
        list();
        System.out.println("Which recipe would you like to print?");

        while(true){
            System.out.print(">_");
            String recipe = userScan.nextLine();
            print(recipe);
            return;
        }

    }

    public void print(String ID){
        System.out.println("Printing recipe "+ ID);
        DatabaseConnect db2 = new DatabaseConnect();
        WindowDisplay wd = db2.viewRecipe(Integer.parseInt(ID));
        wd.makeWindow();
    }

    public void favorite(){
        list();
        System.out.println("Which recipe would you like to favorite?");

        while(true){
            System.out.print(">_");
            String recipe = userScan.nextLine();
            favorite(recipe);
            return;
        }

    }

    public void favorite(String ID){
        Favorites.addFavorite(loggedinUser,Integer.parseInt(ID));
        System.out.println("\n");
    }
    public void filter(){
        System.out.println("What ingredients would you like to use? (Enter all the ingredients you would like to use, seperated by commas)\n" +
                "If you would like to clear the filters, type \"none\"");
        String ingredients = userScan.nextLine();
        if(ingredients.equals("none")){
            System.out.println("Clearing ingredient filters...");
            ingredientFilters = new String[0];
        } else {
            ingredientFilters = ingredients.split(",\\s*");
        }
        System.out.println("Do you have any dietary restrictions? If so, please list them here in the same fashion.\n" +
                "Again, you can clear the dietary filters by entering \"none\"");
        String diet = userScan.nextLine();
        if(diet.equals("none")){
            System.out.println("Clearing dietary filters...");
            dietFilters = new String[0];
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
    private void filter(String[] args){
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
            ingredientFilters = new String[0];
        } else {
            System.out.println("Invalid format. Please enter a valid filter, followed by a comma separated list of arguments");
        }
        dbConnect.setFilter(new ArrayList<>(Arrays.asList(ingredientFilters)), new ArrayList<>(Arrays.asList(dietFilters)));
    }
    public static void main(String[] args){
        CommandLine cl = new CommandLine();
        cl.run();
    }
}
