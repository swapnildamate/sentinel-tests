package org.sentinel.tests.reportUtils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] captureScreenshot(WebDriver driver) {
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return null;
    }
}

