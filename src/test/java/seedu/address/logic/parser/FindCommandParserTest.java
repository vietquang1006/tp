package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.KeywordRelation;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NameTagContainsKeywordsPredicate;
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
        assertParseSuccess(parser, " -n Alice ; Bob", expectedFindCommand);

        FindCommand expectedSemicolonCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("alice pauline", "josh")));
        assertParseSuccess(parser, " -n alice pauline ; josh", expectedSemicolonCommand);

        FindCommand expectedAndCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"), KeywordRelation.ALL));
        assertParseSuccess(parser, " -n Alice ; Bob -m and", expectedAndCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() throws CommandException {
        FindCommand expectedFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friend", "classmate")));
        assertParseSuccess(parser, " -t friend ; classmate", expectedFindCommand);

        FindCommand expectedSemicolonCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friends", "owes me", "secretary")));
        assertParseSuccess(parser, " -t friends ; owes me ; secretary", expectedSemicolonCommand);

        FindCommand expectedOrCommand =
                new FindCommand(
                        new TagContainsKeywordsPredicate(Arrays.asList("friend", "classmate"),
                                KeywordRelation.ANY));
        assertParseSuccess(parser, " -t friend ; classmate -m or", expectedOrCommand);
    }

    @Test
    public void parse_validNameAndTagArgs_returnsFindCommand() throws CommandException {
        FindCommand expectedFindCommand = new FindCommand(
                new NameTagContainsKeywordsPredicate(Arrays.asList("meier"), Arrays.asList("friends")));
        assertParseSuccess(parser, " -n meier -t friends", expectedFindCommand);

        FindCommand expectedMixedCommand = new FindCommand(
                new NameTagContainsKeywordsPredicate(Arrays.asList("dan", "elle"), Arrays.asList("friends", "student"),
                        KeywordRelation.ALL, KeywordRelation.ANY));
        assertParseSuccess(parser, " -n dan ; elle -m and -t friends ; student -m or", expectedMixedCommand);
    }

    @Test
    public void parse_modeOnly_throwsParseException() {
        assertParseFailure(parser, " -n", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " -t", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unknownSearchMode_throwsParseException() {
        assertParseFailure(parser, " phone Alice",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidKeyword_throwsCommandException() {
        assertParseFailure(parser, " -n Alice Bob!", MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER);
        assertParseFailure(parser, " -t friend!", MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER);
        assertParseFailure(parser, " -n alice ; ; bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " -n alice -m maybe",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " -n alice -m and -m or",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
