package automation;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import util.AllureReports;
import util.TestData;
import util.WaitElements;

/**
 * Base class for all TestNG test classes in this framework.
 *
 * @author Osiris Montiel Campos
 * @version 2025-08-28
 */
public class TestBase {

    protected final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TestBase.class);

    /**
     * Active WebDriver session, available to all subclasses.
     */
    protected Driver driver;

    /**
     * UI interaction helper, available to all subclasses.
     */
    protected UIElement uiElement;

    /**
     * Explicit wait helper, available to all subclasses.
     */
    protected WaitElements wait;

    /**
     * Test data reader bound to the current test case name.
     */
    protected TestData testData;

    /**
     * Name of the test case, used to look up rows in the test data file.
     */
    protected String ATC_Name;

    /**
     * Total number of test case iterations defined in the data file.
     */
    protected int MAX_ATC;

    /**
     * Creates a new TestBase and registers the test case name.
     *
     * @param testName identifier matching the test case name in the data file
     */
    public TestBase(String testName) {
        ATC_Name = testName;
    }

    /**
     * Initializes all framework components before each test method.
     *
     * @param testUrl URL of the application under test.
     * @param browser browser to launch — "CHROME", "FIREFOX", or "EDGE".
     */
    @BeforeMethod
    @Parameters({"testUrl", "browser"})
    public void beforeTest(String testUrl, String browser) {

        logger.info("Initializing test script");
        driver = new Driver(browser);
        testData = new TestData("Nearpedia", ATC_Name);
        uiElement = new UIElement(driver);
        wait = new WaitElements(driver);
        MAX_ATC = testData.getNUM_ATC();

        String resolvedUrl = testUrl;
        if (!testUrl.startsWith("http") && !testUrl.startsWith("file:")) {
            resolvedUrl = new java.io.File(
                    System.getProperty("user.dir"), testUrl
            ).toURI().toString();
        }
        System.out.println("Resolved URL: " + resolvedUrl);
        driver.goTo(resolvedUrl);
        wait.pageComplete();
        driver.maximizeWindows();

        // Tag every test in Allure with browser and URL for easy filtering
        Allure.label("browser", browser);
        Allure.label("testUrl", testUrl);
    }

    /**
     * Closes the browser after each test method to ensure test isolation.
     *
     * @throws Exception if an error occurs while closing the browser session
     */
    @AfterMethod
    public void finish(ITestResult result) throws Exception {
        logger.info("Finalizing test script — status: {}", result.isSuccess() ? "PASSED" : "FAILED");

        // Always attach a final screenshot so QA has visual evidence of the last UI state
        String screenshotLabel = result.isSuccess() ? "Final screenshot — " + result.getName() : "Screenshot on failure — " + result.getName();
        AllureReports.screenshot(screenshotLabel, driver.getDriver());

        driver.finishExecution();
        logger.info("Test script finalized successfully");
    }
}
