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

    /**
     * Creates a predicate that matches names containing any of the given keywords.
     *
     * @param keywords Keywords to match against a person's name.
     * @throws CommandException If any keyword contains a non-alphanumeric character.
     */
    public NameContainsKeywordsPredicate(List<String> keywords) throws CommandException {
        if (keywords.stream().anyMatch(keyword -> !keyword.matches(KEYWORD_VALIDATION_REGEX))) {
            throw new CommandException(Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER);
        }

        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
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
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
