package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        String expectedMessage = String.format(ClearCommand.MESSAGE_SUCCESS, 0);
        assertCommandSuccess(new ClearCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        int clearedCount = getTypicalAddressBook().getPersonList().size();
        String expectedMessage = String.format(ClearCommand.MESSAGE_SUCCESS, clearedCount);
        assertCommandSuccess(new ClearCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getSortedFilteredPersonList().get(0);
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(new ClearCommand(), model,
                String.format(ClearCommand.MESSAGE_SUCCESS, 1), expectedModel);
        assertEquals(expectedModel.getSortedFilteredPersonList(), model.getSortedFilteredPersonList());
    }

    @Test
    public void execute_filteredListEmpty_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.updateFilteredPersonList(person -> false);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ClearCommand.MESSAGE_SUCCESS, 0);

        assertCommandSuccess(new ClearCommand(), model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getSortedFilteredPersonList(), model.getSortedFilteredPersonList());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ClearCommand command = new ClearCommand();
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_nullPersonList_assertionPathThrowsNullPointerException() {
        assumeTrue(ClearCommand.class.desiredAssertionStatus());

        Model modelWithNullPersonList = new ModelManager(getTypicalAddressBook(), new UserPrefs()) {
            @Override
            public ObservableList<Person> getSortedFilteredPersonList() {
                return null;
            }
        };

        ClearCommand command = new ClearCommand();
        assertThrows(NullPointerException.class, () -> command.execute(modelWithNullPersonList));
    }

    @Test
    public void getCommandWord() {
        ClearCommand command = new ClearCommand();
        assertEquals(ClearCommand.COMMAND_WORD, command.getCommandWord());
    }
}
