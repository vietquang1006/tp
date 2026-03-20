package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Listed contacts has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Person> currentPersonList = new ArrayList<>(model.getSortedFilteredPersonList());

        currentPersonList.forEach(model::deletePerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
