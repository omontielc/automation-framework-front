package test.nearpedia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.testng.annotations.Test;

import automation.TestBase;
import config.Config;
import front.generalFeatures.Menu;
import front.pages.Nearpedia;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import util.AllureReports;
import util.Reporter;

/**
 * Test suite for the Nearpedia flight search and sort functionality.
 * @author Osiris Montiel Campos
 * @version 2025-07-06
 */
@Epic("Nearpedia Flight Search")
@Feature("Flight Search Results")
public class ATC01_NearpediaTest extends TestBase {
	
    /** Identifier used to load test data rows from the Excel data file. */
    private static final String ATC_NAME = "ATC01_NearpediaTest";

    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ATC01_NearpediaTest.class);

    /** Evidence reporter — one instance per test class. */
    private final Reporter reporter;

    /**
     * Registers this test class with {@link TestBase} using {@link #ATC_NAME}.
     */
    public ATC01_NearpediaTest() {
        super(ATC_NAME);
        reporter = new Reporter();
    }

    /**
     * Executes the ATC01 test case: flight search and ascending price sort validation.
     */
    @Test
    @Story("User sorts flight results by price ascending")
    @Description("""
            Given the user is on the Nearpedia home page
            When they search for a round-trip flight and apply the 'Price' sort filter
            Then all displayed results must be ordered from lowest to highest price
            """)
    @Severity(SeverityLevel.CRITICAL)
    public void verifyFlightResultsSortedByPriceAscending() {
        Nearpedia nearpedia = new Nearpedia(driver);
        Menu menu           = new Menu(driver);
        // Initialize report
        reporter.crearReporte("1.0.0", ATC_NAME, "Test 1 - Nearpedia Flight Search", "Osiris Montiel");

        navigateToFlights(menu);
        fillSearchForm(nearpedia);
        applySortFilter(nearpedia);
        validateSortOrder(nearpedia);

        logger.info("Test execution completed successfully");

        // Generate timestamped evidence report
        DateFormat dateFormat = new SimpleDateFormat("d_MM_yyyy_HH,mm,ss");
        String reportName = "Report_" + testData.getData("TestCaseName", 1) + "_" + dateFormat.format(new Date());
        reporter.generarRepote(reportName, Config.REPORTER_PATH);
    }
    
    /**
     * Clicks the "Flights" menu item to navigate to the flight search section.
     *
     * @param menu Menu page object
     */
    @Step("Navigate to Flights menu")
    private void navigateToFlights(Menu menu) {
        menu.clickMenu("Flights", "NEARPEDIA.menu.menuFlight");
        AllureReports.screenshot("Flights menu opened", driver.getDriver());
    }

    /**
     * Fills in the flight search form with departure city, destination, and travel dates.
     *
     * @param nearpedia Nearpedia page object
     */
    @Step("Fill in flight search form")
    private void fillSearchForm(Nearpedia nearpedia) {
        nearpedia.selectListflightFrom("Flight From", "NEARPEDIA.nearpedia.cmbFlightFrom", testData.getData("FlightFrom", 1));
        nearpedia.selectListflightTo  ("Flight To",   "NEARPEDIA.nearpedia.cmbFlightTo",   testData.getData("FlightTo",   1));
        nearpedia.setTextDeparting    ("Departing",   "NEARPEDIA.nearpedia.txtDeparting");
        nearpedia.setTxtReturning     ("Returning",   "NEARPEDIA.nearpedia.txtReturning");
        nearpedia.clickBotonSearch();
        AllureReports.screenshot("Search results loaded", driver.getDriver());
    }

    /**
     * Selects the sort criterion from the "Sort By" dropdown.
     *
     * @param nearpedia Nearpedia page object
     */
    @Step("Apply sort filter: {testData.getData('SortBy', 1)}")
    private void applySortFilter(Nearpedia nearpedia) {
        nearpedia.selectListSort("Sort By", "NEARPEDIA.nearpedia.cmbSort", testData.getData("SortBy", 1));
        AllureReports.screenshot("Sort filter applied", driver.getDriver());
    }

    /**
     * Asserts that all flight prices appear in ascending order.
     *
     * @param nearpedia Nearpedia page object
     */
    @Step("Validate results are sorted by price ascending")
    private void validateSortOrder(Nearpedia nearpedia) {
        nearpedia.validarOrdenamiento();
        AllureReports.screenshot("Sort order validated", driver.getDriver());
    }
    
    
}
