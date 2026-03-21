package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;

public class BusyCommand extends Command{
    public static final String COMMAND_WORD = "busy";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the person identified "
            + "by the index number used in the displayed person list as busy for a specified period.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_START_DATE + "START_DATE "
            + PREFIX_END_DATE + "END_DATE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_START_DATE + "25-03-2026 "
            + PREFIX_END_DATE + "28-03-2026";

    public static final String MESSAGE_BUSY_PERSON_SUCCESS = "Successfully marked %1$s as busy from %2$s to %3$s";

    @Override
    public CommandResult execute(seedu.address.model.Model model) throws seedu.address.logic.commands.exceptions.CommandException {
        throw new seedu.address.logic.commands.exceptions.CommandException("Not implemented yet");
    }
}
