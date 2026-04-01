package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NameTagContainsKeywordsPredicate;
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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_TAG);

        Optional<String> nameValue = argMultimap.getValue(PREFIX_NAME);
        Optional<String> tagValue = argMultimap.getValue(PREFIX_TAG);

        if (nameValue.isEmpty() && tagValue.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = null;
        if (nameValue.isPresent()) {
            nameKeywords = parseKeywords(nameValue.get());
        }

        List<String> tagKeywords = null;
        if (tagValue.isPresent()) {
            tagKeywords = parseKeywords(tagValue.get());
        }

        if (nameKeywords != null && tagKeywords != null) {
            return new FindCommand(new NameTagContainsKeywordsPredicate(nameKeywords, tagKeywords));
        }

        if (nameKeywords != null) {
            return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
        }

        return new FindCommand(new TagContainsKeywordsPredicate(tagKeywords));
    }

    /**
     * Parses the query segment after the search mode into keyword groups.
     */
    private List<String> parseKeywords(String rawQuery) throws ParseException {

        String trimmedQuery = rawQuery.trim();
        if (trimmedQuery.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Stream<String> keywordStream = trimmedQuery.contains(";")
                ? Stream.of(trimmedQuery.split(";"))
                : Stream.of(trimmedQuery);

        List<String> keywords = keywordStream
                .map(this::normalizeWhitespace)
                .collect(Collectors.toList());

        if (keywords.isEmpty() || keywords.stream().anyMatch(String::isEmpty)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return keywords;
    }

    private String normalizeWhitespace(String rawKeyword) {
        return rawKeyword.trim().replaceAll("\\s+", " ");
    }

}
