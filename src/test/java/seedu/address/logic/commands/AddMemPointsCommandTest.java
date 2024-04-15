package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.MembershipPoints;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddMemPointsCommandTest {
    private static final MembershipPoints POINTS_TO_ADD_STUB = new MembershipPoints(50);
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        int updatedPoints = Math.min(
                Person.MAX_POINTS, firstPerson.getMembershipPoints().getValue() + POINTS_TO_ADD_STUB.getValue());
        Person editedPerson =
                new PersonBuilder(firstPerson).withMembershipPoints(Integer.toString(updatedPoints)).build();

        AddMemPointsCommand addMemPointsCommand = new AddMemPointsCommand(firstPerson.getName(), POINTS_TO_ADD_STUB);

        String expectedMessage =
                String.format(AddMemPointsCommand.MESSAGE_ADD_MEMBERSHIP_SUCCESS,
                        POINTS_TO_ADD_STUB.getValue(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addMemPointsCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonNameUnfilteredList_throwsCommandException() {
        Name invalidName = new Name("Invalid Name");
        AddMemPointsCommand addMemPointsCommand = new AddMemPointsCommand(invalidName, POINTS_TO_ADD_STUB);
        assertCommandFailure(addMemPointsCommand, model, Messages.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_partialNameSpecifiedUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Name partialName = new Name(firstPerson.getName().fullName.split(" ")[0]);
        int updatedPoints = Math.min(
                Person.MAX_POINTS, firstPerson.getMembershipPoints().getValue() + POINTS_TO_ADD_STUB.getValue());
        Person editedPerson =
                new PersonBuilder(firstPerson).withMembershipPoints(Integer.toString(updatedPoints)).build();

        AddMemPointsCommand addMemPointsCommand = new AddMemPointsCommand(partialName, POINTS_TO_ADD_STUB);
        String expectedMessage =
                String.format(AddMemPointsCommand.MESSAGE_ADD_MEMBERSHIP_SUCCESS, POINTS_TO_ADD_STUB.getValue(),
                        editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addMemPointsCommand, model, expectedMessage, expectedModel);
    }
}
