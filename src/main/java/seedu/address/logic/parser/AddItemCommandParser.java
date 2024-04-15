package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINTS;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddItemCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.item.Item;
import seedu.address.model.person.Points;

/**
 * Parses input arguments and creates a new AddItemCommand object.
 * Input format: {@code additem i/ITEM_NAME p/POINTS}, where ITEM_NAME is the name of the item to be added
 * and POINTS is the number of points associated with the item.
 */
public class AddItemCommandParser implements Parser<AddItemCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddItemCommand
     * and returns an AddItemCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public AddItemCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ITEM, PREFIX_POINTS);

        if (!arePrefixesPresent(argMultimap, PREFIX_ITEM, PREFIX_POINTS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddItemCommand.MESSAGE_USAGE));
        }

        String itemName = argMultimap.getValue(PREFIX_ITEM).orElse("");

        if (!Item.isValidItemName(itemName)) {
            throw new ParseException(Item.MESSAGE_CONSTRAINTS);
        }
        int points = ParserUtil.parsePoints(argMultimap.getValue(PREFIX_POINTS).orElse("")).getValue();

        Item item = new Item(itemName, points);
        return new AddItemCommand(item);

    }
    /**
     * Returns true if all the prefixes contain non-empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
