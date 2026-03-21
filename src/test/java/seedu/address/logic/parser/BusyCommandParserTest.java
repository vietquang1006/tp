package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.BusyCommand;
import seedu.address.model.person.BusyPeriod;

public class BusyCommandParserTest {
    private BusyCommandParser parser = new BusyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        BusyPeriod expectedBusyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");
        assertParseSuccess(parser, "1 -s 25/03/2026 -e 28/03/2026",
                new BusyCommand(INDEX_FIRST_PERSON, Optional.of(expectedBusyPeriod)));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyCommand.MESSAGE_USAGE);

        // missing index
        assertParseFailure(parser, "-s 25/03/2026 -e 28/03/2026", expectedMessage);

        // missing start date prefix
        assertParseFailure(parser, "1 25/03/2026 -e 28/03/2026", expectedMessage);

        // missing end date prefix
        assertParseFailure(parser, "1 -s 25/03/2026 28/03/2026", expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, "1 25/03/2026 28/03/2026", expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid index
        assertParseFailure(parser, "a -s 25/03/2026 -e 28/03/2026", 
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyCommand.MESSAGE_USAGE));

        // invalid start date
        assertParseFailure(parser, "1 -s 25-03-2026 -e 28/03/2026", BusyPeriod.MESSAGE_CONSTRAINTS);

        // invalid end date
        assertParseFailure(parser, "1 -s 25/03/2026 -e 28-03-2026", BusyPeriod.MESSAGE_CONSTRAINTS);

        // start date later than end date
        assertParseFailure(parser, "1 -s 28/03/2026 -e 25/03/2026", BusyPeriod.MESSAGE_DATE_LOGIC);
    }
}
