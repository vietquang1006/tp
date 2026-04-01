package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;


/**
 * Finds and lists all persons whose name and/or tags contain all of the provided keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names/tags contain all "
            + "specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: -n NAME_KEYWORDS [; MORE_NAME_KEYWORDS]... [-t TAG_KEYWORDS [; MORE_TAG_KEYWORDS]...]\n"
            + "Examples: " + COMMAND_WORD + " -n alice pauline ; josh\n"
            + "          " + COMMAND_WORD + " -t friends ; owes me ; secretary\n"
            + "          " + COMMAND_WORD + " -n meier -t friends";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getSortedFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
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
