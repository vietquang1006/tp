package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException, CommandException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] modeAndQuery = trimmedArgs.split("\\s+", 2);
        if (modeAndQuery.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String searchBy = modeAndQuery[0];
        List<String> keywords = parseKeywords(modeAndQuery[1]);

        if (searchBy.equals("name")) {
            return new FindCommand(new NameContainsKeywordsPredicate(keywords));
        } else if (searchBy.equals("tag")) {
            return new FindCommand(new TagContainsKeywordsPredicate(keywords));
        } else {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

    }

    private List<String> parseKeywords(String rawQuery) throws ParseException {
        String trimmedQuery = rawQuery.trim();
        if (trimmedQuery.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Stream<String> keywordStream = trimmedQuery.contains(";")
                ? Stream.of(trimmedQuery.split(";"))
                : Stream.of(trimmedQuery.split("\\s+"));

        List<String> keywords = keywordStream
                .map(String::trim)
                .collect(Collectors.toList());

        if (keywords.isEmpty() || keywords.stream().anyMatch(String::isEmpty)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return keywords;
    }

}
