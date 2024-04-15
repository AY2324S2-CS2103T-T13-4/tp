package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's membership tier and points in the address book.
 * Guarantees: immutable;
 */
public class MembershipPoints {

    public static final String MESSAGE_CONSTRAINTS =
            "Membership points should be non-negative integer and less than 2,000,000,000.";

    /**
     * Represents the tiers and the minimum points required to reach them, from highest to lowest.
     */
    public static final String[][] TIERS = {
            {"PLATINUM", "10000"},
            {"GOLD", "5000"},
            {"SILVER", "2000"},
            {"BRONZE", "0"},
    };

    /**
     * The total amount of points accumulated by a member.
     */
    public final int value;

    public MembershipPoints() {
        this.value = 0;
    }

    /**
     * Creates a MembershipPoints object with the specified points.
     * @param newPoints The points to be set.
     */
    public MembershipPoints(int newPoints) {
        requireNonNull(newPoints);
        this.value = newPoints;
    }

    /**
     * Creates a MembershipPoints object with the specified points given as a string.
     * @param points The points to be set.
     */
    public MembershipPoints(String points) {
        requireNonNull(points);
        checkArgument(isValidMembershipPoints(Integer.parseInt(points)),
                "Points should be a non-negative integer.");
        this.value = Integer.parseInt(points);
    }

    /**
     * Calculates the tier of the membership based on {@code expPoints}.
     * @return The tier of the member.
     */
    public String getTier() {
        for (int i = 0; i < TIERS.length - 1; i++) {
            if (value >= Integer.parseInt(TIERS[i][1])) {
                return TIERS[i][0];
            }
        }
        return TIERS[TIERS.length - 1][0];
    }


    /**
     * Returns the integer value of membership points.
     *
     * @return The integer value of the membership points.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Checks if the points {@code test} are valid.
     * @param test The points to be tested.
     */
    public static boolean isValidMembershipPoints(int test) {
        requireNonNull(test);
        return test >= 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%d pts)", getTier(), value);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        MembershipPoints that = (MembershipPoints) other;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        Integer expPoints = this.value;
        return expPoints.hashCode();
    }
}
