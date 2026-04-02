package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_NO_PERSON_LISTED_OVERVIEW = "No person listed!";
    public static final String MESSAGE_SINGULAR_LISTED_OVERVIEW = "1 person listed!";
    public static final String MESSAGE_PLURAL_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER = "Only give alphanumeric keywords";
    public static final String MESSAGE_ONLY_YES_NO = "Please enter either \'y\' or \'n\'";
    public static final String MESSAGE_SUCCESSFUL_CANCEL = "%1$s command cancelled.";

    /**
     * Returns a message to summarise the number of persons listed.
     * @param n the number of persons in the list
     */
    public static String getMessageForPersonsListed(int n) {
        if (n == 0) {
            return MESSAGE_NO_PERSON_LISTED_OVERVIEW;
        } else if (n == 1) {
            return MESSAGE_SINGULAR_LISTED_OVERVIEW;
        } else {
            return String.format(MESSAGE_PLURAL_LISTED_OVERVIEW, n);
        }
    }

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        person.getRole().ifPresent(role -> builder.append(role).append(" "));
        builder.append(person.getName());
        person.getPhone().ifPresent(phone -> builder.append("; Phone: ").append(phone));
        person.getEmail().ifPresent(email -> builder.append("; Email: ").append(email));
        person.getAddress().ifPresent(address -> builder.append("; Address: ").append(address));
        if (!person.getTags().isEmpty()) {
            builder.append("; Tags: ");
            person.getTags().forEach(builder::append);
        }
        if (!person.getBusyPeriods().isEmpty()) {
            builder.append("; Busy: ").append(person.getBusyPeriods());
        }
        return builder.toString();
    }
}
