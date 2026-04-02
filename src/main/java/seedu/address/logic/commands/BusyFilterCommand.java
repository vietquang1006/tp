package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Filters and lists all persons in address book whose busy period overlaps with the specified date range.
 */
public class BusyFilterCommand extends Command {

    public static final String COMMAND_WORD = "busyfilter";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Filters persons whose busy period overlaps with the specified date range "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: -s START_DATE -e END_DATE (both in DD/MM/YYYY format)\n"
            + "Example: " + COMMAND_WORD + " -s 01/01/2026 -e 31/12/2026";

    private final Predicate<Person> predicate;

    public BusyFilterCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                Messages.getMessageForPersonsListed(model.getSortedFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BusyFilterCommand otherBusyFilterCommand)) {
            return false;
        }
        return predicate.equals(otherBusyFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }

    public String getCommandWord() {
        return COMMAND_WORD;
    }
}

