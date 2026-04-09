package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Thinh: I used AI assistance to help identify relevant files to modify
 * and to generate and refine validation test cases for roles. I reviewed
 * and adapted the final tests.
 */

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        String invalidRole = "";
        assertThrows(IllegalArgumentException.class, () -> new Role(invalidRole));
    }

    @Test
    public void isValidRole() {
        // null role
        assertThrows(NullPointerException.class, () -> Role.isValidRole(null));

        // invalid role
        assertFalse(Role.isValidRole("")); // empty string
        assertFalse(Role.isValidRole(" ")); // spaces only
        assertFalse(Role.isValidRole(" President")); // leading space
        assertFalse(Role.isValidRole("President ")); // trailing space
        assertFalse(Role.isValidRole("Vice  President")); // consecutive spaces
        assertFalse(Role.isValidRole("^")); // invalid symbol only
        assertFalse(Role.isValidRole("Head*")); // contains invalid symbol
        assertFalse(Role.isValidRole("@Admin")); // invalid starting character

        // valid role
        assertTrue(Role.isValidRole("President")); // simple
        assertTrue(Role.isValidRole("Vice President")); // space
        assertTrue(Role.isValidRole("Head of Blabla")); // lowercase word inside
        assertTrue(Role.isValidRole("Year 1 Representative")); // numbers
        assertTrue(Role.isValidRole("Vice-President")); // hyphen
        assertTrue(Role.isValidRole("Welfare & Logistics")); // ampersand
        assertTrue(Role.isValidRole("Admin/Finance")); // slash
        assertTrue(Role.isValidRole("Secretary (Admin)")); // parentheses
        assertTrue(Role.isValidRole("Director, Outreach")); // comma
        assertTrue(Role.isValidRole("Director of Students' Affairs")); // apostrophe
        assertTrue(Role.isValidRole("R&D Lead")); // compact punctuation
        assertTrue(Role.isValidRole("Head, Welfare & Logistics")); // mixed punctuation
        assertTrue(Role.isValidRole("Secretary-General")); // hyphenated
        assertTrue(Role.isValidRole("Students' Welfare Officer")); // apostrophe
        assertTrue(Role.isValidRole("Committee Lead (Year 1/2)")); // slash inside parentheses
    }

    @Test
    public void equals() {
        Role role = new Role("Valid Role");

        // same values -> returns true
        assertTrue(role.equals(new Role("Valid Role")));

        // same object -> returns true
        assertTrue(role.equals(role));

        // null -> returns false
        assertFalse(role.equals(null));

        // different types -> returns false
        assertFalse(role.equals(5.0f));

        // different values -> returns false
        assertFalse(role.equals(new Role("Other Valid Role")));
    }
}
