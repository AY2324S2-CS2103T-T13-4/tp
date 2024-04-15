package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMSHIP_PTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddMemPointsCommand;
import seedu.address.model.person.MembershipPoints;
import seedu.address.model.person.Name;

public class AddMemPointsCommandParserTest {

    private AddMemPointsCommandParser parser = new AddMemPointsCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Name expectedName = new Name("Alice");
        MembershipPoints expectedMembershipPoints = new MembershipPoints("10");
        AddMemPointsCommand expectedCommand = new AddMemPointsCommand(expectedName, expectedMembershipPoints);
        // correct command format should pass
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_MEMSHIP_PTS + "10", expectedCommand);
    }

    @Test
    public void parse_missingName_failure() {
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        // missing name
        assertParseFailure(parser, " " + PREFIX_NAME + " " + PREFIX_MEMSHIP_PTS + "10", expectedMessage);
    }

    @Test
    public void parse_missingMembershipPoints_failure() {
        String expectedMessage = MembershipPoints.MESSAGE_CONSTRAINTS;
        // missing points
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice " + PREFIX_MEMSHIP_PTS + " ", expectedMessage);
    }

    @Test
    public void parse_invalidMembershipPoints_failure() {
        String expectedMessage = AddMemPointsCommand.MESSAGE_CONSTRAINTS;
        // invalid points (non-numeric)
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice " + PREFIX_MEMSHIP_PTS + "abc", expectedMessage);
    }

    @Test
    public void parse_invalidPrefix_failure() {
        String expectedMessage = AddMemPointsCommand.INVALID_COMMAND_FORMAT + "\n" + AddMemPointsCommand.MESSAGE_USAGE;
        // wrong prefix used ('p' instead of expected 'm')
        assertParseFailure(parser, " n/Alice p/10", expectedMessage);
    }

    @Test
    public void parse_pointsNoPrefix_failure() {
        String expectedMessage = AddMemPointsCommand.INVALID_COMMAND_FORMAT + "\n" + AddMemPointsCommand.MESSAGE_USAGE;
        // missing prefix for points
        assertParseFailure(parser, " n/Alice 10", expectedMessage);
    }

    @Test
    public void parse_noInput_failure() {
        String expectedMessage = AddMemPointsCommand.INVALID_COMMAND_FORMAT + "\n" + AddMemPointsCommand.MESSAGE_USAGE;
        // empty string input
        assertParseFailure(parser, "", expectedMessage);
    }

}
