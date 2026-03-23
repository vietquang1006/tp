package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;

import java.util.stream.Stream;

import seedu.address.logic.commands.BusyFilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.BusyInDateRangePredicate;
import seedu.address.model.person.BusyPeriod;

/**
 * Parses input arguments and creates a new BusyFilterCommand object
 */
public class BusyFilterCommandParser implements Parser<BusyFilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the BusyFilterCommand
     * and returns a BusyFilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public BusyFilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_START_DATE, PREFIX_END_DATE);

        // No preamble should be present and both start and end date prefixes must be present
        if (!arePrefixesPresent(argMultimap, PREFIX_START_DATE, PREFIX_END_DATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BusyFilterCommand.MESSAGE_USAGE));
        }

        // No "-s xxx -s" or "-e xxx -e"
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_START_DATE, PREFIX_END_DATE);

        String startDateStr = argMultimap.getValue(PREFIX_START_DATE).get();
        String endDateStr = argMultimap.getValue(PREFIX_END_DATE).get();

        if (!BusyPeriod.isValidDateFormat(startDateStr) || !BusyPeriod.isValidDateFormat(endDateStr)) {
            throw new ParseException(BusyPeriod.MESSAGE_CONSTRAINTS);
        }

        BusyInDateRangePredicate predicate;
        try {
            predicate = new BusyInDateRangePredicate(startDateStr, endDateStr);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }

        return new BusyFilterCommand(predicate);
    }

    /**
     * Returns true if all the prefixes contain non-empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

