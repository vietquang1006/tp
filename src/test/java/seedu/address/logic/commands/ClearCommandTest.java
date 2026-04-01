package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

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

    @org.junit.jupiter.api.Test
    public void getCommandWord() {
        ClearCommand command = new ClearCommand();
        org.junit.jupiter.api.Assertions.assertEquals(ClearCommand.COMMAND_WORD, command.getCommandWord());
    }
}
