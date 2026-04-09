/**
 * Yi Heng: I used AI to help me complete the rest of the logging instances after I wrote the first user input
 * log and warning, and the first case under switch.
 */

package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_ONLY_YES_NO;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.BusyCommand;
import seedu.address.logic.commands.BusyFilterCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConfirmAddCommand;
import seedu.address.logic.commands.ConfirmClearCommand;
import seedu.address.logic.commands.ConfirmDeleteCommand;
import seedu.address.logic.commands.ConfirmEditCommand;
import seedu.address.logic.commands.ConfirmExitCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException, CommandException {
        logger.info("Parsing command from user input: '" + userInput + "'");

        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            logger.warning("User input does not match basic command format: '" + userInput + "'");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            logger.fine("Routing to AddCommandParser");
            return new AddCommandParser().parse(arguments);

        case BusyCommand.COMMAND_WORD:
            logger.fine("Routing to BusyCommandParser");
            return new BusyCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            logger.fine("Routing to EditCommandParser");
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            logger.fine("Routing to DeleteCommandParser");
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            logger.fine("Executing ClearCommand");
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            logger.fine("Routing to FindCommandParser");
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            logger.fine("Routing to ListCommandParser");
            return new ListCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            logger.info("Exit command detected - preparing to exit");
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            logger.fine("Help command detected");
            return new HelpCommand();

        default:
            logger.warning("Unknown command detected: '" + commandWord + "'");
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
    /**
     * Parses user input into command for execution.
     * If a command has a "Confirm" variant, it is included here.
     *
     * @param userInput full user input string
     * @return the command or ConfirmCommand based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommandWithConfirmation(String userInput) throws ParseException, CommandException {
        logger.info("Parsing command (with confirmation) from user input: '" + userInput + "'");

        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            logger.warning("User input does not match basic command format: '" + userInput + "'");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case ConfirmAddCommand.COMMAND_WORD:
            logger.fine("Routing to ConfirmAddCommandParser");
            return new ConfirmAddCommandParser().parse(arguments);

        case BusyCommand.COMMAND_WORD:
            logger.fine("Routing to BusyCommandParser");
            return new BusyCommandParser().parse(arguments);

        case BusyFilterCommand.COMMAND_WORD:
            logger.fine("Routing to BusyFilterCommandParser");
            return new BusyFilterCommandParser().parse(arguments);

        case ConfirmEditCommand.COMMAND_WORD:
            logger.fine("Routing to ConfirmEditCommandParser");
            return new ConfirmEditCommandParser().parse(arguments);

        case ConfirmDeleteCommand.COMMAND_WORD:
            logger.fine("Routing to ConfirmDeleteCommandParser");
            return new ConfirmDeleteCommandParser().parse(arguments);

        case ConfirmClearCommand.COMMAND_WORD:
            logger.fine("Executing ConfirmClearCommand");
            return new ConfirmClearCommand();

        case FindCommand.COMMAND_WORD:
            logger.fine("Routing to FindCommandParser");
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            logger.fine("Routing to ListCommandParser");
            return new ListCommandParser().parse(arguments);

        case ConfirmExitCommand.COMMAND_WORD:
            logger.info("Confirm exit command detected - preparing to exit");
            return new ConfirmExitCommand();

        case HelpCommand.COMMAND_WORD:
            logger.fine("Help command detected");
            return new HelpCommand();

        default:
            logger.warning("Unknown command detected: '" + commandWord + "'");
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses user input [y/n] into a boolean.
     *
     * @param userInput full user input string
     * @return boolean based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public boolean parseYesNo(String userInput) throws ParseException, CommandException {
        logger.info("Parsing yes/no response from user input: '" + userInput + "'");

        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            logger.warning("User input does not match basic command format for yes/no: '" + userInput + "'");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case "y":
            logger.fine("User confirmed with 'yes' response");
            return true;

        case "n":
            logger.fine("User declined with 'no' response");
            return false;

        default:
            logger.warning("Invalid yes/no response received: '" + commandWord + "'");
            throw new ParseException(MESSAGE_ONLY_YES_NO);
        }
    }

}
