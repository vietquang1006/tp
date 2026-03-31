package seedu.address.logic;

import static seedu.address.logic.Messages.MESSAGE_SUCCESSFUL_CANCEL;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    private boolean isAwaitingConfirmation;
    private Command awaitingCommand;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
        this.isAwaitingConfirmation = false;
        this.awaitingCommand = null;
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult = null;

        if (!isAwaitingConfirmation) {
            commandResult = handleNotAwaitingExecute(commandText);
        } else {
            commandResult = handleAwaitingExecute(commandText);
        }
        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    private CommandResult handleNotAwaitingExecute(String commandText)
            throws CommandException, ParseException {
        Command command = addressBookParser.parseCommandWithConfirmation(commandText);
        // either execute a ConfirmXXXCommand (e.g. ConfirmDeleteCommand) or a normal command
        // that has no confirmation step (e.g. list)
        CommandResult commandResult = command.execute(model);
        boolean thisCommandRequiresConfirmation = commandResult.isAwaitingConfirmation();
        if (thisCommandRequiresConfirmation) {
            this.isAwaitingConfirmation = true;
            this.awaitingCommand = addressBookParser.parseCommand(commandText);
        }
        return commandResult;
    }

    private CommandResult handleAwaitingExecute(String commandText)
            throws CommandException, ParseException {
        boolean hasConfirmed = addressBookParser.parseYesNo(commandText);
        CommandResult commandResult = null;
        if (hasConfirmed) {
            commandResult = awaitingCommand.execute(model);
        } else {
            String awaitingCommandWord = awaitingCommand.getCommandWord();
            String properAwaitingCommandWord = awaitingCommandWord.substring(0, 1).toUpperCase()
                    + awaitingCommandWord.substring(1).toLowerCase();
            commandResult = new CommandResult(String.format(MESSAGE_SUCCESSFUL_CANCEL, properAwaitingCommandWord));
        }
        this.isAwaitingConfirmation = false;
        this.awaitingCommand = null;
        return commandResult;
    }


    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getSortedFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
