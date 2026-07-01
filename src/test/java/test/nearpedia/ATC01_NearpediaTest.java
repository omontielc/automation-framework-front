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

@Epic("Nearpedia Flight Search")
@Feature("Flight Search Results")
public class ATC01_NearpediaTest extends TestBase {

    private static final String ATC_NAME = "ATC01_NearpediaTest";
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ATC01_NearpediaTest.class);

    public ATC01_NearpediaTest() {
        super(ATC_NAME);
    }

    @Test
    @Story("User sorts flight results by price ascending")
    @Description("""
            Given the user is on the Nearpedia home page
            When they search for a round-trip flight and apply the 'Price' sort filter
            Then all displayed results must be ordered from lowest to highest price
            """)
    @Severity(SeverityLevel.CRITICAL)
    public void verifyFlightResultsSortedByPriceAscending() {
        Nearpedia nearpedia = new Nearpedia(getDriver(), getReporter());
        Menu menu = new Menu(getDriver(), getReporter());

        getReporter().crearReporte("1.0.0", ATC_NAME, "Test 1 - Nearpedia Flight Search", "Osiris Montiel");

        navigateToFlights(menu);
        fillSearchForm(nearpedia);
        applySortFilter(nearpedia);
        validateSortOrder(nearpedia);

        logger.info("Test execution completed successfully");

        DateFormat dateFormat = new SimpleDateFormat("d_MM_yyyy_HH,mm,ss");
        String reportName = "Report_" + getTestData().getData("TestCaseName", 1) + "_" + dateFormat.format(new Date());
        getReporter().generarRepote(reportName, Config.REPORTER_PATH);
    }

    @Step("Navigate to Flights menu")
    private void navigateToFlights(Menu menu) {
        menu.clickMenu("Flights", "NEARPEDIA.menu.menuFlight");
        AllureReports.screenshot("Flights menu opened", getDriver().getDriver());
    }

    @Step("Fill in flight search form")
    private void fillSearchForm(Nearpedia nearpedia) {
        nearpedia.selectListflightFrom("Flight From", "NEARPEDIA.nearpedia.cmbFlightFrom", getTestData().getData("FlightFrom", 1));
        nearpedia.selectListflightTo("Flight To", "NEARPEDIA.nearpedia.cmbFlightTo", getTestData().getData("FlightTo", 1));
        nearpedia.setTextDeparting("Departing", "NEARPEDIA.nearpedia.txtDeparting");
        nearpedia.setTxtReturning("Returning", "NEARPEDIA.nearpedia.txtReturning");
        nearpedia.clickBotonSearch();
        AllureReports.screenshot("Search results loaded", getDriver().getDriver());
    }

    @Step("Apply sort filter")
    private void applySortFilter(Nearpedia nearpedia) {
        nearpedia.selectListSort("Sort By", "NEARPEDIA.nearpedia.cmbSort", getTestData().getData("SortBy", 1));
        AllureReports.screenshot("Sort filter applied", getDriver().getDriver());
    }

    @Step("Validate results are sorted by price ascending")
    private void validateSortOrder(Nearpedia nearpedia) {
        nearpedia.validarOrdenamiento();
        AllureReports.screenshot("Sort order validated", getDriver().getDriver());
    }
}