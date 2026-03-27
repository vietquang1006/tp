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
    private static final String SEARCH_MODE_NAME = "name";
    private static final String SEARCH_MODE_TAG = "tag";


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


        return parseByMode(searchBy, keywords);
    }

    /**
     * Parses the query segment after the search mode into keyword groups.
     */
    private List<String> parseKeywords(String rawQuery) throws ParseException {

        String trimmedQuery = rawQuery.trim();
        if (trimmedQuery.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // If not ; means the whole word itself is the keyword
        Stream<String> keywordStream = trimmedQuery.contains(";")
                ? Stream.of(trimmedQuery.split(";"))
                : Stream.of(rawQuery);

        List<String> keywords = keywordStream
                .map(this::normalizeWhitespace)
                .collect(Collectors.toList());

        if (keywords.isEmpty() || keywords.stream().anyMatch(String::isEmpty)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return keywords;
    }

    private FindCommand parseByMode(String searchBy, List<String> keywords) throws ParseException, CommandException {
        switch (searchBy) {
        case SEARCH_MODE_NAME:
            return new FindCommand(new NameContainsKeywordsPredicate(keywords));
        case SEARCH_MODE_TAG:
            return new FindCommand(new TagContainsKeywordsPredicate(keywords));
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    private String normalizeWhitespace(String rawKeyword) {
        return rawKeyword.trim().replaceAll("\\s+", " ");
    }

}
