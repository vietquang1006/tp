package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class HelpWindowTest {

    @BeforeAll
    public static void setUp() {
        assumeFalse("true".equals(System.getenv("CI")), "Skipping GUI tests on CI environments.");
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized.
        } catch (UnsupportedOperationException e) {
            System.out.println("JavaFX startup not supported in this environment; continuing");
        }
    }

    @Test
    public void initialize_setsHelpMessage() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<HelpWindow> windowRef = new AtomicReference<>();
        AtomicReference<Throwable> errorRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                windowRef.set(new HelpWindow());
            } catch (Throwable t) {
                errorRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new AssertionError("Timeout waiting for JavaFX thread.");
        }
        if (errorRef.get() != null) {
            throw new AssertionError("Failed to initialize HelpWindow", errorRef.get());
        }

        HelpWindow window = windowRef.get();
        Field field = HelpWindow.class.getDeclaredField("helpMessage");
        field.setAccessible(true);
        Label label = (Label) field.get(window);

        assertEquals(HelpWindow.HELP_MESSAGE, label.getText());
    }

    @Test
    public void openUserGuide_headless_noException() throws Exception {
        String previous = System.getProperty("java.awt.headless");
        System.setProperty("java.awt.headless", "true");

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> errorRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                HelpWindow window = new HelpWindow();
                window.openUserGuide();
            } catch (Throwable t) {
                errorRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new AssertionError("Timeout waiting for JavaFX thread.");
        }

        if (previous == null) {
            System.clearProperty("java.awt.headless");
        } else {
            System.setProperty("java.awt.headless", previous);
        }
        assertNull(errorRef.get());
    }
}
