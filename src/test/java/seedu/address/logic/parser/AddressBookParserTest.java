package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_ONLY_YES_NO;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.BusyCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ConfirmAddCommand;
import seedu.address.logic.commands.ConfirmClearCommand;
import seedu.address.logic.commands.ConfirmDeleteCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.BusyPeriod;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_busy() throws Exception {
        BusyPeriod busyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");
        BusyCommand command = (BusyCommand) parser.parseCommand(BusyCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " -s 25/03/2026 -e 28/03/2026");
        assertEquals(new BusyCommand(INDEX_FIRST_PERSON, Optional.of(busyPeriod)), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " name " + keywords.stream().collect(Collectors.joining(" ; ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findByTag() throws Exception {
        List<String> keywords = Arrays.asList("friend", "classmate");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " tag " + keywords.stream().collect(Collectors.joining(" ; ")));
        assertEquals(new FindCommand(new TagContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findWithSemicolonGroups() throws Exception {
        FindCommand nameCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " name alice pauline ; josh");
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("alice pauline", "josh"))),
                nameCommand);

        FindCommand tagCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " tag friends ; owes me ; secretary");
        assertEquals(new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friends", "owes me",
                "secretary"))), tagCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list_invalid() throws Exception {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () ->
                        parser.parseCommand(ListCommand.COMMAND_WORD + " 3")
        );
    }

    @Test
    public void parseCommand_list_valid() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " sort") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommandWithConfirmation_add() throws Exception {
        Person person = new PersonBuilder().build();
        ConfirmAddCommand command =
                (ConfirmAddCommand) parser.parseCommandWithConfirmation(PersonUtil.getAddCommand(person));
        assertEquals(new ConfirmAddCommand(person), command);
    }

    @Test
    public void parseCommandWithConfirmation_busy() throws Exception {
        BusyPeriod busyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");
        BusyCommand command = (BusyCommand) parser.parseCommandWithConfirmation(BusyCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " -s 25/03/2026 -e 28/03/2026");
        assertEquals(new BusyCommand(INDEX_FIRST_PERSON, Optional.of(busyPeriod)), command);
    }

    @Test
    public void parseCommandWithConfirmation_delete() throws Exception {
        ConfirmDeleteCommand command = (ConfirmDeleteCommand) parser.parseCommandWithConfirmation(
                ConfirmDeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ConfirmDeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommandWithConfirmation_clear() throws Exception {
        assertTrue(parser.parseCommandWithConfirmation(ClearCommand.COMMAND_WORD) instanceof ConfirmClearCommand);
        assertTrue(parser.parseCommandWithConfirmation(ClearCommand.COMMAND_WORD + " 3")
                instanceof ConfirmClearCommand);
    }

    @Test
    public void parseCommandWithConfirmation_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommandWithConfirmation(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommandWithConfirmation_exit() throws Exception {
        assertTrue(parser.parseCommandWithConfirmation(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommandWithConfirmation(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommandWithConfirmation_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommandWithConfirmation(
                FindCommand.COMMAND_WORD + " name " + keywords.stream().collect(Collectors.joining(" ; ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommandWithConfirmation_help() throws Exception {
        assertTrue(parser.parseCommandWithConfirmation(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommandWithConfirmation(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommandWithConfirmation_list() throws Exception {
        assertTrue(parser.parseCommandWithConfirmation(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommandWithConfirmation(ListCommand.COMMAND_WORD + " sort") instanceof ListCommand);
    }

    @Test
    public void parseCommandWithConfirmation_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommandWithConfirmation(""));
    }

    @Test
    public void parseCommandWithConfirmation_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommandWithConfirmation("unknownCommand"));
    }

    @Test
    public void parseYesNo_yes() throws Exception {
        assertTrue(parser.parseYesNo("y"));
        assertTrue(parser.parseYesNo("y ")); // with trailing space
    }

    @Test
    public void parseYesNo_no() throws Exception {
        assertTrue(!parser.parseYesNo("n"));
        assertTrue(!parser.parseYesNo("n ")); // with trailing space
    }

    @Test
    public void parseYesNo_invalid_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_ONLY_YES_NO, () -> parser.parseYesNo("invalid"));
        assertThrows(ParseException.class, MESSAGE_ONLY_YES_NO, () -> parser.parseYesNo("yes"));
        assertThrows(ParseException.class, MESSAGE_ONLY_YES_NO, () -> parser.parseYesNo("no"));
    }

    @Test
    public void parseYesNo_empty_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseYesNo(""));
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseYesNo("   "));
    }

}
