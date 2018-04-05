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

    private void view(String title){
        System.out.println("Viewing recipe "+ title+"\n");
    }

    private void list(){
        System.out.println("Listing recipes...\n");
        System.out.println("List recipes not implemented yet\n");
    }
    private void help(){
        System.out.println("Help: \n" +
                "The help command prints a menu of other commands and what they do \n" +
                "List: Lists available rooms\n" +
                "View [ID]: Views the details of the room numbered [ID]\n" +
                "Quit: Quits the program\n\n");
    }
    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        cl.run();
    }
}
