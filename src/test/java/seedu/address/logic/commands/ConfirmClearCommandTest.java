package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ConfirmClearCommand}.
 */
public class ConfirmClearCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unfilteredList_success() {
        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();
        String expectedMessage = buildExpectedConfirmationMessage(model.getSortedFilteredPersonList());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(confirmClearCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();
        String expectedMessage = buildExpectedConfirmationMessage(model.getSortedFilteredPersonList());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(confirmClearCommand, model, expectedCommandResult, expectedModel);

        String actualPrompt = confirmClearCommand.getConfirmationMessage(model);
        assertTrue(actualPrompt.contains(Messages.format(model.getSortedFilteredPersonList().get(0))));
        assertFalse(actualPrompt.contains(Messages.format(getTypicalAddressBook().getPersonList().get(1))));
    }

    @Test
    public void execute_emptyList_success() {
        model = new ModelManager();
        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();

        CommandResult expectedCommandResult = new CommandResult(
                String.format(ConfirmClearCommand.MESSAGE_ASK_CONFIRMATION, ""), false, false, true);

        assertCommandSuccess(confirmClearCommand, model, expectedCommandResult, new ModelManager());
    }

    @Test
    public void getConfirmationMessage_filteredListEmpty_success() {
        Model nonEmptyModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        nonEmptyModel.updateFilteredPersonList(person -> false);

        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();
        String expectedMessage = String.format(ConfirmClearCommand.MESSAGE_ASK_CONFIRMATION, "");

        assertEquals(expectedMessage, confirmClearCommand.getConfirmationMessage(nonEmptyModel));
    }

    @Test
    public void getConfirmationMessage_unfilteredList_success() {
        Model nonEmptyModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();

        String message = confirmClearCommand.getConfirmationMessage(nonEmptyModel);
        String firstPerson = Messages.format(nonEmptyModel.getSortedFilteredPersonList().get(0));
        String secondPerson = Messages.format(nonEmptyModel.getSortedFilteredPersonList().get(1));

        assertTrue(message.contains(firstPerson));
        assertTrue(message.contains(secondPerson));
        assertTrue(message.indexOf(firstPerson) < message.indexOf(secondPerson));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();
        assertThrows(NullPointerException.class, () -> confirmClearCommand.execute(null));
    }

    @Test
    public void getConfirmationMessage_nullModel_throwsNullPointerException() {
        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();
        assertThrows(NullPointerException.class, () -> confirmClearCommand.getConfirmationMessage(null));
    }

    @Test
    public void getConfirmationMessage_nullPersonList_assertionError() {
        assumeTrue(ConfirmClearCommand.class.desiredAssertionStatus());

        Model modelWithNullPersonList = new ModelManager(getTypicalAddressBook(), new UserPrefs()) {
            @Override
            public ObservableList<Person> getSortedFilteredPersonList() {
                return null;
            }
        };

        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();
        assertThrows(AssertionError.class, () -> confirmClearCommand.getConfirmationMessage(modelWithNullPersonList));
    }

    @Test
    public void getCommandWord_inheritedFromClearCommand() {
        ConfirmClearCommand confirmClearCommand = new ConfirmClearCommand();
        assertEquals(ClearCommand.COMMAND_WORD, confirmClearCommand.getCommandWord());
    }

    @Test
    public void equals() {
        ConfirmClearCommand firstCommand = new ConfirmClearCommand();
        ConfirmClearCommand secondCommand = new ConfirmClearCommand();

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
    }

    private String buildExpectedConfirmationMessage(List<Person> persons) {
        StringBuilder sb = new StringBuilder();
        persons.stream()
                .map(person -> "\n" + Messages.format(person))
                .forEach(sb::append);
        return String.format(ConfirmClearCommand.MESSAGE_ASK_CONFIRMATION, sb);
    }
}
