package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.BusyPeriod;

/**
 * Jackson-friendly version of {@link BusyPeriod}.
 */
class JsonAdaptedBusyPeriod {

    private final String startDate;
    private final String endDate;

    /**
     * Constructs a {@code JsonAdaptedBusyPeriod} with the given busy period details.
     */
    @JsonCreator
    public JsonAdaptedBusyPeriod(@JsonProperty("startDate") String startDate,
                                 @JsonProperty("endDate") String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Converts a given {@code BusyPeriod} into this class for Jackson use.
     */
    public JsonAdaptedBusyPeriod(BusyPeriod source) {
        startDate = source.getStartDateString();
        endDate = source.getEndDateString();
    }

    /**
     * Converts this Jackson-friendly adapted busy period object into the model's {@code BusyPeriod} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted busy period.
     */
    public BusyPeriod toModelType() throws IllegalValueException {
        if (startDate == null || endDate == null) {
            throw new IllegalValueException(BusyPeriod.MESSAGE_CONSTRAINTS);
        }
        if (!BusyPeriod.isValidDateFormat(startDate) || !BusyPeriod.isValidDateFormat(endDate)) {
            throw new IllegalValueException(BusyPeriod.MESSAGE_CONSTRAINTS);
        }
        try {
            return new BusyPeriod(startDate, endDate);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage());
        }
    }

}
