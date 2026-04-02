package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.BusyPeriod;

public class JsonAdaptedBusyPeriodTest {
    private static final String INVALID_DATE = "2020-01-01";
    private static final String VALID_START_DATE = "01/01/2020";
    private static final String VALID_END_DATE = "02/01/2020";

    @Test
    public void toModelType_validBusyPeriodDetails_returnsBusyPeriod() throws Exception {
        BusyPeriod busyPeriod = new BusyPeriod(VALID_START_DATE, VALID_END_DATE);
        JsonAdaptedBusyPeriod jsonAdaptedBusyPeriod = new JsonAdaptedBusyPeriod(busyPeriod);
        assertEquals(busyPeriod, jsonAdaptedBusyPeriod.toModelType());
    }

    @Test
    public void toModelType_invalidStartDate_throwsIllegalValueException() {
        JsonAdaptedBusyPeriod jsonAdaptedBusyPeriod =
                new JsonAdaptedBusyPeriod(INVALID_DATE, VALID_END_DATE);
        String expectedMessage = BusyPeriod.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedBusyPeriod::toModelType);
    }

    @Test
    public void toModelType_nullStartDate_throwsIllegalValueException() {
        JsonAdaptedBusyPeriod jsonAdaptedBusyPeriod = new JsonAdaptedBusyPeriod(null, VALID_END_DATE);
        String expectedMessage = BusyPeriod.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedBusyPeriod::toModelType);
    }

    @Test
    public void toModelType_invalidEndDate_throwsIllegalValueException() {
        JsonAdaptedBusyPeriod jsonAdaptedBusyPeriod =
                new JsonAdaptedBusyPeriod(VALID_START_DATE, INVALID_DATE);
        String expectedMessage = BusyPeriod.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedBusyPeriod::toModelType);
    }

    @Test
    public void toModelType_nullEndDate_throwsIllegalValueException() {
        JsonAdaptedBusyPeriod jsonAdaptedBusyPeriod = new JsonAdaptedBusyPeriod(VALID_START_DATE, null);
        String expectedMessage = BusyPeriod.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedBusyPeriod::toModelType);
    }

    @Test
    public void toModelType_invalidDateLogic_throwsIllegalValueException() {
        JsonAdaptedBusyPeriod jsonAdaptedBusyPeriod =
                new JsonAdaptedBusyPeriod(VALID_END_DATE, VALID_START_DATE);
        String expectedMessage = BusyPeriod.MESSAGE_DATE_LOGIC;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedBusyPeriod::toModelType);
    }
}
