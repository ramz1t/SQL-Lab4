package se.hkr.menu;

/**
 * Represents an option in a menu with a description and an associated action.
 * Each menu option is immutable and executes a specific {@link Runnable} action when selected.
 */
public class MenuOption {
    /**
     * A brief description of the menu option.
     */
    public final String description;

    /**
     * The action to be performed when this menu option is selected.
     */
    public final Runnable action;

    /**
     * Constructs a new {@code MenuOption} with the specified description and action.
     *
     * @param description a brief text describing the menu option.
     * @param action      a {@link Runnable} representing the action to be executed.
     */
    public MenuOption(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }
}