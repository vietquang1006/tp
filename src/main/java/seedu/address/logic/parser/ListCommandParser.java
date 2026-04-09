package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Yi Heng: I used AI to help me ideate what are the points of assertions in the main code
     * and only kept the instances recommended by AI that I think are the most important, following
     * the course's instructions to use assertions to check for programmer errors and not user input errors.
     *
     * Likewise for logging.
     */

    private static final Logger logger = LogsCenter.getLogger(ListCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ListCommand parse(String args) throws ParseException {
        assert args != null : "Arguments string should not be null";
        logger.info("Parsing ListCommand arguments: '" + args + "'");

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            logger.fine("No sort order specified - using default NONE");
            ListCommand command = new ListCommand(ListCommand.SortOrder.NONE);
            logger.fine("ListCommand created with NONE sort order");
            return command;
        }

        String[] tokens = trimmedArgs.split("\\s+");
        assert tokens.length > 0 : "Tokens should not be empty after split";
        logger.fine("Parsed tokens from arguments: " + tokens.length + " token(s)");

        if (tokens.length >= 2) {
            logger.warning("Invalid ListCommand format: too many arguments provided (" + tokens.length + ")");
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ListCommand.MESSAGE_USAGE));
        }

        String keyword = tokens[0].toLowerCase();
        logger.fine("Processing sort keyword: '" + keyword + "'");

        ListCommand command;
        switch (keyword) {
        case "sort":
        case "ascending":
            logger.fine("Sort order detected: ASCENDING");
            command = new ListCommand(ListCommand.SortOrder.ASCENDING);
            assert command.getSortOrder() == ListCommand.SortOrder.ASCENDING : "Sort order should be ASCENDING";
            logger.info("ListCommand created with ASCENDING sort order");
            return command;

        case "descending":
        case "reverse":
            logger.fine("Sort order detected: DESCENDING");
            command = new ListCommand(ListCommand.SortOrder.DESCENDING);
            assert command.getSortOrder() == ListCommand.SortOrder.DESCENDING : "Sort order should be DESCENDING";
            logger.info("ListCommand created with DESCENDING sort order");
            return command;

        default:
            logger.warning("Unknown sort keyword: '"
                    + keyword + "' - valid options are: sort, ascending, descending, reverse");
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ListCommand.MESSAGE_USAGE));
        }
    }
}
