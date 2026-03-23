package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ConfirmEditCommand.
 */
public class ConfirmEditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person personToEdit = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(new PersonBuilder().build()).build();
        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(ConfirmEditCommand.MESSAGE_ASK_CONFIRMATION,
                Messages.format(personToEdit));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);

        assertCommandSuccess(confirmEditCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getSortedFilteredPersonList().size());
        Person lastPerson = model.getSortedFilteredPersonList().get(indexLastPerson.getZeroBased());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(ConfirmEditCommand.MESSAGE_ASK_CONFIRMATION,
                Messages.format(lastPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);

        assertCommandSuccess(confirmEditCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(ConfirmEditCommand.MESSAGE_ASK_CONFIRMATION,
                Messages.format(personInFilteredList));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);

        assertCommandSuccess(confirmEditCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_warningShown() {
        Person firstPerson = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getSortedFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(INDEX_SECOND_PERSON, descriptor);

        String expectedMessage =
                String.format(ConfirmEditCommand.MESSAGE_DUPLICATE_PERSON_WARNING, Messages.format(firstPerson))
                + String.format(ConfirmEditCommand.MESSAGE_ASK_CONFIRMATION, Messages.format(secondPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);

        assertCommandSuccess(confirmEditCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_duplicatePersonFilteredList_warningShown() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person duplicatePerson = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(duplicatePerson).build());

        String expectedMessage =
                String.format(ConfirmEditCommand.MESSAGE_DUPLICATE_PERSON_WARNING, Messages.format(duplicatePerson))
                + String.format(ConfirmEditCommand.MESSAGE_ASK_CONFIRMATION, Messages.format(personInFilteredList));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true);

        assertCommandSuccess(confirmEditCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(confirmEditCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book.
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(confirmEditCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final ConfirmEditCommand standardCommand = new ConfirmEditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        ConfirmEditCommand commandWithSameValues = new ConfirmEditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        assertTrue(standardCommand.equals(standardCommand));

        assertFalse(standardCommand.equals(null));

        assertFalse(standardCommand.equals(new ClearCommand()));

        assertFalse(standardCommand.equals(new ConfirmEditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        assertFalse(standardCommand.equals(new ConfirmEditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        ConfirmEditCommand confirmEditCommand = new ConfirmEditCommand(index, editPersonDescriptor);
        String expected = ConfirmEditCommand.class.getCanonicalName() + "{index=" + index
                + ", editPersonDescriptor=" + editPersonDescriptor + "}";
        assertEquals(expected, confirmEditCommand.toString());
    }
}
