package util;

import java.io.ByteArrayInputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

/**
 * Utility class for attaching evidence to Allure reports.
 * @author Osiris Montiel Campos
 * @version 2025-07-06
 */
public class AllureReports {

    private AllureReports() {
        throw new IllegalStateException("AllureAttachments is a utility class and must not be instantiated");
    }

    /**
     * Captures a screenshot from the active browser and attaches it to the Allure report.
     * @param name   label shown for the attachment in the Allure report
     * @param driver active {@link WebDriver} session
     */
    public static void screenshot(String name, WebDriver driver) {
        if (driver instanceof TakesScreenshot ts) {
            byte[] bytes = ts.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(bytes), ".png");
        }
    }

    /**
     * Attaches a plain-text string to the Allure report.
     * @param name  label shown for the attachment in the Allure report
     * @param value text content to attach
     * @return the {@code value} parameter (enables use as a {@link Attachment} return value)
     */
    @Attachment(value = "{name}", type = "text/plain")
    public static String text(String name, String value) {
        return value;
    }
}
