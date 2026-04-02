package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * requests confirmation before allowing user to exit the application.
 */
public class ConfirmExitCommand extends ExitCommand implements ConfirmCommand {
    public static final String MESSAGE_CONFIRM_EXIT =
            "Are you sure you want to exit the application? [y/n]";

    /**
     * Creates a ConfirmExitCommand to add the specified {@code Person}
     */
    public ConfirmExitCommand() {
        super();
    }

    /**
     * Executes the command. If the person is not a duplicate, adds the person immediately.
     * Otherwise, returns a confirmation message and waits for user confirmation.
     *
     * @param model {@code Model} which the command should operate on.
     * @return a command result containing either a success message or a confirmation prompt.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        return new CommandResult(
                MESSAGE_CONFIRM_EXIT,
                false, false, true
        );
    }

    @Override
    public String getConfirmationMessage(Model model) {
        return MESSAGE_CONFIRM_EXIT;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ConfirmExitCommand confirmExitCommand)) {
            return false;
        }

        return this.equals(confirmExitCommand);
    }
}
