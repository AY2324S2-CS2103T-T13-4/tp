package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINTS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddItemCommand;
import seedu.address.model.item.Item;
import seedu.address.model.person.Points;

public class AddItemCommandParserTest {

    private AddItemCommandParser parser = new AddItemCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String expectedItemName = "Bagel";
        int expectedPoints = 5;
        Item expectedItem = new Item(expectedItemName, expectedPoints);

        assertParseSuccess(parser, " " + PREFIX_ITEM + expectedItemName + " " + PREFIX_POINTS + expectedPoints,
                new AddItemCommand(expectedItem));
    }

    @Test
    public void parse_missingItemName_failure() {
        String expectedMessage = AddItemCommand.INVALID_COMMAND_FORMAT + "\n" + AddItemCommand.MESSAGE_USAGE;
        assertParseFailure(parser, " " + PREFIX_POINTS + "5", expectedMessage);
    }

    @Test
    public void parse_missingPoints_failure() {
        String expectedMessage = AddItemCommand.INVALID_COMMAND_FORMAT + "\n" + AddItemCommand.MESSAGE_USAGE;
        assertParseFailure(parser, " " + PREFIX_ITEM + "Bagel", expectedMessage);
    }

    @Test
    public void parse_invalidPoints_failure() {
        String expectedMessage = Points.MESSAGE_CONSTRAINTS;
        assertParseFailure(parser, " " + PREFIX_ITEM + "Bagel " + PREFIX_POINTS + "abc", expectedMessage);
    }

    @Test
    public void parse_invalidItemName_failure() {
        String expectedMessage = Item.MESSAGE_CONSTRAINTS;
        // Assuming you have validation for item names
        assertParseFailure(parser, " " + PREFIX_ITEM + "123 " + PREFIX_POINTS + "5", expectedMessage);
    }

    @Test
    public void parse_pointsNoPrefix_failure() {
        String expectedMessage = AddItemCommand.INVALID_COMMAND_FORMAT + "\n" + AddItemCommand.MESSAGE_USAGE;
        assertParseFailure(parser, " " + PREFIX_ITEM + "Bagel 5", expectedMessage);
    }

    @Test
    public void parse_noInput_failure() {
        String expectedMessage = AddItemCommand.INVALID_COMMAND_FORMAT + "\n" + AddItemCommand.MESSAGE_USAGE;
        assertParseFailure(parser, "", expectedMessage);
    }
}
