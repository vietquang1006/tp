package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Tests that a {@code Person}'s {@code Name} and/or {@code Tag} matches the given keywords.
 */
public class NameTagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;
    private final NameContainsKeywordsPredicate namePredicate;
    private final TagContainsKeywordsPredicate tagPredicate;

    /**
     * Creates a predicate that matches name keywords, tag keywords, or both.
     *
     * @param nameKeywords Keywords to match against a person's name, or null to ignore names.
     * @param tagKeywords Keywords to match against a person's tags, or null to ignore tags.
     */
    public NameTagContainsKeywordsPredicate(
            List<String> nameKeywords, List<String> tagKeywords) throws CommandException {
        if (nameKeywords == null && tagKeywords == null) {
            throw new IllegalArgumentException("At least one keyword list must be provided.");
        }

        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
        this.namePredicate = nameKeywords == null ? null : new NameContainsKeywordsPredicate(nameKeywords);
        this.tagPredicate = tagKeywords == null ? null : new TagContainsKeywordsPredicate(tagKeywords);
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        boolean nameMatches = namePredicate == null || namePredicate.test(person);
        boolean tagMatches = tagPredicate == null || tagPredicate.test(person);
        return nameMatches && tagMatches;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NameTagContainsKeywordsPredicate)) {
            return false;
        }

        NameTagContainsKeywordsPredicate otherPredicate = (NameTagContainsKeywordsPredicate) other;
        return Objects.equals(nameKeywords, otherPredicate.nameKeywords)
                && Objects.equals(tagKeywords, otherPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}

