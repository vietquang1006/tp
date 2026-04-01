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

public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() throws CommandException {
        List<String> firstPredicateKeywordList = Collections.singletonList("friend");
        List<String> secondPredicateKeywordList = Arrays.asList("friend", "classmate");

        TagContainsKeywordsPredicate firstPredicate = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        TagContainsKeywordsPredicate secondPredicate = new TagContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate firstPredicateCopy = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicates -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_tagContainsKeywords_returnsTrue() throws CommandException {
        // One keyword exact match
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "school").build()));

        // Partial keyword match
        predicate = new TagContainsKeywordsPredicate(Collections.singletonList("class"));
        assertTrue(predicate.test(new PersonBuilder().withTags("classmate").build()));

        // Multiple keywords (all match across tags)
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("friend", "class"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "classmate").build()));

        // Mixed-case keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("FrIeNd", "cLaSs"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "classmate").build()));
    }

    @Test
    public void test_tagDoesNotContainKeywords_returnsFalse() throws CommandException {
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Collections.singletonList("project"));
        assertFalse(predicate.test(new PersonBuilder().withTags("friend", "family").build()));

        predicate = new TagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withTags("friend").build()));

        // Any matching keyword returns true
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("friend", "owes"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));

        predicate = new TagContainsKeywordsPredicate(Collections.singletonList("owes me"));
        assertFalse(predicate.test(new PersonBuilder().withTags("friend").build()));
    }

    @Test
    public void constructor_invalidKeyword_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class, () ->
                new TagContainsKeywordsPredicate(Arrays.asList("friend", "classmate!")));
        assertEquals(Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER, exception.getMessage());

        exception = assertThrows(CommandException.class, () ->
                new TagContainsKeywordsPredicate(Arrays.asList("friend", "group-work")));
        assertEquals(Messages.MESSAGE_CONTAINS_NON_ALPHANUMERIC_CHARACTER, exception.getMessage());
    }

    @Test
    public void toStringMethod() throws CommandException {
        List<String> keywords = List.of("keyword1", "keyword2");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(keywords);

        String expected = TagContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}

