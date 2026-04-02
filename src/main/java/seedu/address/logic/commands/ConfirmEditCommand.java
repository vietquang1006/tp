package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Asks user for confirmation for editing an existing person in the address book.
 */
public class ConfirmEditCommand extends EditCommand implements ConfirmCommand {

    public static final String MESSAGE_DUPLICATE_PERSON_WARNING =
            "Warning: %1$s\nis an existing person.\n";

    public static final String MESSAGE_ASK_CONFIRMATION =
            "Are you sure you want to edit the contact: %1$s?\n"
                    + "Changes made:\n%2$s\n"
                    + "[y/n]";

    /**
     * Creates a ConfirmEditCommand to confirm editing the specified {@code Person}.
     */
    public ConfirmEditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        super(index, editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return new CommandResult(getConfirmationMessage(model), false, false, true);
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        String changesMade = buildChangesMessage(personToEdit, editedPerson);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            return String.format(MESSAGE_DUPLICATE_PERSON_WARNING, Messages.format(editedPerson))
                    + String.format(MESSAGE_ASK_CONFIRMATION, Messages.format(personToEdit), changesMade);
        }

        return String.format(MESSAGE_ASK_CONFIRMATION, Messages.format(personToEdit), changesMade);
    }

    /**
     * Builds a multi-line message showing the differences between the original and edited person.
     */
    protected String buildChangesMessage(Person originalPerson, Person editedPerson) throws CommandException {
        List<String> changes = new ArrayList<>();

        addChange(changes, "Role", originalPerson.getRole(), editedPerson.getRole());
        addChange(changes, "Name", originalPerson.getName(), editedPerson.getName());
        addChange(changes, "Phone", originalPerson.getPhone(), editedPerson.getPhone());
        addChange(changes, "Email", originalPerson.getEmail(), editedPerson.getEmail());
        addChange(changes, "Address", originalPerson.getAddress(), editedPerson.getAddress());

        if (!originalPerson.getTags().equals(editedPerson.getTags())) {
            changes.add(String.format("Tags: %s -> %s",
                    formatTags(originalPerson.getTags()),
                    formatTags(editedPerson.getTags())));
        }

        if (changes.isEmpty()) {
            throw new CommandException(MESSAGE_NO_CHANGE);
        }
        return String.join("\n", changes);
    }

    private void addChange(List<String> changes, String fieldName, Object originalValue, Object editedValue) {
        String oldString = formatValue(originalValue);
        String newString = formatValue(editedValue);

        if (!oldString.equals(newString)) {
            changes.add(String.format("%s: %s -> %s", fieldName, oldString, newString));
        }
    }

    private String formatValue(Object value) {
        if (value instanceof Optional<?>) {
            Optional<?> optionalValue = (Optional<?>) value;
            return optionalValue.map(Object::toString).orElse("-");
        }
        return value == null ? "-" : value.toString();
    }

    private String formatTags(Set<Tag> tags) {
        if (tags.isEmpty()) {
            return "-";
        }
        return tags.stream()
                .map(Tag::toString)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ConfirmEditCommand
                && index.equals(((ConfirmEditCommand) other).index)
                && editPersonDescriptor.equals(((ConfirmEditCommand) other).editPersonDescriptor));
    }

    @Override
    public String toString() {
        return ConfirmEditCommand.class.getCanonicalName() + "{"
                + "index=" + index
                + ", editPersonDescriptor=" + editPersonDescriptor
                + "}";
    }
}
