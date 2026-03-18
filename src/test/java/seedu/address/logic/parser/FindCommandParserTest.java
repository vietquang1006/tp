package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() throws CommandException {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "name Alice ; Bob", expectedFindCommand);

        FindCommand expectedSemicolonCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("alice pauline", "josh")));
        assertParseSuccess(parser, "name alice pauline ; josh", expectedSemicolonCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() throws CommandException {
        FindCommand expectedFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friend", "classmate")));
        assertParseSuccess(parser, "tag friend ; classmate", expectedFindCommand);

        FindCommand expectedSemicolonCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friends", "owes me", "secretary")));
        assertParseSuccess(parser, "tag friends ; owes me ; secretary", expectedSemicolonCommand);
    }

    @Test
    public void parse_modeOnly_throwsParseException() {
        assertParseFailure(parser, "name", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "tag", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unknownSearchMode_throwsParseException() {
        assertParseFailure(parser, "phone Alice",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidKeyword_throwsCommandException() {
        assertParseFailure(parser, "name Alice Bob!", MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER);
        assertParseFailure(parser, "tag friend!", MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER);
        assertParseFailure(parser, "name alice ; ; bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
