package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private static final String KEYWORD_VALIDATION_REGEX = "^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$";

    private final List<String> keywords;
    private final KeywordRelation relation;

    /**
     * Creates a predicate that matches names containing any of the given keywords.
     *
     * @param keywords Keywords to match against a person's name.
     * @throws CommandException If any keyword contains a non-alphanumeric character.
     */
    public NameContainsKeywordsPredicate(List<String> keywords) throws CommandException {
        this(keywords, KeywordRelation.ANY);
    }

    /**
     * Creates a predicate that matches names containing keywords with the provided relation.
     *
     * @param keywords Keywords to match against a person's name.
     * @param relation How to combine keyword matches.
     * @throws CommandException If any keyword contains a non-alphanumeric character.
     */
    public NameContainsKeywordsPredicate(List<String> keywords, KeywordRelation relation) throws CommandException {
        if (keywords.stream().anyMatch(keyword -> !keyword.matches(KEYWORD_VALIDATION_REGEX))) {
            throw new CommandException(Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER);
        }

        this.keywords = keywords;
        this.relation = relation;
    }

    @Override
    public boolean test(Person person) {
        if (keywords.isEmpty()) {
            return false;
        }

        if (relation == KeywordRelation.ALL) {
            return keywords.stream()
                    .allMatch(keyword -> containsIgnoreCase(person.getName().fullName, keyword));
        }

        return keywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getName().fullName, keyword));
    }

    private boolean containsIgnoreCase(String source, String keyword) {
        return source.toLowerCase().contains(keyword.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords)
                && relation == otherNameContainsKeywordsPredicate.relation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .add("relation", relation)
                .toString();
    }
}
