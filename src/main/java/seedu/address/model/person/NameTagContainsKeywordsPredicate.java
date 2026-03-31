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
    private final KeywordRelation nameRelation;
    private final KeywordRelation tagRelation;

    /**
     * Creates a predicate that matches name keywords, tag keywords, or both.
     *
     * @param nameKeywords Keywords to match against a person's name, or null to ignore names.
     * @param tagKeywords Keywords to match against a person's tags, or null to ignore tags.
     */
    public NameTagContainsKeywordsPredicate(
            List<String> nameKeywords, List<String> tagKeywords) throws CommandException {
        this(nameKeywords, tagKeywords, KeywordRelation.ANY, KeywordRelation.ANY);
    }

    /**
     * Creates a predicate that matches name keywords, tag keywords, or both using the same relation.
     *
     * @param nameKeywords Keywords to match against a person's name, or null to ignore names.
     * @param tagKeywords Keywords to match against a person's tags, or null to ignore tags.
     * @param relation How to combine keyword matches.
     */
    public NameTagContainsKeywordsPredicate(List<String> nameKeywords, List<String> tagKeywords,
                                            KeywordRelation relation) throws CommandException {
        this(nameKeywords, tagKeywords, relation, relation);
    }

    /**
     * Creates a predicate that matches name keywords, tag keywords, or both using provided relations.
     *
     * @param nameKeywords Keywords to match against a person's name, or null to ignore names.
     * @param tagKeywords Keywords to match against a person's tags, or null to ignore tags.
     * @param nameRelation How to combine name keyword matches.
     * @param tagRelation How to combine tag keyword matches.
     */
    public NameTagContainsKeywordsPredicate(List<String> nameKeywords, List<String> tagKeywords,
                                            KeywordRelation nameRelation, KeywordRelation tagRelation)
            throws CommandException {
        if (nameKeywords == null && tagKeywords == null) {
            throw new IllegalArgumentException("At least one keyword list must be provided.");
        }

        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
        this.nameRelation = nameRelation;
        this.tagRelation = tagRelation;
        this.namePredicate = nameKeywords == null
                ? null
                : new NameContainsKeywordsPredicate(nameKeywords, nameRelation);
        this.tagPredicate = tagKeywords == null
                ? null
                : new TagContainsKeywordsPredicate(tagKeywords, tagRelation);
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
                && Objects.equals(tagKeywords, otherPredicate.tagKeywords)
                && nameRelation == otherPredicate.nameRelation
                && tagRelation == otherPredicate.tagRelation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tagKeywords", tagKeywords)
                .add("nameRelation", nameRelation)
                .add("tagRelation", tagRelation)
                .toString();
    }
}
