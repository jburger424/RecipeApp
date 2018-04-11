package edu.ithaca.recipeApp;

import java.io.IOException;
import java.util.Scanner;

/**
 * Isaak Hill RecipeApp 3/25/18.
 */
public class CommandLine {
    private Scanner userScan;

    public CommandLine() {
        this.userScan=new Scanner(System.in);
    }


    public void run() throws IOException{

        System.out.println(
                "\nRecipeApp \n" +
                        "Available commands are: \n" +
                        "Help\n" +
                        "Quit\n" +
                        "List\n" +
                        "View [ID]\n" +
                        "Filter [Args]");

        while(true) {
            System.out.print(">_");
            String cmd = userScan.nextLine();
            String[] cmdArray = cmd.split(" ");
            switch (cmdArray[0]) {
                case "List":
                case "list":
                case "l":
                    list();
                    break;
                case "help":
                case "Help":
                case "h":
                    help();
                    break;
                case "view":
                case "View":
                case "v":
                    if(cmdArray.length==1){
                        System.out.println("No Recipe ID entered, please try again\n");
                        break;
                    } else {
                        view(cmdArray[1]);
                    }
                    break;
                case "filter":
                case "Filter":
                case "f":
                    if (cmdArray.length == 1) {
                        System.out.println("No arguments provided. Please try again\n");
                        break;
                    } else {
                        filter(cmdArray);
                    }
                case "quit":
                case "Quit":
                case "q":
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


    private void list() throws IOException{

        //To be linked to a function for listing recipes
        System.out.println("Listing recipes...\n");
        ListRecipes listRecipes = new ListRecipes();
        listRecipes.list_all();
    }
    private void help(){
        System.out.println("\nHelp: \n" +
                "The help command prints a menu of other commands and what they do \n" +
                "List: Lists available rooms\n" +
                "View [ID]: Views the details of the room numbered [ID]\n" +
                "Quit: Quits the program\n\n");
    }

    private void view(String title){
        //To be linked to a function to list details of a single recipe based on a title input
        System.out.println("Viewing recipe "+ title+"\n");
        System.out.println("Available commands are:\n" +
                "Edit\n" +
                "Print\n" +
                "Back\n");
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

    public void edit(){

    }
    public void print(){

    }

    public void filter(String[] args){
        
    }
    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        cl.run();
    }
}
