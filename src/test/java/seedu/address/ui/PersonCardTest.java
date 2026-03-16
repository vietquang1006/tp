package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * A class to test the {@code PersonCard} UI component.
 */
public class PersonCardTest {

    /**
     * Initializes the JavaFX toolkit appropriately for headless testing.
     */
    @BeforeAll
    public static void setUp() {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        } catch (UnsupportedOperationException e) {
            System.out.println("JavaFX startup not supported in this environment; continuing");
        }
    }

    /**
     * Helper method to get private fields of the class using reflection.
     */
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(object);
    }

    /**
     * Tests if the {@code PersonCard} correctly displays the details of a {@code Person}.
     */
    @Test
    public void display_personDetails_correctlyAssigned() throws Exception {
        Person person = new PersonBuilder().build();

        CountDownLatch latch = new CountDownLatch(1);
        final PersonCard[] personCard = new PersonCard[1];

        Platform.runLater(() -> {
            personCard[0] = new PersonCard(person, 1);
            latch.countDown();
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out on JavaFX thread. Toolkit may not have initialized correctly.");
        }

        Label idLabel = getPrivateField(personCard[0], "id");
        Label nameLabel = getPrivateField(personCard[0], "name");
        Label phoneLabel = getPrivateField(personCard[0], "phone");
        Label addressLabel = getPrivateField(personCard[0], "address");
        Label emailLabel = getPrivateField(personCard[0], "email");
        Label roleLabel = getPrivateField(personCard[0], "role");

        assertEquals("1. ", idLabel.getText());
        assertEquals(person.getName().fullName, nameLabel.getText());
        assertEquals(person.getPhone().value, phoneLabel.getText());
        assertEquals(person.getAddress().value, addressLabel.getText());
        assertEquals(person.getEmail().value, emailLabel.getText());
        assertEquals(person.getRole().roleName, roleLabel.getText());
    }

    /**
     * Tests the {@code equals} method of {@code PersonCard}.
     */
    @Test
    public void equals() throws Exception {
        Person person = new PersonBuilder().build();

        CountDownLatch latch = new CountDownLatch(1);
        final PersonCard[] personCard = new PersonCard[1];

        Platform.runLater(() -> {
            personCard[0] = new PersonCard(person, 1);
            latch.countDown();
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new AssertionError("Timeout waiting for JavaFX thread. Toolkit may not have initialized properly.");
        }

        PersonCard card = personCard[0];

        // same object -> returns true
        assertTrue(card.equals(card));

        // null -> returns false
        assertFalse(card.equals(null));

        // different types -> returns false
        assertFalse(card.equals(1));

        CountDownLatch latch2 = new CountDownLatch(1);
        final PersonCard[] differentCard = new PersonCard[1];
        final PersonCard[] differentPersonCard = new PersonCard[1];

        Person person2 = new PersonBuilder().withName("Different Name").build();

        Platform.runLater(() -> {
            differentCard[0] = new PersonCard(person, 2);
            differentPersonCard[0] = new PersonCard(person2, 1);
            latch2.countDown();
        });
        if (!latch2.await(5, TimeUnit.SECONDS)) {
            throw new AssertionError("Timeout waiting for JavaFX thread. Toolkit may not have initialized properly.");
        }

        // different person, same index -> returns false
        assertFalse(card.equals(differentPersonCard[0]));

        // same person, different index -> returns false
        assertFalse(card.equals(differentCard[0]));

        // same person, same index -> returns true
        CountDownLatch latch3 = new CountDownLatch(1);
        final PersonCard[] sameCard = new PersonCard[1];
        Platform.runLater(() -> {
            sameCard[0] = new PersonCard(person, 1);
            latch3.countDown();
        });
        if (!latch3.await(5, TimeUnit.SECONDS)) {
            throw new AssertionError("Timeout waiting for JavaFX thread. Toolkit may not have initialized properly.");
        }
        assertTrue(card.equals(sameCard[0]));
    }

    // Yi Heng: I used AI to help me iterate to cover the display of a personcard with the tags
    // I prompted AI with the report feedback by CodeCov and what is my current state of PersonCardTest
    // It replied me with the below test case to cover the display of the tags in PersonCard has the
    // correct quantity.
    /**
     * Tests that tags are correctly created and added to the UI.
     */
    @Test
    public void display_personWithTags_tagsDisplayed() throws Exception {
        Person person = new PersonBuilder()
                .withTags("friend", "colleague")
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        final PersonCard[] personCard = new PersonCard[1];

        Platform.runLater(() -> {
            personCard[0] = new PersonCard(person, 1);
            latch.countDown();
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out on JavaFX thread.");
        }

        javafx.scene.layout.FlowPane tagsPane = getPrivateField(personCard[0], "tags");

        assertEquals(2, tagsPane.getChildren().size());
    }

    /**
     * Tests that clicking on a copyable label (e.g., phone) copies the correct text to the clipboard.
     * @throws Exception
     */
    @Test
    public void copyableLabel_click_copiesText() throws Exception {
        Person person = new PersonBuilder().build();

        CountDownLatch latch = new CountDownLatch(1);
        final PersonCard[] personCard = new PersonCard[1];

        // Create a PersonCard on the JavaFX Application Thread
        // Asynchronously to ensure the UI is properly initialized before the test continues/ ends prematurely
        Platform.runLater(() -> {
            personCard[0] = new PersonCard(person, 1);
            latch.countDown();
        });

        latch.await(500, TimeUnit.MILLISECONDS);

        Label phoneLabel = getPrivateField(personCard[0], "phone");

        CountDownLatch clickLatch = new CountDownLatch(1);

        // Simulate a click on the phone label and check if the correct text is copied to the clipboard
        // Asynchronously to ensure the UI is properly initialized before the test continues/ ends prematurely
        Platform.runLater(() -> {
            // Yi Heng: I used AI to help me iterate to cover the click event of the copyable label in PersonCard
            phoneLabel.fireEvent(
                    new javafx.scene.input.MouseEvent(
                            javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                            0, 0, 0, 0,
                            javafx.scene.input.MouseButton.PRIMARY,
                            1,
                            false, false, false, false,
                            true, false, false,
                            true, false, false,
                            null
                    )
            );

            Clipboard clipboard = Clipboard.getSystemClipboard();
            assertEquals(person.getPhone().value, clipboard.getString());

            clickLatch.countDown();
        });

        // Basic knowledge of Asynchronous programming applied from CS2030S
        clickLatch.await(500, TimeUnit.MILLISECONDS);
    }
}
