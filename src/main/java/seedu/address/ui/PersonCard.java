package seedu.address.ui;

import java.util.Comparator;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label role;
    @FXML
    private FlowPane busyPeriods;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        person.getRole().ifPresentOrElse(r -> role.setText(r.roleName), () -> {
            role.setText("");
            role.setVisible(false);
            role.setManaged(false);
        });
        person.getPhone().ifPresentOrElse(p -> phone.setText(p.value), () -> {
            phone.setText("");
            phone.setVisible(false);
            phone.setManaged(false);
        });
        person.getAddress().ifPresentOrElse(a -> address.setText(a.value), () -> {
            address.setText("");
            address.setVisible(false);
            address.setManaged(false);
        });
        person.getEmail().ifPresentOrElse(e -> email.setText(e.value), () -> {
            email.setText("");
            email.setVisible(false);
            email.setManaged(false);
        });
        if (person.getBusyPeriods().isEmpty()) {
            busyPeriods.setVisible(false);
            busyPeriods.setManaged(false);
        } else {
            person.getBusyPeriods().stream()
                    .sorted(Comparator.comparing(bp -> bp.startDate))
                    .forEach(bp -> {
                        Label bpLabel = new Label("Busy: " + bp.toString());
                        bpLabel.getStyleClass().add("cell_busy_period_label");
                        busyPeriods.getChildren().add(bpLabel);
                        makeCopyable(bpLabel, "cell_busy_period_label");
                    });
        }
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .map(tag -> {
                    Label label = new Label(tag.tagName);
                    label.getStyleClass().add("tag");
                    return label;
                })
                .forEach(tags.getChildren()::add);
        initialiseCopyable();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }

    /**
     * Initialises the copyable labels by setting their on-click event
     * handlers to copy their text to the clipboard.
     * This method is called during the construction of the PersonCard to
     * ensure that the copyable functionality is set up for the relevant labels.
     */
    private void initialiseCopyable() {
        makeCopyable(phone, "cell_phone_label");
        makeCopyable(email, "cell_email_label");
        makeCopyable(address, "cell_address_label");
        makeCopyable(role, "cell_role_label");
        tags.getChildren().forEach(node -> {
            if (node instanceof Label label) {
                makeCopyable(label, "tag");
            }
        });
    }

    /**
     * Makes the given label copyable to the clipboard when clicked.
     * @param label the label to make copyable
     * @param cssClass the CSS class to add to the label for styling purposes
     */
    private void makeCopyable(Label label, String cssClass) {
        label.getStyleClass().add(cssClass);
        label.setOnMouseClicked(event -> {
            // Step ONE: Copy to clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            clipboard.setContent(content);
            System.out.println("Copied: " + label.getText());

            // Step TWO: Simulate feedback by changing background color
            String originalStyle = label.getStyle();
            String copiedStyle = "-fx-background-color: #FFC0CB"; // Light pink color to indicate copy action
            label.setStyle(copiedStyle);

            // Step THREE: Revert back after 200ms
            PauseTransition pause = new PauseTransition(Duration.millis(200));
            pause.setOnFinished(e -> label.setStyle(originalStyle));
            pause.play();
        });
    }
}
