package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.BusyPeriod;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for BusyCommand.
 */
public class BusyCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToBusy = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        BusyPeriod busyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");
        BusyCommand busyCommand = new BusyCommand(INDEX_FIRST_PERSON, Optional.of(busyPeriod));

        String expectedMessage = String.format(BusyCommand.MESSAGE_BUSY_PERSON_SUCCESS,
                personToBusy.getName(), busyPeriod.toString());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person busyPerson = new PersonBuilder(personToBusy).withBusyPeriod("25/03/2026", "28/03/2026").build();
        expectedModel.setPerson(personToBusy, busyPerson);

        assertCommandSuccess(busyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredListOverwrite_success() {
        // First set a busy period
        Person personToBusy = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithInitialBusy = new PersonBuilder(personToBusy)
                .withBusyPeriod("01/01/2026", "02/01/2026").build();
        model.setPerson(personToBusy, personWithInitialBusy);

        // Now overwrite it
        BusyPeriod newBusyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");
        BusyCommand busyCommand = new BusyCommand(INDEX_FIRST_PERSON, Optional.of(newBusyPeriod));

        String expectedMessage = String.format(BusyCommand.MESSAGE_BUSY_PERSON_SUCCESS,
                personToBusy.getName(), newBusyPeriod.toString());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person busyPerson = new PersonBuilder(personToBusy).withBusyPeriod("25/03/2026", "28/03/2026").build();
        expectedModel.setPerson(personWithInitialBusy, busyPerson);

        assertCommandSuccess(busyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_identicalBusyPeriodUnfilteredList_failure() {
        Person personToBusy = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        BusyPeriod busyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");
        Person busyPerson = new PersonBuilder(personToBusy).withBusyPeriod("25/03/2026", "28/03/2026").build();
        model.setPerson(personToBusy, busyPerson);

        BusyCommand busyCommand = new BusyCommand(INDEX_FIRST_PERSON, Optional.of(busyPeriod));
        String expectedMessage = String.format(BusyCommand.MESSAGE_IDENTICAL_BUSY_PERIOD,
                personToBusy.getName(), busyPeriod.toString());

        assertCommandFailure(busyCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        BusyPeriod busyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");
        BusyCommand busyCommand = new BusyCommand(outOfBoundIndex, Optional.of(busyPeriod));

        assertCommandFailure(busyCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        BusyPeriod busyPeriod1 = new BusyPeriod("25/03/2026", "28/03/2026");
        BusyPeriod busyPeriod2 = new BusyPeriod("01/04/2026", "05/04/2026");
        final BusyCommand standardCommand = new BusyCommand(INDEX_FIRST_PERSON, Optional.of(busyPeriod1));

        // same values -> returns true
        BusyCommand commandWithSameValues = new BusyCommand(INDEX_FIRST_PERSON, Optional.of(busyPeriod1));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new BusyCommand(INDEX_SECOND_PERSON, Optional.of(busyPeriod1))));

        // different busyPeriod -> returns false
        assertFalse(standardCommand.equals(new BusyCommand(INDEX_FIRST_PERSON, Optional.of(busyPeriod2))));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Optional<BusyPeriod> busyPeriod = Optional.of(new BusyPeriod("25/03/2026", "28/03/2026"));
        BusyCommand busyCommand = new BusyCommand(index, busyPeriod);
        String expected = BusyCommand.class.getCanonicalName() + "{index=" + index + ", busyPeriod="
                + busyPeriod + "}";
        assertEquals(expected, busyCommand.toString());
    }

}
