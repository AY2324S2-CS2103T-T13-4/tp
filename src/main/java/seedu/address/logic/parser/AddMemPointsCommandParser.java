package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMSHIP_PTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddMemPointsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.MembershipPoints;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new {@code MembershipPoints Command} object
 */
public class AddMemPointsCommandParser implements Parser<AddMemPointsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code AddMemPointsCommand}
     * and returns a {@code AddMemPointsCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddMemPointsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_MEMSHIP_PTS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_MEMSHIP_PTS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(AddMemPointsCommand.INVALID_COMMAND_FORMAT + "\n"
                    + AddMemPointsCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).orElse(null));
        MembershipPoints pointsToAdd =
                ParserUtil.parseMembershipPoints(argMultimap.getValue(PREFIX_MEMSHIP_PTS).orElse(null));


        if (pointsToAdd.value <= 0) {
            throw new ParseException(AddMemPointsCommand.MESSAGE_CONSTRAINTS);
        }

        return new AddMemPointsCommand(name, pointsToAdd);
    }

    /**
     * Returns true if all the given prefixes are present in the given ArgumentMultimap and not empty.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
