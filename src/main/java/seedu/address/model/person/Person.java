package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person's identity in the address book.
 * Guarantees: minimum details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Optional<Role> role;
    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Email> email;

    // Data fields
    private final Optional<Address> address;
    private final Set<Tag> tags = new HashSet<>();
    private final Optional<BusyPeriod> busyPeriod;

    /**
     * Legacy constructor: Wraps nullable parameters in Optionals.
     */
    public Person(Role role, Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.role = Optional.ofNullable(role);
        this.name = name;
        this.phone = Optional.ofNullable(phone);
        this.email = Optional.ofNullable(email);
        this.address = Optional.ofNullable(address);
        this.tags.addAll(tags);
        this.busyPeriod = Optional.empty();
    }

    /**
     * Legacy constructor: Wraps nullable parameters in Optionals.
     */
    public Person(Role role, Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  Optional<BusyPeriod> busyPeriod) {
        requireAllNonNull(name, tags, busyPeriod);
        this.role = Optional.ofNullable(role);
        this.name = name;
        this.phone = Optional.ofNullable(phone);
        this.email = Optional.ofNullable(email);
        this.address = Optional.ofNullable(address);
        this.tags.addAll(tags);
        this.busyPeriod = busyPeriod;
    }

    /**
     * Main constructor utilizing Optionals natively.
     */
    public Person(Optional<Role> role, Name name, Optional<Phone> phone, Optional<Email> email,
                  Optional<Address> address, Set<Tag> tags, Optional<BusyPeriod> busyPeriod) {
        requireAllNonNull(role, name, phone, email, address, tags, busyPeriod);
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.busyPeriod = busyPeriod;
    }

    public Optional<Role> getRole() {
        return role;
    }

    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public Optional<Email> getEmail() {
        return email;
    }

    public Optional<Address> getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an {@code Optional<BusyPeriod>}.
     */
    public Optional<BusyPeriod> getBusyPeriod() {
        return this.busyPeriod;
    }

    /**
     * Returns true if both persons have the same identity.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return role.equals(otherPerson.role)
                && name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && busyPeriod.equals(otherPerson.busyPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, name, phone, email, address, tags, busyPeriod);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
                .add("name", name);
        role.ifPresent(r -> builder.add("role", r));
        phone.ifPresent(p -> builder.add("phone", p));
        email.ifPresent(e -> builder.add("email", e));
        address.ifPresent(a -> builder.add("address", a));
        builder.add("tags", tags);
        busyPeriod.ifPresent(bp -> builder.add("busyPeriod", bp));

        return builder.toString();
    }
}
