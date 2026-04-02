package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.KeywordRelation;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NameTagContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {
    private static final String SEARCH_MODE_NAME = "-n";
    private static final String SEARCH_MODE_TAG = "-t";


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

        ParsedFindSegments segments = parseSegments(trimmedArgs);
        List<String> nameKeywords = segments.nameKeywords;
        List<String> tagKeywords = segments.tagKeywords;

        if (nameKeywords != null && tagKeywords != null) {
            return new FindCommand(new NameTagContainsKeywordsPredicate(
                    nameKeywords,
                    tagKeywords,
                    segments.nameRelation,
                    segments.tagRelation));
        }

        if (nameKeywords != null) {
            return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords, segments.nameRelation));
        }

        return new FindCommand(new TagContainsKeywordsPredicate(tagKeywords, segments.tagRelation));
    }

    private ParsedFindSegments parseSegments(String args) throws ParseException {
        String working = " " + args;
        List<PrefixPosition> positions = findPrefixPositions(working, PREFIX_NAME, PREFIX_TAG);

        if (positions.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        positions.sort(Comparator.comparingInt(position -> position.startPosition));
        int firstStart = positions.get(0).startPosition;
        if (!working.substring(1, firstStart).trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = null;
        List<String> tagKeywords = null;
        KeywordRelation nameRelation = KeywordRelation.ANY;
        KeywordRelation tagRelation = KeywordRelation.ANY;

        for (int i = 0; i < positions.size(); i++) {
            PrefixPosition current = positions.get(i);
            int segmentEnd = (i + 1 < positions.size())
                    ? positions.get(i + 1).startPosition
                    : working.length();
            String segment = working.substring(current.startPosition + current.prefix.getPrefix().length(), segmentEnd)
                    .trim();

            ParsedSegment parsedSegment = parseSegment(current.prefix, segment);
            if (current.prefix.equals(PREFIX_NAME)) {
                if (nameKeywords != null) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
                }
                nameKeywords = parsedSegment.keywords;
                nameRelation = parsedSegment.relation;
            } else if (current.prefix.equals(PREFIX_TAG)) {
                if (tagKeywords != null) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
                }
                tagKeywords = parsedSegment.keywords;
                tagRelation = parsedSegment.relation;
            }
        }

        if (nameKeywords == null && tagKeywords == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new ParsedFindSegments(nameKeywords, tagKeywords, nameRelation, tagRelation);
    }

    private ParsedSegment parseSegment(Prefix prefix, String segment) throws ParseException {
        if (segment.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Matcher matcher = Pattern.compile("(?<=^|\\s)-m\\b").matcher(segment);
        int matchIndex = -1;
        while (matcher.find()) {
            if (matchIndex != -1) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            matchIndex = matcher.start();
        }

        String keywordsPart = segment;
        KeywordRelation relation = KeywordRelation.ANY;
        if (matchIndex != -1) {
            keywordsPart = segment.substring(0, matchIndex).trim();
            String relationPart = segment.substring(matchIndex + 2).trim();
            relation = parseRelationValue(relationPart);
        }

        List<String> keywords = parseKeywords(keywordsPart);
        return new ParsedSegment(prefix, keywords, relation);
    }

    private KeywordRelation parseRelationValue(String relationPart) throws ParseException {
        if (relationPart.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] tokens = relationPart.split("\\s+");
        if (tokens.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String value = tokens[0].toLowerCase();
        if (value.equals("and")) {
            return KeywordRelation.ALL;
        }

        if (value.equals("or")) {
            return KeywordRelation.ANY;
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    private List<PrefixPosition> findPrefixPositions(String argsString, Prefix... prefixes) {
        List<PrefixPosition> positions = new ArrayList<>();
        for (Prefix prefix : prefixes) {
            int prefixPosition = findPrefixPosition(argsString, prefix.getPrefix(), 0);
            while (prefixPosition != -1) {
                positions.add(new PrefixPosition(prefix, prefixPosition));
                prefixPosition = findPrefixPosition(argsString, prefix.getPrefix(),
                        prefixPosition + prefix.getPrefix().length());

            }
        }
        return positions;
    }

    private int findPrefixPosition(String argsString, String prefix, int fromIndex) {
        // Check if the prefix is at the very start (index 1) only if we haven't passed it
        if (fromIndex <= 1 && argsString.startsWith(prefix, 1)) {
            return 1;
        }

        // Otherwise, only look for " -prefix" (space + prefix)
        int prefixIndex = argsString.indexOf(" " + prefix, fromIndex);

        // Return the actual start of the prefix (index after the space)
        return (prefixIndex == -1) ? -1 : prefixIndex + 1;
    }

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

    private static class PrefixPosition {
        private final Prefix prefix;
        private final int startPosition;

        PrefixPosition(Prefix prefix, int startPosition) {
            this.prefix = prefix;
            this.startPosition = startPosition;
        }
    }

    private static class ParsedSegment {
        private final Prefix prefix;
        private final List<String> keywords;
        private final KeywordRelation relation;

        ParsedSegment(Prefix prefix, List<String> keywords, KeywordRelation relation) {
            this.prefix = prefix;
            this.keywords = keywords;
            this.relation = relation;
        }
    }

    private static class ParsedFindSegments {
        private final List<String> nameKeywords;
        private final List<String> tagKeywords;
        private final KeywordRelation nameRelation;
        private final KeywordRelation tagRelation;

        ParsedFindSegments(List<String> nameKeywords, List<String> tagKeywords,
                           KeywordRelation nameRelation, KeywordRelation tagRelation) {
            this.nameKeywords = nameKeywords;
            this.tagKeywords = tagKeywords;
            this.nameRelation = nameRelation;
            this.tagRelation = tagRelation;
        }
    }

}
