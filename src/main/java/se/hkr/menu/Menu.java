package se.hkr.menu;

import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * A command-line menu system that handles user input and executes corresponding actions.
 * The menu supports adding options with descriptions and associated actions, displaying
 * the menu to users, and running an input loop to process user choices.
 */
public class Menu {
    /** Stores menu options mapped to their keys */
    final private LinkedHashMap<String, MenuOption> options = new LinkedHashMap<>();
    /** Scanner for reading user input */
    final private Scanner scanner = new Scanner(System.in);

    /**
     * Adds a new option to the menu.
     * @param key The key/number used to select this option
     * @param description A description of what this option does
     * @param action The Runnable to execute when this option is selected
     */
    public void addOption(String key, String description, Runnable action) {
        options.put(key, new MenuOption(description, action));
    }

    /**
     * Displays the menu options to the user, including all added options
     * plus the standard menu and quit commands.
     */
    public void printMenu() {
        println("----------------");
        options.forEach((key, opt) -> println("| " + key + ") " + opt.description));
        println("| m) Print menu");
        println("| q) Quit");
        println("----------------");
    }

    /**
     * Prompts the user with a message and returns their input.
     * @param message The prompt message to display
     * @return The user's input as a String
     */
    public String prompt(String message) {
        print(message);
        return scanner.nextLine();
    }

    /**
     * Prints a message to the console without a line break.
     * @param message The message to print
     */
    public static void print(String message) {
        System.out.print(message);
    }

    /**
     * Prints a message to the console followed by a line break.
     * @param message The message to print
     */
    public static void println(String message) {
        System.out.println(message);
    }

    public static void printf(String message, Object ... args) {
        System.out.printf(message, args);
    }

    /**
     * Starts the menu system, displaying options and handling user input.
     * Runs in an infinite loop until the user selects quit.
     * Executes the corresponding action when a valid option is selected.
     */
    public void run() {
        printMenu();
        while (true) {
            String choice = prompt("Enter your choice: ").toLowerCase();

            if (choice.equals("q")) {
                System.exit(0);
            } else if (choice.equals("m")) {
                printMenu();
            } else if (options.containsKey(choice)) {
                options.get(choice).action.run();
            } else {
                println("Invalid choice. Please try again.");
            }
        }
    }
}