package front.pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import automation.Driver;
import automation.UIElement;
import io.qameta.allure.Step;
import util.ObjectRepository;
import util.WaitElements;

/**
 * Page Object for the Nearpedia flight search screen.
 * @author Osiris Montiel Campos
 * @version 2025-07-06
 */
public class Nearpedia {

    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Nearpedia.class);

    /** 
     * Active driver session. 
     */
    private final Driver driver;

    /** 
     * UI interaction helper. 
     */
    private final UIElement uiElement;

    /** 
     * Object repository containing element locators for this module. 
     */
    private final Properties objRepository;

    /** 
     * Explicit wait helper. 
     */
    private final WaitElements wait;

    private WebElement cmbFlightFrom;
    private WebElement cmbFlightTo;
    private WebElement txtDeparting;
    private WebElement txtReturning;
    private WebElement btnSearch;
    private WebElement cmbSort;

    //Departure date: tomorrow's date at test execution time.
    private final LocalDate fechaDeparting = LocalDate.now().plusDays(1);

    //Return date: six days from today at test execution time.
    private final LocalDate fechaReturning = LocalDate.now().plusDays(6);

    //Date formatter matching the application's expected input format.
    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Creates a new page object bound to the given driver session.
     * @param driver active driver session
     */
    public Nearpedia(Driver driver) {
        this.driver = driver;
        uiElement     = new UIElement(driver);
        wait          = new WaitElements(driver);
        objRepository = new ObjectRepository("Nearpedia").getObjectRepository();
    }
    
    /**
     * Selects the departure city from the "Flight From" dropdown.
     * @param nombreElemento descriptive name used in the evidence report
     * @param properties     object repository key for this element's locator
     * @param varTestData    visible text of the option to select
     */
    @Step("Select departure city: {varTestData}")
    public void selectListflightFrom(String nombreElemento, String properties, String varTestData) {
        if (wait.visibilityElement("id", objRepository.getProperty(properties))) {
            cmbFlightFrom = uiElement.findElement("id", objRepository.getProperty(properties));
            uiElement.selectListByText(cmbFlightFrom, nombreElemento, varTestData, true, true);
        }
    }

    /**
     * Selects the destination city from the "Flight To" dropdown.
     * @param nombreElemento descriptive name used in the evidence report
     * @param properties     object repository key for this element's locator
     * @param varTestData    visible text of the option to select
     */
    @Step("Select destination city: {varTestData}")
    public void selectListflightTo(String nombreElemento, String properties, String varTestData) {
        if (wait.visibilityElement("id", objRepository.getProperty(properties))) {
            cmbFlightTo = uiElement.findElement("id", objRepository.getProperty(properties));
            uiElement.selectListByText(cmbFlightTo, nombreElemento, varTestData, true, true);
        }
    }

    /**
     * Enters the dynamically computed departure date into the "Departing" field.
     * @param nombreElemento descriptive name used in the evidence report
     * @param properties     object repository key for this element's locator
     */
    @Step("Enter departure date: {fechaDeparting}")
    public void setTextDeparting(String nombreElemento, String properties) {
        if (wait.visibilityElement("id", objRepository.getProperty(properties))) {
            txtDeparting = uiElement.findElement("id", objRepository.getProperty(properties));
            uiElement.setText(txtDeparting, nombreElemento, fechaDeparting.format(formato), true, true);
        }
    }

    /**
     * Enters the dynamically computed return date into the "Returning" field.
     * @param nombreElemento descriptive name used in the evidence report
     * @param properties     object repository key for this element's locator
     */
    @Step("Enter return date: {fechaReturning}")
    public void setTxtReturning(String nombreElemento, String properties) {
        if (wait.visibilityElement("id", objRepository.getProperty(properties))) {
            txtReturning = uiElement.findElement("id", objRepository.getProperty(properties));
            uiElement.setText(txtReturning, nombreElemento, fechaReturning.format(formato), true, true);
        }
    }

    /**
     * Clicks the "Search" button to submit the flight search form.
     */
    @Step("Click Search button")
    public void clickBotonSearch() {
        if (wait.visibilityElement("cssSelector", objRepository.getProperty("NEARPEDIA.nearpedia.btnSearch"))) {
            btnSearch = uiElement.findElement("cssSelector", objRepository.getProperty("NEARPEDIA.nearpedia.btnSearch"));
            uiElement.clickButton(btnSearch, "Search", true, true);
        }
    }

    /**
     * Selects a sorting criterion from the "Sort By" dropdown.
     * @param nombreElemento descriptive name used in the evidence report
     * @param properties     object repository key for this element's locator
     * @param varTestData    visible text of the sorting option to select
     */
    @Step("Select sort criterion: {varTestData}")
    public void selectListSort(String nombreElemento, String properties, String varTestData) {
        if (wait.visibilityElement("id", objRepository.getProperty(properties))) {
            cmbSort = uiElement.findElement("id", objRepository.getProperty(properties));
            uiElement.selectListByText(cmbSort, nombreElemento, varTestData, true, true);
        }
    }

    /**
     * Asserts that all displayed flight results are sorted in ascending order by price.
     */
    @Step("Validate results sorted by price ascending")
    public void validarOrdenamiento() {
        List<WebElement> prices = driver.getDriver().findElements(By.xpath("//*[@id='results']/div/div[3]/span[1]"));
        System.out.println(prices);
        if (prices.isEmpty()) {
            Assert.fail("No flight results found — cannot validate sort order");
            return;
        }

        int previousPrice = 0;
        
        for (WebElement priceElement : prices) {
        	
        	Pattern patron = Pattern.compile("\\d+");

                Matcher buscador = patron.matcher(priceElement.getText());
                String numeroExtraido = "";
               // If the number is found in the current string
                if (buscador.find()) {
                	// We extract the found text
                	numeroExtraido = buscador.group();
                }
                int currentPrice = Integer.parseInt(numeroExtraido);
                Assert.assertTrue(currentPrice >= previousPrice,"Sort order violation: found " + previousPrice + " followed by " + currentPrice);
                previousPrice = currentPrice;
            }
        
        wait.sleep(5);
        uiElement.validacion("Results are correctly sorted in ascending order by price", true, true);
        logger.info("Sort order validated successfully across {} results", prices.size());
    }
}
