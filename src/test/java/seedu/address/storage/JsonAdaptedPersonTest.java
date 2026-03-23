package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.BusyPeriod;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.testutil.PersonBuilder;

public class JsonAdaptedPersonTest {
    private static final String INVALID_ROLE = " ";
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_ROLE = BENSON.getRole().get().toString();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().get().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().get().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().get().toString();
    private static final String VALID_BUSY_START_DATE = "25/03/2026";
    private static final String VALID_BUSY_END_DATE = "28/03/2026";
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_nullOptionalFields_returnsPersonWithoutOptionalFields() throws Exception {
        Person emptyFieldsPerson = new PersonBuilder(BENSON)
                .withRole(null).withPhone(null).withEmail(null).withAddress(null).build();
        JsonAdaptedPerson person = new JsonAdaptedPerson(emptyFieldsPerson);
        assertEquals(emptyFieldsPerson, person.toModelType());
    }

    @Test
    public void toModelType_invalidRole_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_ROLE, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        null, null, VALID_TAGS);
        String expectedMessage = Role.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        null, null, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ROLE, null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        null, null, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_returnsPersonWithoutPhone() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ROLE, VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, null, null, VALID_TAGS);
        Person expectedPerson = new PersonBuilder(BENSON).withPhone(null).build();
        assertEquals(expectedPerson, person.toModelType());
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        null, null, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_returnsPersonWithoutEmail() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ROLE, VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, null, null, VALID_TAGS);
        Person expectedPerson = new PersonBuilder(BENSON).withEmail(null).build();
        assertEquals(expectedPerson, person.toModelType());
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        null, null, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_returnsPersonWithoutAddress() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ROLE, VALID_NAME, VALID_PHONE, VALID_EMAIL, null, null, null, VALID_TAGS);
        Person expectedPerson = new PersonBuilder(BENSON).withAddress(null).build();
        assertEquals(expectedPerson, person.toModelType());
    }

    @Test
    public void toModelType_invalidBusyStartDate_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        "invalid date", VALID_BUSY_END_DATE, VALID_TAGS);
        String expectedMessage = BusyPeriod.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidBusyEndDate_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BUSY_START_DATE, "invalid date", VALID_TAGS);
        String expectedMessage = BusyPeriod.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidBusyPeriodLogic_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_BUSY_END_DATE, VALID_BUSY_START_DATE, VALID_TAGS);
        String expectedMessage = BusyPeriod.MESSAGE_DATE_LOGIC;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_ROLE, VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        null, null, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }
}
