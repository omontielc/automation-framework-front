package front.pages;

import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.qameta.allure.Step;
import util.AllureReports;
import util.ObjectRepository;

/**
 * Page Object for the SauceDemo e-commerce application.
 * Locators are loaded from ObjectRepositorySauceDemo.properties.
 *
 * @author Osiris Montiel Campos
 * @version 2025-07-01
 */
public class SauceDemoPage {

    private static final Logger logger = LogManager.getLogger(SauceDemoPage.class);

    /**
     * Active WebDriver session.
     */
    private final WebDriver driver;

    /**
     * Explicit wait — 30 second timeout for all element interactions.
     */
    private final WebDriverWait wait;

    /**
     * Object repository containing element locators for this module.
     * Loaded from properties/ObjectRepositorySauceDemo.properties.
     */
    private final Properties objRepository;

    // --------------------------------------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------------------------------------

    /**
     * Creates a new SauceDemoPage bound to the given WebDriver session.
     * Loads the SauceDemo object repository automatically.
     *
     * @param driver active WebDriver session
     */
    public SauceDemoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.objRepository = new ObjectRepository("SauceDemo").getObjectRepository();
    }

    // --------------------------------------------------------------------------------------------
    // Private helper — single point of entry for element location
    // --------------------------------------------------------------------------------------------

    /**
     * Waits for an element to be visible and returns it.
     * Uses By.id as the default locator strategy — all SauceDemo locators are IDs or classNames.
     *
     * @param key    object repository key
     * @param byType locator strategy: "id", "className", "cssSelector", "xpath"
     * @return visible {@link WebElement}
     */
    private WebElement find(String key, String byType) {
        String locator = objRepository.getProperty(key);
        By by = switch (byType) {
            case "id" -> By.id(locator);
            case "className" -> By.className(locator);
            case "cssSelector" -> By.cssSelector(locator);
            case "xpath" -> By.xpath(locator);
            default -> throw new IllegalArgumentException("Unsupported locator type: " + byType);
        };
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Waits for an element to be clickable and returns it.
     *
     * @param key    object repository key
     * @param byType locator strategy: "id", "className", "cssSelector", "xpath"
     * @return clickable {@link WebElement}
     */
    private WebElement findClickable(String key, String byType) {
        String locator = objRepository.getProperty(key);
        By by = switch (byType) {
            case "id" -> By.id(locator);
            case "className" -> By.className(locator);
            case "cssSelector" -> By.cssSelector(locator);
            case "xpath" -> By.xpath(locator);
            default -> throw new IllegalArgumentException("Unsupported locator type: " + byType);
        };
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    // --------------------------------------------------------------------------------------------
    // Login Page Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Enters the username into the login form.
     *
     * @param username value to type into the username field
     */
    @Step("Enter username: {username}")
    public void setUsername(String username) {
        WebElement field = find("SAUCEDEMO.login.txtUsername", "id");
        field.clear();
        field.sendKeys(username);
        logger.info("Username entered: {}", username);
        AllureReports.screenshot("Username entered", driver);
    }

    /**
     * Enters the password into the login form.
     *
     * @param password value to type into the password field
     */
    @Step("Enter password")
    public void setPassword(String password) {
        WebElement field = find("SAUCEDEMO.login.txtPassword", "id");
        field.clear();
        field.sendKeys(password);
        logger.info("Password entered");
        AllureReports.screenshot("Password entered", driver);
    }

    /**
     * Clicks the Login button to submit the login form.
     */
    @Step("Click Login button")
    public void clickLogin() {
        findClickable("SAUCEDEMO.login.btnLogin", "id").click();
        logger.info("Login button clicked");
        AllureReports.screenshot("Login submitted", driver);
    }

    // --------------------------------------------------------------------------------------------
    // Inventory Page Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Asserts that the inventory page title reads "Products",
     * confirming that login was successful.
     */
    @Step("Validate inventory page is displayed")
    public void validateInventoryPage() {
        String title = find("SAUCEDEMO.inventory.title", "className").getText();
        Assert.assertEquals(title, "Products", "Inventory page title mismatch — login may have failed");
        logger.info("Inventory page validated — title: {}", title);
        AllureReports.screenshot("Inventory page loaded", driver);
    }

    /**
     * Clicks the 'Add to cart' button for the Sauce Labs Backpack.
     */
    @Step("Add Sauce Labs Backpack to cart")
    public void addFirstProductToCart() {
        findClickable("SAUCEDEMO.inventory.btnAddToCart", "id").click();
        logger.info("Sauce Labs Backpack added to cart");
        AllureReports.screenshot("Product added to cart", driver);
    }

    // --------------------------------------------------------------------------------------------
    // Cart Page Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Clicks the shopping cart icon to navigate to the cart page.
     */
    @Step("Open shopping cart")
    public void openCart() {
        findClickable("SAUCEDEMO.cart.btnCart", "id").click();
        logger.info("Shopping cart opened");
        AllureReports.screenshot("Cart opened", driver);
    }

    /**
     * Clicks the Checkout button on the cart page to begin the checkout flow.
     */
    @Step("Click Checkout button")
    public void clickCheckout() {
        findClickable("SAUCEDEMO.cart.btnCheckout", "id").click();
        logger.info("Checkout initiated");
        AllureReports.screenshot("Checkout initiated", driver);
    }

    // --------------------------------------------------------------------------------------------
    // Checkout Step One Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Fills in the customer information form on checkout step one.
     *
     * @param firstName customer first name
     * @param lastName  customer last name
     * @param zipCode   customer postal/zip code
     */
    @Step("Fill customer info: {firstName} {lastName} — zip: {zipCode}")
    public void fillCustomerInfo(String firstName, String lastName, String zipCode) {
        find("SAUCEDEMO.checkout.txtFirstName", "id").sendKeys(firstName);
        find("SAUCEDEMO.checkout.txtLastName", "id").sendKeys(lastName);
        find("SAUCEDEMO.checkout.txtZipCode", "id").sendKeys(zipCode);
        logger.info("Customer info filled — {} {} / {}", firstName, lastName, zipCode);
        AllureReports.screenshot("Customer info filled", driver);
    }

    /**
     * Clicks the Continue button to proceed to the order summary.
     */
    @Step("Click Continue button")
    public void clickContinue() {
        findClickable("SAUCEDEMO.checkout.btnContinue", "id").click();
        logger.info("Continue clicked");
        AllureReports.screenshot("Navigated to order summary", driver);
    }

    // --------------------------------------------------------------------------------------------
    // Checkout Step Two Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Clicks the Finish button on the order summary to place the order.
     */
    @Step("Click Finish button")
    public void clickFinish() {
        findClickable("SAUCEDEMO.checkout.btnFinish", "id").click();
        logger.info("Finish clicked — order placed");
        AllureReports.screenshot("Finish clicked", driver);
    }

    // --------------------------------------------------------------------------------------------
    // Checkout Complete Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Asserts that the order confirmation header reads "Thank you for your order!".
     */
    @Step("Validate order confirmation message")
    public void validateOrderConfirmation() {
        String header = find("SAUCEDEMO.checkout.confirmHeader", "className").getText();
        Assert.assertEquals(header, "Thank you for your order!", "Order confirmation header mismatch");
        logger.info("Order confirmed — header: {}", header);
        AllureReports.screenshot("Order confirmed", driver);
    }
}