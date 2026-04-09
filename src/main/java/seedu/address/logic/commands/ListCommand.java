package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.SORT_BY_NAME_ASCENDING;
import static seedu.address.model.Model.SORT_BY_NAME_DESCENDING;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    /**
     * Yi Heng: I used AI to help me ideate what are the points of assertions in the main code
     * and only kept the instances recommended by AI that I think are the most important, following
     * the course's instructions to use assertions to check for programmer errors and not user input errors.
     *
     * Likewise for logging.
     */

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    public static final String MESSAGE_SUCCESS_SORT_ASCENDING = "Listed all persons in ascending order";
    public static final String MESSAGE_SUCCESS_SORT_DESCENDING = "Listed all persons in descending order";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all persons in the address book.\n"
            + "Parameters: [SORT_ORDER]\n"
            + "Example: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD + " sort\n"
            + "Example: " + COMMAND_WORD + " ascending\n"
            + "Example: " + COMMAND_WORD + " descending\n"
            + "Example: " + COMMAND_WORD + " reverse";

    private static final Logger logger = LogsCenter.getLogger(ListCommand.class);

    /**
     * Represents the sorting order for the list command.
     */
    public enum SortOrder {
        NONE,
        ASCENDING,
        DESCENDING
    }

    private final SortOrder sortOrder;

    /**
     * Creates a ListCommand with the specified sorting order.
     *
     * @param sortOrder The sorting order for the list command. Must not be null.
     */
    public ListCommand(SortOrder sortOrder) {
        assert sortOrder != null : "Sort order must not be null";
        this.sortOrder = sortOrder;
        logger.fine("ListCommand created with sort order: " + sortOrder);
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

        logger.info("Executing ListCommand with sort order: " + sortOrder);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        assert model.getSortedFilteredPersonList() != null : "Filtered person list should not be null after update";
        logger.fine("Filtered person list updated successfully");

        CommandResult result;
        switch (sortOrder) {
        case ASCENDING:
            logger.fine("Applying ASCENDING sort order");
            model.updateSortedPersonList(SORT_BY_NAME_ASCENDING);
            result = new CommandResult(MESSAGE_SUCCESS_SORT_ASCENDING);
            logger.info("Persons listed with ASCENDING sort order");
            break;

        case DESCENDING:
            logger.fine("Applying DESCENDING sort order");
            model.updateSortedPersonList(SORT_BY_NAME_DESCENDING);
            result = new CommandResult(MESSAGE_SUCCESS_SORT_DESCENDING);
            logger.info("Persons listed with DESCENDING sort order");
            break;

        case NONE:
        default:
            logger.fine("Applying no sort order (displaying as-is)");
            model.updateSortedPersonList(null);
            result = new CommandResult(MESSAGE_SUCCESS);
            logger.info("Persons listed without sorting");
            break;
        }

        logger.fine("ListCommand execution completed successfully");
        return result;
    }

    public String getCommandWord() {
        return COMMAND_WORD;
    }
}
