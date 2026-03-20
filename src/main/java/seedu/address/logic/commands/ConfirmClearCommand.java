package seedu.address.logic.commands;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

public class ConfirmClearCommand extends ClearCommand implements ConfirmCommand {
    public static final String MESSAGE_ASK_CONFIRMATION =
            "Are you sure you want to clear the listed contacts? [y/n] %1$s";

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
                .map(person -> "\n" + Messages.format(person))
                .forEach(sb::append);

        return String.format(MESSAGE_ASK_CONFIRMATION, sb);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ConfirmClearCommand confirmClearCommand)) {
            return false;
        }

        return this.equals(confirmClearCommand);
    }
}
