package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Asks user for confirmation before clearing all currently listed contacts.
 */
public class ConfirmClearCommand extends ClearCommand implements ConfirmCommand {
    public static final String MESSAGE_ASK_CONFIRMATION =
            "Are you sure you want to clear the currently listed contacts? [y/n]%1$s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        return new CommandResult(
                getConfirmationMessage(model),
                false, false, true
        );
    }

    @Override
    public String getConfirmationMessage(Model model) {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        StringBuilder sb = new StringBuilder();
        lastShownList.stream()
                .map(Messages::format)
                .forEach(formattedPerson -> sb.append("\n").append(formattedPerson));

        String listedContactsPreview = sb.toString();
        return String.format(MESSAGE_ASK_CONFIRMATION, listedContactsPreview);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof ConfirmClearCommand;
    }
}
