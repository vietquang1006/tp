package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.BusyInDateRangePredicate;

public class BusyFilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validDateRange_filtersPersons() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand command = new BusyFilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getSortedFilteredPersonList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getSortedFilteredPersonList(), model.getSortedFilteredPersonList());
    }

    @Test
    public void execute_noMatchingPersons_emptyList() {
        // A date range far in the future that no typical person would be busy in
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2099", "31/12/2099");
        BusyFilterCommand command = new BusyFilterCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 0);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(0, model.getSortedFilteredPersonList().size());
    }

    @Test
    public void equals_samePredicate_returnsTrue() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand command1 = new BusyFilterCommand(predicate);
        BusyFilterCommand command2 = new BusyFilterCommand(predicate);

        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand command = new BusyFilterCommand(predicate);

        assertTrue(command.equals(command));
    }

    @Test
    public void equals_null_returnsFalse() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand command = new BusyFilterCommand(predicate);

        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand command = new BusyFilterCommand(predicate);

        assertFalse(command.equals(new ClearCommand()));
    }

    @Test
    public void equals_differentPredicate_returnsFalse() {
        BusyInDateRangePredicate predicate1 = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyInDateRangePredicate predicate2 = new BusyInDateRangePredicate("01/06/2026", "31/12/2026");
        BusyFilterCommand command1 = new BusyFilterCommand(predicate1);
        BusyFilterCommand command2 = new BusyFilterCommand(predicate2);

        assertFalse(command1.equals(command2));
    }

    @Test
    public void toStringMethod() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand command = new BusyFilterCommand(predicate);
        String expected = BusyFilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void getCommandWord() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand command = new BusyFilterCommand(predicate);
        assertEquals(BusyFilterCommand.COMMAND_WORD, command.getCommandWord());
    }
}
