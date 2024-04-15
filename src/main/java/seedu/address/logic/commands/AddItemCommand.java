package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINTS;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.item.Item;

/**
 * Adds an item to the application's item catalogue.
 * This command allows users to add new items, including their names and associated points,
 * to the catalogue. Each item must have a unique name. If an attempt is made to add an item
 * with a name that already exists in the catalogue, the command will not be executed, and
 * an error message will be shown to the user.
 * Usage example: {@code addItem i/Croissant p/50} - This adds a new item named "Croissant"
 * with 50 points to the catalogue.
 */
public class AddItemCommand extends Command {

    public static final String COMMAND_WORD = "additem";
    public static final String INVALID_COMMAND_FORMAT = "Invalid command format! ";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an item to the catalogue. \n"
            + "Parameters: "
            + PREFIX_ITEM + "ITEM_NAME "
            + PREFIX_POINTS + "POINTS_PER_UNIT\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ITEM + "chocolate cake "
            + PREFIX_POINTS + "50";

    public static final String MESSAGE_SUCCESS = "New item added: %1$s";
    public static final String MESSAGE_DUPLICATE_ITEM = "This item already exists in the catalogue.";

    private final Item toAdd;

    /**
     * Creates an AddItemCommand to add the specified {@code Item}
     */
    public AddItemCommand(Item item) {
        requireNonNull(item);
        toAdd = item;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasItem(toAdd.getName())) {
            throw new CommandException(MESSAGE_DUPLICATE_ITEM);
        }

        model.addItem(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddItemCommand // instanceof handles nulls
                && toAdd.equals(((AddItemCommand) other).toAdd));
    }
}
