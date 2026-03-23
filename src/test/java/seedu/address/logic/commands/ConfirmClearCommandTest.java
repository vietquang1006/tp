package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

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

