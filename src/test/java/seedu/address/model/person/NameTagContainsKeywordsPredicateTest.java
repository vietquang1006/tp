package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.testutil.PersonBuilder;

public class NameTagContainsKeywordsPredicateTest {

    @Test
    public void equals() throws CommandException {
        List<String> nameKeywords = Collections.singletonList("Meier");
        List<String> tagKeywords = Collections.singletonList("friends");

        NameTagContainsKeywordsPredicate predicate =
                new NameTagContainsKeywordsPredicate(nameKeywords, tagKeywords);
        NameTagContainsKeywordsPredicate samePredicate =
                new NameTagContainsKeywordsPredicate(nameKeywords, tagKeywords);
        NameTagContainsKeywordsPredicate differentPredicate =
                new NameTagContainsKeywordsPredicate(Collections.singletonList("Alice"), tagKeywords);
        NameTagContainsKeywordsPredicate allPredicate =
                new NameTagContainsKeywordsPredicate(nameKeywords, tagKeywords, KeywordRelation.ALL);
        NameTagContainsKeywordsPredicate mixedPredicate =
                new NameTagContainsKeywordsPredicate(
                        nameKeywords, tagKeywords, KeywordRelation.ALL, KeywordRelation.ANY);

        assertTrue(predicate.equals(predicate));
        assertTrue(predicate.equals(samePredicate));
        assertFalse(predicate.equals(allPredicate));
        assertFalse(predicate.equals(mixedPredicate));
        assertFalse(predicate.equals(1));
        assertFalse(predicate.equals(null));
        assertFalse(predicate.equals(differentPredicate));
    }

    @Test
    public void test_nameOnlyMatchesAnyKeyword() throws CommandException {
        NameTagContainsKeywordsPredicate predicate = new NameTagContainsKeywordsPredicate(
                Arrays.asList("Meier", "Alice"), null);

        assertTrue(predicate.test(new PersonBuilder().withName("Benson Meier").build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Carl Kurz").build()));

        NameTagContainsKeywordsPredicate allPredicate = new NameTagContainsKeywordsPredicate(
                Arrays.asList("Benson", "Meier"), null, KeywordRelation.ALL);
        assertTrue(allPredicate.test(new PersonBuilder().withName("Benson Meier").build()));
    }

    @Test
    public void test_tagOnlyMatchesAnyKeyword() throws CommandException {
        NameTagContainsKeywordsPredicate predicate = new NameTagContainsKeywordsPredicate(
                null, Arrays.asList("friends", "project"));

        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));
        assertFalse(predicate.test(new PersonBuilder().withTags("family").build()));

        NameTagContainsKeywordsPredicate allPredicate = new NameTagContainsKeywordsPredicate(
                null, Arrays.asList("friends", "project"), KeywordRelation.ALL);
        assertFalse(allPredicate.test(new PersonBuilder().withTags("friends").build()));
    }

    @Test
    public void test_nameAndTagMustBothMatch() throws CommandException {
        NameTagContainsKeywordsPredicate predicate = new NameTagContainsKeywordsPredicate(
                Collections.singletonList("Meier"), Collections.singletonList("friends"));

        assertTrue(predicate.test(new PersonBuilder().withName("Benson Meier").withTags("friends").build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Benson Meier").withTags("family").build()));
    }

    @Test
    public void constructor_nullKeywords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new NameTagContainsKeywordsPredicate(null, null));
    }

    @Test
    public void toStringMethod() throws CommandException {
        List<String> nameKeywords = Collections.singletonList("Meier");
        List<String> tagKeywords = Collections.singletonList("friends");
        NameTagContainsKeywordsPredicate predicate = new NameTagContainsKeywordsPredicate(nameKeywords, tagKeywords);

        String expected = NameTagContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords + ", tagKeywords=" + tagKeywords
                + ", nameRelation=" + KeywordRelation.ANY + ", tagRelation=" + KeywordRelation.ANY + "}";
        assertEquals(expected, predicate.toString());
    }
}
