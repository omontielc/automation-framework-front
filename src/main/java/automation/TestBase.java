package automation;

import org.apache.logging.log4j.LogManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import io.qameta.allure.Allure;
import util.AllureReports;
import util.TestData;
import util.WaitElements;
import util.Reporter;

/**
 * Base class for all TestNG test classes in this framework.
 *
 * @author Osiris Montiel Campos
 * @version 2025-08-28
 */
public class TestBase {

    protected final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TestBase.class);

    private static final ThreadLocal<Driver> TL_DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<UIElement> TL_UI = new ThreadLocal<>();
    private static final ThreadLocal<WaitElements> TL_WAIT = new ThreadLocal<>();
    private static final ThreadLocal<TestData> TL_DATA = new ThreadLocal<>();
    private static final ThreadLocal<Reporter> TL_REPORTER = new ThreadLocal<>();

    protected Reporter getReporter() {
        return TL_REPORTER.get();
    }

    protected Driver getDriver() {
        return TL_DRIVER.get();
    }

    protected UIElement getUiElement() {
        return TL_UI.get();
    }

    protected WaitElements getWait() {
        return TL_WAIT.get();
    }

    protected TestData getTestData() {
        return TL_DATA.get();
    }

    protected String ATC_Name;
    protected int MAX_ATC;

    public TestBase(String testName) {
        ATC_Name = testName;
    }

    @BeforeMethod
    @Parameters({"testUrl", "browser"})
    public void beforeTest(String testUrl, String browser) {
        logger.info("Initializing test script — browser: {}", browser);

        Driver d = new Driver(browser);
        Reporter rep = new Reporter();
        TL_DRIVER.set(d);
        TL_UI.set(new UIElement(d, rep));
        TL_WAIT.set(new WaitElements(d));
        TL_DATA.set(new TestData("Nearpedia", ATC_Name));
        TL_REPORTER.set(rep);

        MAX_ATC = getTestData().getNUM_ATC();

        String resolvedUrl = testUrl;
        if (!testUrl.startsWith("http") && !testUrl.startsWith("file:")) {
            resolvedUrl = new java.io.File(
                    System.getProperty("user.dir"), testUrl
            ).toURI().toString();
        }
        logger.info("Resolved URL: {}", resolvedUrl);

        getDriver().goTo(resolvedUrl);
        getWait().pageComplete();
        getDriver().maximizeWindows();

        Allure.label("browser", browser);
        Allure.label("testUrl", resolvedUrl);
    }

    @AfterMethod
    public void finish(ITestResult result) throws Exception {
        logger.info("Finalizing test script — status: {}", result.isSuccess() ? "PASSED" : "FAILED");

        String screenshotLabel = result.isSuccess()
                ? "Final screenshot — " + result.getName()
                : "Screenshot on failure — " + result.getName();
        AllureReports.screenshot(screenshotLabel, getDriver().getDriver());

        getDriver().finishExecution();
        logger.info("Test script finalized successfully");

        TL_DRIVER.remove();
        TL_UI.remove();
        TL_WAIT.remove();
        TL_DATA.remove();
        TL_REPORTER.remove();
    }
}