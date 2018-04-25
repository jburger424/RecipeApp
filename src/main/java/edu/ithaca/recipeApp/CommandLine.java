package edu.ithaca.recipeApp;

import java.io.IOException;
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
        "\nIngredient filters:");
        for (int i = 0; i < ingredientFilters.length; i++) {
            if(ingredientFilters[i]!=null){System.out.println(ingredientFilters[i]);}
        }
        System.out.println(
                        "\nAvailable commands are: \n" +
                        "1) List Recipes\n" +
                        "2) View Recipe\n" +
                        "3) Edit Recipe\n" +

                        "4) Filter Recipes\n" +
                        "5) Help\n" +
                        "6) Quit");


        while(true) {
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
                case "5":
                    help();
                    break;

                case "view":
                case "View":
                case "v":
                case "2":
                    if(cmdArray.length==1){
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        view(cmdArray[1]);
                    }
                    break;


                case "edit":
                case "Edit":
                case "e":
                case "3":
                    edit();

                case "filter":
                case "Filter":
                case "f":
                case "4":
                    if (cmdArray.length == 1) {
                        System.out.println("No arguments provided. Please try again\n");
                        break;
                    } else {
                        filter(cmdArray);
                    }
                    break;

                case "quit":
                case "Quit":
                case "q":
                case "6":
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



    public void list() throws IOException{


        //To be linked to a function for listing recipes
        System.out.println("Listing recipes...\n");
        System.out.println(
                        "Dietary filters currently applied: \n");
        for (int i = 0; i < dietFilters.length; i++) {
            if(dietFilters[i]!=null){System.out.println(dietFilters[i]);}
        }
        System.out.println(
                "\nIngredient filters currently applied: \n");
        for (int i = 0; i < ingredientFilters.length; i++) {
            if(ingredientFilters[i]!=null){System.out.println(ingredientFilters[i]);}
        }
        ListRecipes listRecipes = new ListRecipes();
        listRecipes.list_all_from_sql();
    }
    public void help(){
        System.out.println("\nHelp: \n" +
                "\nAvailable commands are: \n" +
                "1) List Recipes (Lists all recipes based on current filters)\n" +
                "2) View Recipe (Views details about an individual recipe)\n" +
                "3) Edit Recipe (Allows for editing an individual recipe)\n" +

                "4) Filter Recipes (Applies filters to the main list of recipes)\n" +
                "5) Help (Prints this menu)\n" +
                "6) Quit (Quits the program");
    }

    public void view(String title){
        //To be linked to a function to list details of a single recipe based on a title input
        System.out.println("Viewing recipe "+ title+"\n");
        while(true){
            System.out.print(">_");
            String rec = userScan.nextLine();
            String[] recArray = rec.split(" ");
            switch (recArray[0]) {
                case "Edit":
                case "edit":
                case "e":
                    edit();
                    break;

                case "Print":
                case "print":
                case "p":
                    print();
                    break;

                case "Back":
                case "back":
                case "b":
                    System.out.println(
                            "RecipeApp \n" +
                                    "Available commands are: \n" +
                                    "Help\n" +
                                    "Quit\n" +
                                    "List\n" +
                                    "View [ID]");

                    return;
                default:
                    System.out.println("Command not recognized, please try again...\n");
                    break;
            }
        }
    }
    private void edit(){
        editRecipe rec = new editRecipe();
        rec.edit();
    }

    public void print(){
        //TODO
    }

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
    }
    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        cl.run();
    }
}
