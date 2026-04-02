package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different role, all other attributes same -> returns true
        editedAlice = new PersonBuilder(ALICE).withRole(VALID_ROLE_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different role -> returns false
        editedAlice = new PersonBuilder(ALICE).withRole(VALID_ROLE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different busy periods -> returns false
        editedAlice = new PersonBuilder(ALICE).withBusyPeriod("01/01/2026", "02/01/2026").build();
        assertFalse(ALICE.equals(editedAlice));

        // Test equality with missing optional fields
        Person emptyFieldsPerson = new PersonBuilder().withName(ALICE.getName().fullName)
                .withRole(null).withPhone(null).withEmail(null).withAddress(null).build();
        Person emptyFieldsPersonCopy = new PersonBuilder().withName(ALICE.getName().fullName)
                .withRole(null).withPhone(null).withEmail(null).withAddress(null).build();
        assertTrue(emptyFieldsPerson.equals(emptyFieldsPersonCopy));
        assertFalse(emptyFieldsPerson.equals(ALICE));
        assertFalse(ALICE.equals(emptyFieldsPerson));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName()
                + ", role=" + ALICE.getRole().get() + ", phone=" + ALICE.getPhone().get()
                + ", email=" + ALICE.getEmail().get() + ", address=" + ALICE.getAddress().get()
                + ", tags=" + ALICE.getTags() + ", busyPeriods=" + ALICE.getBusyPeriods() + "}";
        assertEquals(expected, ALICE.toString());

        Person emptyFieldsPerson = new PersonBuilder().withName(ALICE.getName().fullName)
                .withRole(null).withPhone(null).withEmail(null).withAddress(null).build();
        String expectedEmpty = Person.class.getCanonicalName() + "{name=" + emptyFieldsPerson.getName()
                + ", tags=" + emptyFieldsPerson.getTags() + ", busyPeriods=" + emptyFieldsPerson.getBusyPeriods() + "}";
        assertEquals(expectedEmpty, emptyFieldsPerson.toString());
    }

    @Test
    public void legacyConstructors_validInputs_success() {
        Person legacy1 = new Person(
            new Role("Developer"), new Name("John"), new Phone("123456"),
            new Email("john@email.com"), new Address("123 Street"), new java.util.HashSet<>()
        );
        assertEquals("John", legacy1.getName().fullName);
        assertEquals("Developer", legacy1.getRole().get().roleName);
        assertTrue(legacy1.getBusyPeriods().isEmpty());
    }
}
