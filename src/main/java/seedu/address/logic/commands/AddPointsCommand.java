
package seedu.address.logic.commands;


import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Points;
/**
 * Adds points to an existing person in the loyalty program.
 */
public class AddPointsCommand extends Command {
    public static final String COMMAND_WORD = "addpts";
    public static final String MESSAGE_CONSTRAINTS = "Points added should be greater than 0.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds points to the person identified. \n"
            + "Parameters: " + PREFIX_NAME + "NAME " + PREFIX_POINTS + "POINTS \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "John Doe " + PREFIX_POINTS + "40";
    public static final String MESSAGE_ADDPOINTS_SUCCESS = "Added %1$s points to %2$s";
    public static final String MESSAGE_MAX_POINTS = "%s's points are now at the maximum limit of %d.";

    private final Name name;
    private final Points pointsToAdd;

    /**
     * Constructs an AddPointsCommand to add the specified {@code Points}
     * to the person identified by {@code Name}.
     *
     * @param name of the person in the list to edit the points
     * @param pointsToAdd to be added to the persons current points
     */
    public AddPointsCommand(Name name, Points pointsToAdd) {
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
            throw new CommandException(
                    Messages.MESSAGE_PERSON_NOT_FOUND);
        }

        Person personToEdit = personOptional.get();
        personToEdit.addPoints(pointsToAdd);
        int newPoints = personToEdit.getPoints().getValue();

        model.setPerson(personToEdit, personToEdit);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (newPoints >= Person.MAX_POINTS) {
            return new CommandResult(String.format(MESSAGE_MAX_POINTS, personToEdit.getName(), Person.MAX_POINTS));
        } else {
            return new CommandResult(
                    String.format(MESSAGE_ADDPOINTS_SUCCESS, pointsToAdd.getValue(), personToEdit.getName()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof AddPointsCommand)) {
            return false;
        }

        AddPointsCommand e = (AddPointsCommand) other;
        return name.equals(e.name)
                && pointsToAdd.equals(e.pointsToAdd);
    }

}
