package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Asks user for confirmation for editing an existing person in the address book to another person.
 */
public class ConfirmEditCommand extends EditCommand implements ConfirmCommand {

    public static final String MESSAGE_DUPLICATE_PERSON_WARNING =
            "Warning: %1$s\nis an existing person.\n";

    public static final String MESSAGE_ASK_CONFIRMATION =
            "Are you sure you want to edit the contact: %1$s? [y/n]";

    /**
     * Creates a ConfirmEditCommand to add the specified {@code Person}
     */
    public ConfirmEditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        super(index, editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return new CommandResult(getConfirmationMessage(model), false, false, true);
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        String outputMessage;
        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            outputMessage =
                    String.format(MESSAGE_DUPLICATE_PERSON_WARNING, Messages.format(editedPerson))
                            + String.format(MESSAGE_ASK_CONFIRMATION, Messages.format(personToEdit));
        } else {
            outputMessage = String.format(MESSAGE_ASK_CONFIRMATION, Messages.format(personToEdit));
        }

        return outputMessage;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ConfirmEditCommand)) {
            return false;
        }

        ConfirmEditCommand otherConfirmEditCommand = (ConfirmEditCommand) other;
        return index.equals(otherConfirmEditCommand.index)
                && editPersonDescriptor.equals(otherConfirmEditCommand.editPersonDescriptor);
    }

}
