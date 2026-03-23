package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.BusyFilterCommand;
import seedu.address.model.person.BusyInDateRangePredicate;
import seedu.address.model.person.BusyPeriod;

public class BusyFilterCommandParserTest {

    private final BusyFilterCommandParser parser = new BusyFilterCommandParser();

    @Test
    public void parse_validArgs_returnsBusyFilterCommand() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyFilterCommand expectedCommand = new BusyFilterCommand(predicate);
        assertParseSuccess(parser, " -s 01/01/2026 -e 31/12/2026", expectedCommand);
        assertParseSuccess(parser, "  -s  01/01/2026  -e  31/12/2026  ", expectedCommand);
    }

    @Test
    public void parse_sameDates_returnsBusyFilterCommand() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("15/06/2026", "15/06/2026");
        assertParseSuccess(parser, " -s 15/06/2026 -e 15/06/2026", new BusyFilterCommand(predicate));
    }

    @Test
    public void parse_missingStartPrefix_throwsParseException() {
        assertParseFailure(parser, " 01/01/2026 -e 31/12/2026",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyFilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingEndPrefix_throwsParseException() {
        assertParseFailure(parser, " -s 01/01/2026 31/12/2026",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyFilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothPrefixesMissing_throwsParseException() {
        assertParseFailure(parser, " 01/01/2026 31/12/2026",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyFilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyFilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertParseFailure(parser, " extra -s 01/01/2026 -e 31/12/2026",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyFilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateStartPrefix_throwsParseException() {
        assertParseFailure(parser, " -s 01/01/2026 -s 02/01/2026 -e 31/12/2026",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_START_DATE));
    }

    @Test
    public void parse_duplicateEndPrefix_throwsParseException() {
        assertParseFailure(parser, " -s 01/01/2026 -e 30/12/2026 -e 31/12/2026",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_END_DATE));
    }

    @Test
    public void parse_invalidStartDateFormat_throwsParseException() {
        assertParseFailure(parser, " -s 2026-01-01 -e 31/12/2026",
                BusyPeriod.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidEndDateFormat_throwsParseException() {
        assertParseFailure(parser, " -s 01/01/2026 -e 31-12-2026",
                BusyPeriod.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonExistentDate_throwsParseException() {
        // 30 Feb does not exist
        assertParseFailure(parser, " -s 30/02/2026 -e 31/12/2026",
                BusyPeriod.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_startAfterEnd_throwsParseException() {
        assertParseFailure(parser, " -s 27/03/2026 -e 26/03/2026",
                BusyPeriod.MESSAGE_DATE_LOGIC);
    }
}
