package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ListCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() throws ParseException {
        assertCommandSuccess(new ListCommandParser().parse(""), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() throws CommandException, ParseException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommandParser().parse(""), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_sortAscending_success() throws Exception {
        ListCommand command = new ListCommand(ListCommand.SortOrder.ASCENDING);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateSortedPersonList(Model.SORT_BY_NAME_ASCENDING);

        assertCommandSuccess(command, model,
                ListCommand.MESSAGE_SUCCESS_SORT_ASCENDING, expectedModel);
    }

    @Test
    public void execute_sortDescending_success() throws Exception {
        ListCommand command = new ListCommand(ListCommand.SortOrder.DESCENDING);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateSortedPersonList(Model.SORT_BY_NAME_DESCENDING);

        assertCommandSuccess(command, model,
                ListCommand.MESSAGE_SUCCESS_SORT_DESCENDING, expectedModel);
    }
}
