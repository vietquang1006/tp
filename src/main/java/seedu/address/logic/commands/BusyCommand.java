package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Address;
import seedu.address.model.person.BusyPeriod;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Command to mark a person as busy.
 */
public class BusyCommand extends Command {
    public static final String COMMAND_WORD = "busy";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the person identified "
            + "by the index number used in the displayed person list as busy for a specified period.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_START_DATE + " START_DATE "
            + PREFIX_END_DATE + " END_DATE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_START_DATE + " 25/03/2026 "
            + PREFIX_END_DATE + " 28/03/2026";

    public static final String MESSAGE_BUSY_PERSON_SUCCESS = "Successfully marked %1$s as busy from %2$s.";
    public static final String MESSAGE_IDENTICAL_BUSY_PERIOD = "%1$s already has the busy period: %2$s.";

    private final Index index;
    private final Optional<BusyPeriod> busyPeriod;

    /**
     * @param index of the person in the filtered person list to edit
     * @param busyPeriod to mark the person as busy
     */
    public BusyCommand(Index index, Optional<BusyPeriod> busyPeriod) {
        this.index = index;
        this.busyPeriod = busyPeriod;

    }

    @Override
    public CommandResult execute(seedu.address.model.Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToBusy = lastShownList.get(index.getZeroBased());
        Person busyPerson = createBusyPerson(personToBusy, this.busyPeriod);

        if (personToBusy.getBusyPeriod().equals(busyPerson.getBusyPeriod())) {
            String busyPeriodString = personToBusy.getBusyPeriod()
                    .map(BusyPeriod::toString)
                    .orElse("No busy period");
            throw new CommandException(String.format(MESSAGE_IDENTICAL_BUSY_PERIOD,
                    personToBusy.getName(), busyPeriodString));
        }

        model.setPerson(personToBusy, busyPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String busyPeriodString = busyPerson.getBusyPeriod()
                .map(BusyPeriod::toString)
                .orElse("No busy period");

        return new CommandResult(String.format(MESSAGE_BUSY_PERSON_SUCCESS, busyPerson.getName(), busyPeriodString));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BusyCommand)) {
            return false;
        }

        BusyCommand otherBusyCommand = (BusyCommand) other;
        return index.equals(otherBusyCommand.index)
                && busyPeriod.equals(otherBusyCommand.busyPeriod);
    }

    @Override
    public String toString() {
        return BusyCommand.class.getCanonicalName() + "{index=" + index + ", busyPeriod=" + busyPeriod + "}";
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToBusy}
     * with a new {@code BusyPeriod}
     */
    private static Person createBusyPerson(Person personToBusy, Optional<BusyPeriod> busyPeriod) {
        assert personToBusy != null;

        Optional<Role> role = personToBusy.getRole();
        Name name = personToBusy.getName();
        Optional<Phone> phone = personToBusy.getPhone();
        Optional<Email> email = personToBusy.getEmail();
        Optional<Address> address = personToBusy.getAddress();
        Set<Tag> tags = personToBusy.getTags();


        return new Person(role, name, phone, email, address, tags, busyPeriod);
    }

    public String getCommandWord() {
        return COMMAND_WORD;
    }


}
