package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the contacts currently shown in the list.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "%d contact(s) have been cleared.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Person> currentPersonList = new ArrayList<>(model.getSortedFilteredPersonList());

        currentPersonList.forEach(model::deletePerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, currentPersonList.size()));
    }

    public String getCommandWord() {
        return COMMAND_WORD;
    }
}
