package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMSHIP_PTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.MembershipPoints;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Adds membership points for a person in the address book.
 */
public class AddMemPointsCommand extends Command {
    public static final String COMMAND_WORD = "addmempts";

    public static final String INVALID_COMMAND_FORMAT = "Invalid command format! ";
    public static final String MESSAGE_CONSTRAINTS =
            "Membership points should be non-negative integer and less than 2,000,000,000.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Add membership points to the person identified. "
            + "May upgrade the member's tier after adding points.\n"
            + "Parameters: " + PREFIX_NAME + "MEMBER_NAME " + PREFIX_MEMSHIP_PTS + "POINTS_TO_ADD\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Alice "
            + PREFIX_MEMSHIP_PTS + "100";
    public static final String MESSAGE_ADD_MEMBERSHIP_SUCCESS = "Added %1$d membership points to Person: %2$s";
    public static final String MESSAGE_MAX_POINTS =
            "%s's membership points are now at the maximum limit of 2,000,000,000.";
    private final Name name;
    private final MembershipPoints pointsToAdd;

    /**
     * @param name of the person in the filtered person list to edit the remark
     * @param pointsToAdd of the person to be updated to
     */
    public AddMemPointsCommand(Name name, MembershipPoints pointsToAdd) {
        requireAllNonNull(name, pointsToAdd);
        this.name = name;
        this.pointsToAdd = pointsToAdd;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        Optional<Person> personOptional = lastShownList.stream()
                .filter(person -> person.getName().fullName.toLowerCase().contains(this.name.fullName.toLowerCase()))
                .findFirst();

        if (personOptional.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_PERSON_NOT_FOUND);
        }

        Person personToEdit = personOptional.get();
        int oldMemPoints = personToEdit.getMembershipPoints().getValue();
        personToEdit.addMembershipPoints(new MembershipPoints(Math.min(pointsToAdd.getValue(),
                Person.MAX_POINTS - oldMemPoints))); // Ensure that the points don't exceed MAX_POINTS
        int newMemPoints = personToEdit.getMembershipPoints().getValue();

        model.setPerson(personToEdit, personToEdit);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (newMemPoints == Person.MAX_POINTS) {
            return new CommandResult(String.format(MESSAGE_MAX_POINTS, personToEdit.getName(), Person.MAX_POINTS));
        } else {
            return new CommandResult(
                    String.format(MESSAGE_ADD_MEMBERSHIP_SUCCESS, pointsToAdd.getValue(), personToEdit.getName()));
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddMemPointsCommand)) {
            return false;
        }

        // state check
        AddMemPointsCommand e = (AddMemPointsCommand) other;
        return name.equals(e.name)
                && pointsToAdd == e.pointsToAdd;
    }
}
