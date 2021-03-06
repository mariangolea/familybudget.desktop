package org.mariangolea.fintrack.bank.parser.ui;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.JFXPanel;
import javax.swing.SwingUtilities;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FXUITest {

    @BeforeClass
    public static void initToolkit()
            throws InterruptedException {
        String headless = System.getProperty("java.awt.headless", "false");
        if (!Boolean.parseBoolean(headless)) {
            final CountDownLatch latch = new CountDownLatch(1);
            SwingUtilities.invokeLater(() -> {
                JFXPanel panel = new JFXPanel();
                latch.countDown();
            });

            FX_INITIALIZED = latch.await(10L, TimeUnit.SECONDS);
        }
    }
    protected static boolean FX_INITIALIZED = false;

    @Test
    public void testSilly() {
        //this class needs a test method to avoid it being marked as a fail...
        assertTrue(true);
    }

}
