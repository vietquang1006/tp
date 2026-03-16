package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;

public class ListCommandParserTest {

    private ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArgs_returnsNone() throws Exception {
        ListCommand command = parser.parse("");
        assertEquals(ListCommand.SortOrder.NONE, command.getSortOrder());
    }

    @Test
    public void parse_sortOnly_returnsAscending() throws Exception {
        ListCommand command = parser.parse("sort");
        assertEquals(ListCommand.SortOrder.ASCENDING, command.getSortOrder());
    }

    @Test
    public void parse_sortAscending_returnsAscending() throws Exception {
        ListCommand command = parser.parse("ascending");
        assertEquals(ListCommand.SortOrder.ASCENDING, command.getSortOrder());
    }

    @Test
    public void parse_sortDescending_returnsDescending() throws Exception {
        ListCommand command = parser.parse("descending");
        assertEquals(ListCommand.SortOrder.DESCENDING, command.getSortOrder());
    }

    @Test
    public void parse_sortReverse_returnsDescending() throws Exception {
        ListCommand command = parser.parse("reverse");
        assertEquals(ListCommand.SortOrder.DESCENDING, command.getSortOrder());
    }

    @Test
    public void parse_invalidFirstWord_throwsParseException() {
        assertParseFailure(parser, "hello",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidOrder_throwsParseException() {
        assertParseFailure(parser, "sort random",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tooManyArguments_throwsParseException() {
        assertParseFailure(parser, "sort ascending extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }
}
