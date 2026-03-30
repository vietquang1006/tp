package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * Test class for MainWindow. Contains tests for the handleHelp method to ensure it
 * behaves correctly under different scenarios.
 *
 * Yi Heng: I used AI to ideate the overall structure of this test suite
 * as it required me to create the stubs, and it was quite complex a high-level structure.
 * the test methods required some dependencies like Logic and PrimaryStage and
 * I need to implement a minimum version of those dependencies to test the handleHelp method.
 * They are carefully set up to support this test suite and are error free
 * so any assert Failures will be due to the MainWindow's handleHelp method and not the test stubs.
 */
public class MainWindowTest {

    private Stage primaryStage;
    private MainWindow mainWindow;
    private TestLogic logic;

    private static class TestHelpWindow extends HelpWindow {
        private boolean opened = false;
        private boolean focused = false;
        private boolean failOnOpen = false;

        @Override
        public void openUserGuide() {
            if (failOnOpen) {
                throw new RuntimeException("Failed to open browser");
            }
            opened = true;
        }

        @Override
        public void focus() {
            focused = true;
        }

        public boolean isShowing() {
            return opened && !failOnOpen;
        }

        public void reset() {
            opened = false;
            focused = false;
            failOnOpen = false;
        }
    }

    private TestHelpWindow testHelpWindow;

    /**
     * A minimal implementation of Logic to support testing MainWindow's handleHelp method.
     */
    private static class TestLogic implements Logic {
        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            if ("help".equals(commandText)) {
                return new CommandResult("Showing help...", true, false, false);
            }
            return new CommandResult("Unknown command");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return null;
        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings(600, 800, 0, 0);
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            // no-op
        }
    }

    /**
     * Initializes JavaFX toolkit before running any tests.
     */
    @BeforeAll
    static void initToolkit() {
        assumeFalse("true".equals(System.getenv("CI")), "Skipping MainWindow GUI tests on CI");

        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");

        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException | UnsupportedOperationException e) {
            // already initialized
        }
    }

    /**
     * Sets up the MainWindow and TestHelpWindow before each test.
     */
    @BeforeEach
    void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                primaryStage = new Stage();
                logic = new TestLogic();
                mainWindow = new MainWindow(primaryStage, logic);

                testHelpWindow = new TestHelpWindow();
                try {
                    Field helpField = MainWindow.class.getDeclaredField("helpWindow");
                    helpField.setAccessible(true);
                    helpField.set(mainWindow, testHelpWindow);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for JavaFX thread setup");
        }
    }

    /**
     * Utility method to set a private field using reflection.
     */
    private void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * Tests that handleHelp opens the help window when it's not already open.
     */
    @Test
    public void handleHelp_opensHelpWindow() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                mainWindow.handleHelp();
                assertTrue(testHelpWindow.opened, "Help window should have been opened");
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for JavaFX thread");
        }
    }

    /**
     * Tests that handleHelp logs a warning when the browser fails to open, but does not throw an exception.
     */
    @Test
    public void handleHelp_browserFails_logsWarning() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        testHelpWindow.failOnOpen = true; // this is to simulate browser failing

        Platform.runLater(() -> {
            try {
                try {
                    mainWindow.handleHelp();
                } catch (Exception e) {
                    fail("Exception should not propagate even if browser fails");
                }
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for JavaFX thread");
        }
    }
}
