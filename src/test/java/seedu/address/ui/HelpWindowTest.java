package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;

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
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        } catch (UnsupportedOperationException e) {
            System.out.println("JavaFX startup not supported in this environment; continuing");
        }
    }

    @Test
    public void openUserGuide_doesNotCrash() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final HelpWindow[] helpWindow = new HelpWindow[1];

        Platform.runLater(() -> {
            helpWindow[0] = new HelpWindow();
            // Try opening a user guide, which might throw exception in headless Desktop.getDesktop(),
            // but HelpWindow catches it.

            assertDoesNotThrow(() -> helpWindow[0].openUserGuide());
            latch.countDown();
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out on JavaFX thread. Toolkit may not have initialized correctly.");
        }
    }
}

