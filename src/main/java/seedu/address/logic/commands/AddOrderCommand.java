package seedu.address.logic.commands;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.item.Item;
import seedu.address.model.person.MembershipPoints;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Points;
import seedu.address.model.person.orders.Order;

/**
 * Changes the order of an existing person in the address book.
 */
public class AddOrderCommand extends Command {

    public static final String COMMAND_WORD = "addorder";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds an order to the order list of the person identified by the given name.\n"
            + "Parameters: n/MEMBER_NAME i/ITEM_NAME [q/QUANTITY]\n"
            + "NOTE: If quantity is not specified, it will default to 1.\n"
            + "Example: " + COMMAND_WORD + " n/Alex Yeoh i/Chocolate Chip Cookies 100g q/3";

    public static final String MESSAGE_ADD_ORDER_SUCCESS = "Added order to Person: %1$s";

    public static final String MESSAGE_ITEM_NOT_FOUND = "Item not found in the inventory";
    public static final String MESSAGE_MAX_POINTS_REACHED = "Maximum points limit reached.";
    public static final String MESSAGE_MAX_MEMBERSHIP_POINTS_REACHED = "Maximum membership points limit reached.";

    public final Name name;
    public final String itemName;
    public final int quantity;

    public final LocalDateTime orderDateTime;

    /**
     * @param name of the person to add the order to
     * @param itemName name of item ordered
     * @param quantity of specified item ordered
     */
    public AddOrderCommand(Name name, String itemName, int quantity) {
        requireAllNonNull(name, itemName, quantity);

        this.name = name;
        this.itemName = itemName;
        this.quantity = quantity;
        this.orderDateTime = null;
    }

    /**
     * @param name of the person to add the order to
     * @param itemName name of item ordered
     * @param quantity of specified item ordered
     * @param orderDateTime of the order
     */
    public AddOrderCommand(Name name, String itemName,
                           int quantity, LocalDateTime orderDateTime) {
        requireAllNonNull(name, itemName, quantity, orderDateTime);

        this.name = name;
        this.itemName = itemName;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Optional<Person> personOptional = lastShownList.stream()
                .filter(person -> person.getName().fullName.toLowerCase().contains(this.name.fullName.toLowerCase()))
                .findFirst();
        if (personOptional.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_PERSON_NOT_FOUND);
        }

        Person personToUpdate = personOptional.get();

        //Find matching item in catalogue
        AddressBook addressBook = (AddressBook) model.getAddressBook();
        Item item = addressBook.findItem(itemName);
        if (isNull(item)) {
            throw new CommandException(MESSAGE_ITEM_NOT_FOUND);
        }

        Order newOrder = orderDateTime != null
                ? new Order(item, quantity, orderDateTime)
                : new Order(item, quantity);
        personToUpdate.addOrders(newOrder);

        Points pointsToAdd = new Points(item.getPoints() * quantity);
        personToUpdate.addPoints(pointsToAdd);

        MembershipPoints membershipPointsToAdd = new MembershipPoints(item.getPoints() * quantity);
        personToUpdate.addMembershipPoints(membershipPointsToAdd);

        model.setPerson(personToUpdate, personToUpdate);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(personToUpdate));
    }

    /**
     * Generates a command execution success message based on whether
     * the order is added to or removed from {@code personToUpdate}.
     */
    static String generateSuccessMessage(Person person) {
        boolean maxPointsReached = person.getPoints().getValue() == Person.MAX_POINTS;
        boolean maxMembershipPointsReached = person.getMembershipPoints().getValue() == Person.MAX_POINTS;
        StringBuilder sb = new StringBuilder(String.format(MESSAGE_ADD_ORDER_SUCCESS, person.getName()));

        if (maxPointsReached && maxMembershipPointsReached) {
            sb.append(" \n").append(MESSAGE_MAX_POINTS_REACHED).append(" \n")
                    .append(MESSAGE_MAX_MEMBERSHIP_POINTS_REACHED);
        } else if (maxPointsReached) {
            sb.append(" \n").append(MESSAGE_MAX_POINTS_REACHED);
        } else if (maxMembershipPointsReached) {
            sb.append(" \n").append(MESSAGE_MAX_MEMBERSHIP_POINTS_REACHED);
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddOrderCommand)) {
            return false;
        }

        AddOrderCommand e = (AddOrderCommand) other;
        return name.equals(e.name)
                && itemName.equals(e.itemName) && (quantity == e.quantity);
    }

    @Override
    public String toString() {
        return "AddOrderCommand{"
                + "personNamePredicate=" + name
                + ", itemName='" + itemName + '\''
                + ", quantity=" + quantity
                + ", orderDateTime=" + orderDateTime
                + '}';
    }
}
