package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Asks user for confirmation for deleting a person identified using it's displayed index from the address book.
 */
public class ConfirmDeleteCommand extends DeleteCommand implements ConfirmCommand {

    public static final String MESSAGE_ASK_CONFIRMATION =
            "Are you sure you want to delete the contact: %1$s? [y/n]";

    public ConfirmDeleteCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(getConfirmationMessage(model), false, false, true);
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        return String.format(MESSAGE_ASK_CONFIRMATION, Messages.format(personToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ConfirmDeleteCommand)) {
            return false;
        }

        ConfirmDeleteCommand otherConfirmDeleteCommand = (ConfirmDeleteCommand) other;
        return targetIndex.equals(otherConfirmDeleteCommand.targetIndex);
    }

}
