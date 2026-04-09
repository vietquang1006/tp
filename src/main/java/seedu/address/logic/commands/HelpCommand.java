package seedu.address.logic.commands;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    /**
     * Yi Heng: I used AI to help me ideate what are the points of assertions in the main code
     * and only kept the instances recommended by AI that I think are the most important, following
     * the course's instructions to use assertions to check for programmer errors and not user input errors.
     *
     * Likewise for logging.
     */

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    private static final Logger logger = LogsCenter.getLogger(HelpCommand.class);

    @Override
    public CommandResult execute(Model model) {
        logger.info("Executing HelpCommand to display help window");

        CommandResult result = new CommandResult(SHOWING_HELP_MESSAGE, true, false, false);
        assert result.isShowHelp() : "CommandResult should have showHelp flag set to true";

        logger.fine("HelpCommand executed successfully - help window flag set");
        return result;
    }

    public String getCommandWord() {
        return COMMAND_WORD;
    }
}
