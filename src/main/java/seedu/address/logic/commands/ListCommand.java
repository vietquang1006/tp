package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.SORT_BY_NAME_ASCENDING;
import static seedu.address.model.Model.SORT_BY_NAME_DESCENDING;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    public static final String MESSAGE_SUCCESS_SORT_ASCENDING = "Listed all persons in ascending order";
    public static final String MESSAGE_SUCCESS_SORT_DESCENDING = "Listed all persons in descending order";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all persons in the address book.\n"
            + "Parameters: [sort] [order]\n"
            + "Example: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD + " ascending\n"
            + "Example: " + COMMAND_WORD + " reverse";

    /**
     * Represents the sorting order for the list command.
     */
    public enum SortOrder {
        NONE,
        ASCENDING,
        DESCENDING
    }

    private final SortOrder sortOrder;

    public ListCommand(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Returns the sorting order of this ListCommand.
     * @return the sorting order
     */
    public SortOrder getSortOrder() {
        return this.sortOrder;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        switch (sortOrder) {
        case ASCENDING:
            model.updateSortedPersonList(SORT_BY_NAME_ASCENDING);
            return new CommandResult(MESSAGE_SUCCESS_SORT_ASCENDING);

        case DESCENDING:
            model.updateSortedPersonList(SORT_BY_NAME_DESCENDING);
            return new CommandResult(MESSAGE_SUCCESS_SORT_DESCENDING);

        case NONE:
        default:
            model.updateSortedPersonList(null);
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }
}
