package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmAddCommand;
import seedu.address.logic.commands.ConfirmClearCommand;
import seedu.address.logic.commands.ConfirmDeleteCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_commandAwaitingConfirmation_success() throws Exception {
        // Add a person
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + ROLE_DESC_AMY;
        logic.execute(addCommand);
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();

        // Delete person 1 (first step: confirmation)
        String deleteCommand = "delete 1";
        String expectedMessage = String.format(ConfirmDeleteCommand.MESSAGE_ASK_CONFIRMATION,
                Messages.format(expectedPerson));
        CommandResult result = logic.execute(deleteCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        // Confirm (second step: execution)
        String confirmCommand = "y";
        String expectedDeleteMessage = String.format(DeleteCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson));
        CommandResult confirmResult = logic.execute(confirmCommand);
        assertEquals(expectedDeleteMessage, confirmResult.getFeedbackToUser());

        // Verify address book is empty
        assertEquals(0, model.getAddressBook().getPersonList().size());
    }

    @Test
    public void execute_commandAwaitingConfirmation_cancel() throws Exception {
        // Add a person
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + ROLE_DESC_AMY;
        logic.execute(addCommand);

        // Delete person 1 (first step: confirmation)
        String deleteCommand = "delete 1";
        logic.execute(deleteCommand);

        // Cancel (second step: execution)
        String cancelCommand = "n";
        CommandResult cancelResult = logic.execute(cancelCommand);
        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_SUCCESSFUL_CANCEL,
                "Delete"), cancelResult.getFeedbackToUser());

        // Verify person is still in address book
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    @Test
    public void execute_commandAwaitingConfirmation_invalidInputthrowsParseException() throws Exception {
        // Add a person
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + ROLE_DESC_AMY;
        logic.execute(addCommand);

        // Delete person 1 (first step: confirmation)
        String deleteCommand = "delete 1";
        logic.execute(deleteCommand);

        // Invalid input
        String invalidCommand = "invalid";
        assertParseException(invalidCommand, seedu.address.logic.Messages.MESSAGE_ONLY_YES_NO);
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                logic.getFilteredPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    /**
     * Executes {@code AddCommand} twice and verifies that adding a duplicate person
     * triggers a confirmation prompt instead of being added immediately.
     */
    @Test
    public void executeAddDuplicatePerson_requiresConfirmation() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();

        logic.execute(addCommand);

        CommandResult result = logic.execute(addCommand);

        assertEquals(
                String.format(ConfirmAddCommand.MESSAGE_CONFIRM_DUPLICATE_PERSON, Messages.format(expectedPerson)),
                result.getFeedbackToUser());
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes {@code AddCommand} on a duplicate person, confirms the operation,
     * and verifies that the duplicate person is added successfully.
     */
    @Test
    public void executeAddDuplicatePerson_confirmed_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();

        logic.execute(addCommand);
        logic.execute(addCommand);
        CommandResult result = logic.execute("y");

        assertEquals(
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(expectedPerson)),
                result.getFeedbackToUser());
        assertEquals(2, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes {@code AddCommand} on a duplicate person, rejects the confirmation,
     * and verifies that no additional person is added.
     */
    @Test
    public void executeAddDuplicatePerson_cancelled_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;

        logic.execute(addCommand);
        logic.execute(addCommand);
        CommandResult result = logic.execute("n");

        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_SUCCESSFUL_CANCEL, "Add"),
                result.getFeedbackToUser());
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes {@code DeleteCommand} and verifies that the command requires
     * confirmation before the person is deleted.
     */
    @Test
    public void executeDeleteCommand_requiresConfirmation() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        logic.execute(addCommand);

        Person expectedPerson = new PersonBuilder(AMY).withTags().build();

        CommandResult result = logic.execute(DeleteCommand.COMMAND_WORD + " 1");

        assertEquals(
                String.format(ConfirmDeleteCommand.MESSAGE_ASK_CONFIRMATION, Messages.format(expectedPerson)),
                result.getFeedbackToUser());
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes {@code DeleteCommand}, confirms the operation, and verifies that
     * the person is deleted successfully.
     */
    @Test
    public void executeDeleteCommand_confirmed_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        logic.execute(addCommand);

        Person expectedPerson = new PersonBuilder(AMY).withTags().build();

        logic.execute(DeleteCommand.COMMAND_WORD + " 1");
        CommandResult result = logic.execute("y");

        assertEquals(
                String.format(DeleteCommand.MESSAGE_SUCCESS, Messages.format(expectedPerson)),
                result.getFeedbackToUser());
        assertEquals(0, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes {@code ClearCommand} and verifies that the command requires
     * confirmation before listed persons are cleared.
     */
    @Test
    public void executeClearCommand_requiresConfirmation() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        logic.execute(addCommand);

        Person expectedPerson = new PersonBuilder(AMY).withTags().build();

        CommandResult result = logic.execute(ClearCommand.COMMAND_WORD);

        assertEquals(
                String.format(ConfirmClearCommand.MESSAGE_ASK_CONFIRMATION, "\n" + Messages.format(expectedPerson)),
                result.getFeedbackToUser());
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes {@code ClearCommand}, confirms the operation, and verifies that
     * listed persons are cleared successfully.
     */
    @Test
    public void executeClearCommand_confirmed_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        logic.execute(addCommand);

        logic.execute(ClearCommand.COMMAND_WORD);
        CommandResult result = logic.execute("y");

        String expectedMessage = String.format(ClearCommand.MESSAGE_SUCCESS, 1);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(0, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes {@code ClearCommand}, rejects the confirmation, and verifies that
     * no person is removed.
     */
    @Test
    public void executeClearCommand_cancelled_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        logic.execute(addCommand);

        logic.execute(ClearCommand.COMMAND_WORD);
        CommandResult result = logic.execute("n");

        assertEquals(String.format(seedu.address.logic.Messages.MESSAGE_SUCCESSFUL_CANCEL, "Clear"),
                result.getFeedbackToUser());
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    /**
     * Executes a command that is awaiting confirmation and verifies that any input
     * other than {@code y} or {@code n} results in a {@code ParseException}.
     */
    @Test
    public void executeCommandAwaitingConfirmation_invalidInput_throwsParseException() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + ROLE_DESC_AMY + NAME_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;

        logic.execute(addCommand);
        logic.execute(addCommand);

        assertParseException("maybe", seedu.address.logic.Messages.MESSAGE_ONLY_YES_NO);
    }

}
