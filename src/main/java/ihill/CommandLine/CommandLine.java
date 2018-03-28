package ihill.CommandLine;

import java.util.Scanner;

/**
 * Isaak Hill RecipeApp 3/25/18.
 */
public class CommandLine {
    private Scanner userScan;

    public CommandLine() {
        this.userScan=new Scanner(System.in);
    }

    public void run(){
        while(true) {
            System.out.println(
                    "RecipeApp \n" +
                            "Available commands are: \n" +
                            "Help\n" +
                            "Quit\n" +
                            "List\n" +
                            "View [ID]");
            System.out.print(">_");
            String cmd = userScan.next();
            switch (cmd) {
                case "List":
                case "list":
                case "l":
                    System.out.println("List recipes is not implemented yet...\n");
                    break;
                case "help":
                case "Help":
                case "h":
                    System.out.println("Help: \n" +
                            "The help command prints a menu of other commands and what they do \n" +
                            "List: Lists available rooms\n" +
                            "View [ID]: Views the details of the room numbered [ID]\n" +
                            "Quit: Quits the program");
                    break;
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

    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        cl.run();
    }
}
