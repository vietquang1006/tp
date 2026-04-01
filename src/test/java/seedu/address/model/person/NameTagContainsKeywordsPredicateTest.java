package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NameTagContainsKeywordsPredicateTest {

    @Test
    public void constructor_nullKeywords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new NameTagContainsKeywordsPredicate(null, null));
    }

    @Test
    public void equals() throws Exception {
        List<String> firstPredicateNameKeywordList = Collections.singletonList("first");
        List<String> secondPredicateNameKeywordList = Arrays.asList("first", "second");
        List<String> firstPredicateTagKeywordList = Collections.singletonList("tag1");
        List<String> secondPredicateTagKeywordList = Arrays.asList("tag1", "tag2");

        NameTagContainsKeywordsPredicate firstPredicate = new NameTagContainsKeywordsPredicate(
                firstPredicateNameKeywordList, firstPredicateTagKeywordList);
        NameTagContainsKeywordsPredicate secondPredicate = new NameTagContainsKeywordsPredicate(
                secondPredicateNameKeywordList, secondPredicateTagKeywordList);
        NameTagContainsKeywordsPredicate thirdPredicate = new NameTagContainsKeywordsPredicate(
                firstPredicateNameKeywordList, null);
        NameTagContainsKeywordsPredicate fourthPredicate = new NameTagContainsKeywordsPredicate(
                null, firstPredicateTagKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameTagContainsKeywordsPredicate firstPredicateCopy = new NameTagContainsKeywordsPredicate(
                firstPredicateNameKeywordList, firstPredicateTagKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different attributes -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(thirdPredicate));
        assertFalse(firstPredicate.equals(fourthPredicate));
    }

    @Test
    public void test_nameAndTagMatches_returnsTrue() throws Exception {
        // One keyword
        NameTagContainsKeywordsPredicate predicate = new NameTagContainsKeywordsPredicate(
                Collections.singletonList("Alice"), Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friend").build()));

        // Multiple keywords
        predicate = new NameTagContainsKeywordsPredicate(
                Arrays.asList("Alice", "Bob"), Arrays.asList("friend", "colleague"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob")
                .withTags("friend", "colleague").build()));

        // Only name matches, tag is null
        predicate = new NameTagContainsKeywordsPredicate(Collections.singletonList("Alice"), null);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friend").build()));

        // Only tag matches, name is null
        predicate = new NameTagContainsKeywordsPredicate(null, Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friend").build()));
    }

    @Test
    public void test_nameAndTagDoesNotMatch_returnsFalse() throws Exception {
        // Neither matches
        NameTagContainsKeywordsPredicate predicate = new NameTagContainsKeywordsPredicate(
                Collections.singletonList("Carol"), Collections.singletonList("enemy"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friend").build()));

        // Name matches but tag does not match
        predicate = new NameTagContainsKeywordsPredicate(
                Collections.singletonList("Alice"), Collections.singletonList("enemy"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friend").build()));

        // Tag matches but name does not match
        predicate = new NameTagContainsKeywordsPredicate(
                Collections.singletonList("Carol"), Collections.singletonList("friend"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friend").build()));
    }

    @Test
    public void toStringMethod() throws Exception {
        List<String> nameKeywords = List.of("keyword1", "keyword2");
        List<String> tagKeywords = List.of("tag1", "tag2");
        NameTagContainsKeywordsPredicate predicate = new NameTagContainsKeywordsPredicate(
                nameKeywords, tagKeywords);

        String expected = NameTagContainsKeywordsPredicate.class.getCanonicalName() + "{nameKeywords=" + nameKeywords
                + ", tagKeywords=" + tagKeywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
