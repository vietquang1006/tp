package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's role in the address book.
 * Guarantees: immutable; role name is valid as declared in {@link #isValidRole(String)}.
 */
public class Role {

    /**
     * Thinh: I used AI assistance to help identify relevant files to modify
     * and to refine the validation rules for roles. I reviewed and adjusted
     * the final regex, constraints, and implementation.
     */

    public static final String MESSAGE_CONSTRAINTS =
            "Roles should not be blank, must not start or end with spaces, "
                    + "must not contain consecutive spaces, and may contain letters, numbers, spaces, "
                    + "and the symbols &, /, (, ), comma, hyphen, and apostrophe.";

    /**
     * Thinh: I used Ai assistance to write this.
     * Starts with alphanumeric.
     * Then allows alphanumeric or allowed punctuation.
     * Spaces are allowed only if they are single spaces (not consecutive).
     * Ends with alphanumeric or one of ) or '.
     */
    public static final String VALIDATION_REGEX =
            "[\\p{Alnum}](?:[\\p{Alnum}&/(),\\-']| (?! ))*[\\p{Alnum}\\)']";

    public final String roleName;

    /**
     * Constructs a {@code Role}.
     *
     * @param role A valid role.
     */
    public Role(String role) {
        requireNonNull(role);
        checkArgument(isValidRole(role), MESSAGE_CONSTRAINTS);
        this.roleName = role;
    }

    /**
     * Returns true if a given string is a valid role.
     */
    public static boolean isValidRole(String test) {
        requireNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return roleName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Role)) {
            return false;
        }
        Role otherRole = (Role) other;
        return roleName.equals(otherRole.roleName);
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }
}
