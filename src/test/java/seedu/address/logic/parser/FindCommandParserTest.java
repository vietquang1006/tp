package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.KeywordRelation;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NameTagContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
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

        FindCommand expectedCaseInsensitiveRelationCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friend", "classmate"),
                        KeywordRelation.ANY));
        assertParseSuccess(parser, " -t friend ; classmate -m Or", expectedCaseInsensitiveRelationCommand);
    }

    @Test
    public void parse_validNameAndTagArgs_returnsFindCommand() throws CommandException {
        FindCommand expectedFindCommand = new FindCommand(
                new NameTagContainsKeywordsPredicate(Collections.singletonList("meier"),
                        Collections.singletonList("friends")));
        assertParseSuccess(parser, " -n meier -t friends", expectedFindCommand);

        FindCommand expectedMixedCommand = new FindCommand(
                new NameTagContainsKeywordsPredicate(Arrays.asList("dan", "elle"), Arrays.asList("friends", "student"),
                        KeywordRelation.ALL, KeywordRelation.ANY));
        assertParseSuccess(parser, " -n dan ; elle -m and -t friends ; student -m or", expectedMixedCommand);

        FindCommand expectedReversedOrderCommand = new FindCommand(
                new NameTagContainsKeywordsPredicate(Arrays.asList("dan", "elle"), Arrays.asList("friends", "student"),
                        KeywordRelation.ALL, KeywordRelation.ANY));
        assertParseSuccess(parser, " -t friends ; student -m or -n dan ; elle -m and", expectedReversedOrderCommand);
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

    @Test
    public void parse_trailingSemicolon_throwsParseException() {
        String invalidFormatMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " -n Alice ;", invalidFormatMessage);
        assertParseFailure(parser, " -t friend ;", invalidFormatMessage);
        assertParseFailure(parser, " -n alice ; -m and", invalidFormatMessage);
    }

    @Test
    public void parse_validArgsWithoutLeadingWhitespace_returnsFindCommand() throws CommandException {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "-n Alice ; Bob", expectedFindCommand);

        FindCommand expectedTagCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friend", "classmate")));
        assertParseSuccess(parser, "-t friend ; classmate", expectedTagCommand);
    }

    @Test
    public void parse_invalidSegmentStructure_throwsParseException() {
        String invalidFormatMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "abc -n Alice", invalidFormatMessage);
        assertParseFailure(parser, " -n Alice -n Bob", invalidFormatMessage);
        assertParseFailure(parser, " -t friend -t classmate", invalidFormatMessage);
        assertParseFailure(parser, " -n Alice -m", invalidFormatMessage);
        assertParseFailure(parser, " -n Alice -m and or", invalidFormatMessage);
        assertParseFailure(parser, " -n -m and", invalidFormatMessage);
        assertParseFailure(parser, " -t friend -m or -m and", invalidFormatMessage);
        assertParseFailure(parser, " -n Alice -t -m or", invalidFormatMessage);
    }

    @Test
    public void parseSegment_nullPrefix_throwsAssertionError() throws Exception {
        assertTrue(FindCommandParser.class.desiredAssertionStatus(),
                "Assertions must be enabled for assert-focused tests");

        Method parseSegment = FindCommandParser.class.getDeclaredMethod("parseSegment", Prefix.class, String.class);
        parseSegment.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class, () -> parseSegment.invoke(parser, null, "Alice"));
        assertTrue(thrown.getCause() instanceof AssertionError);
    }

    @Test
    public void parsedSegment_nullPrefix_throwsAssertionError() throws Exception {
        assertTrue(FindCommandParser.class.desiredAssertionStatus(),
                "Assertions must be enabled for assert-focused tests");

        Class<?> parsedSegmentClass = Class.forName("seedu.address.logic.parser.FindCommandParser$ParsedSegment");
        Constructor<?> constructor = parsedSegmentClass.getDeclaredConstructor(
                Prefix.class, java.util.List.class, KeywordRelation.class);
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class, () -> constructor.newInstance(
                        null, Arrays.asList("alice", "bob"), KeywordRelation.ANY));
        assertTrue(thrown.getCause() instanceof AssertionError);
    }

    @Test
    public void parsedSegment_nullKeywords_throwsAssertionError() throws Exception {
        assertTrue(FindCommandParser.class.desiredAssertionStatus(),
                "Assertions must be enabled for assert-focused tests");

        Class<?> parsedSegmentClass = Class.forName("seedu.address.logic.parser.FindCommandParser$ParsedSegment");
        Constructor<?> constructor = parsedSegmentClass.getDeclaredConstructor(
                Prefix.class, java.util.List.class, KeywordRelation.class);
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class, () -> constructor.newInstance(
                        CliSyntax.PREFIX_NAME, null, KeywordRelation.ANY));
        assertTrue(thrown.getCause() instanceof AssertionError);
    }

    @Test
    public void parsedSegment_nullRelation_throwsAssertionError() throws Exception {
        assertTrue(FindCommandParser.class.desiredAssertionStatus(),
                "Assertions must be enabled for assert-focused tests");

        Class<?> parsedSegmentClass = Class.forName("seedu.address.logic.parser.FindCommandParser$ParsedSegment");
        Constructor<?> constructor = parsedSegmentClass.getDeclaredConstructor(
                Prefix.class, java.util.List.class, KeywordRelation.class);
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class, () -> constructor.newInstance(
                        CliSyntax.PREFIX_NAME, Arrays.asList("alice", "bob"), null));
        assertTrue(thrown.getCause() instanceof AssertionError);
    }

    @Test
    public void parsedFindSegments_nullRelation_throwsAssertionError() throws Exception {
        assertTrue(FindCommandParser.class.desiredAssertionStatus(),
                "Assertions must be enabled for assert-focused tests");

        Class<?> parsedFindSegmentsClass = Class.forName(
                "seedu.address.logic.parser.FindCommandParser$ParsedFindSegments");
        Constructor<?> constructor = parsedFindSegmentsClass.getDeclaredConstructor(
                java.util.List.class, java.util.List.class, KeywordRelation.class, KeywordRelation.class);
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class, () -> constructor.newInstance(
                        Arrays.asList("alice", "bob"), Arrays.asList("friend", "student"),
                        null, KeywordRelation.ANY));
        assertTrue(thrown.getCause() instanceof AssertionError);
    }

    @Test
    public void parsedFindSegments_nullTagRelation_throwsAssertionError() throws Exception {
        assertTrue(FindCommandParser.class.desiredAssertionStatus(),
                "Assertions must be enabled for assert-focused tests");

        Class<?> parsedFindSegmentsClass = Class.forName(
                "seedu.address.logic.parser.FindCommandParser$ParsedFindSegments");
        Constructor<?> constructor = parsedFindSegmentsClass.getDeclaredConstructor(
                java.util.List.class, java.util.List.class, KeywordRelation.class, KeywordRelation.class);
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class, () -> constructor.newInstance(
                        Arrays.asList("alice", "bob"), Arrays.asList("friend", "student"),
                        KeywordRelation.ANY, null));
        assertTrue(thrown.getCause() instanceof AssertionError);
    }
}
