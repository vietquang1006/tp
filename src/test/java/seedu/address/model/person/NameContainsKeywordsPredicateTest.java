package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.testutil.PersonBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() throws CommandException {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different relation -> returns false
        NameContainsKeywordsPredicate allPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList,
                KeywordRelation.ALL);
        assertFalse(firstPredicate.equals(allPredicate));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() throws CommandException {
        // One keyword
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // One partial keyword
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("lic"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords (any match)
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords (all match)
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"), KeywordRelation.ALL);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multi-word keyword phrase
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("alice bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() throws CommandException {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Any matching keyword returns true
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // All relation requires both keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"), KeywordRelation.ALL);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void constructor_invalidKeyword_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class, () ->
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob!")));
        assertEquals(Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER, exception.getMessage());

        exception = assertThrows(CommandException.class, () ->
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob-Charles")));
        assertEquals(Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER, exception.getMessage());
    }

    @Test
    public void toStringMethod() throws CommandException {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + ", relation=" + KeywordRelation.ANY + "}";
        assertEquals(expected, predicate.toString());
    }
}
