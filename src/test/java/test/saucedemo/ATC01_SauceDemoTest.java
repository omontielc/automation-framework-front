package test.saucedemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import automation.TestBase;
import front.pages.SauceDemoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

/**
 * Happy path test for the SauceDemo e-commerce application.
 * Pure Selenium implementation — no UIElement, Reporter or ObjectRepository.
 * Flow: login → validate inventory → add to cart → checkout → confirm order.
 *
 * @author Osiris Montiel Campos
 * @version 2025-07-01
 */
@Epic("SauceDemo E-Commerce")
@Feature("Purchase Flow")
public class ATC01_SauceDemoTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(ATC01_SauceDemoTest.class);

    /**
     * Registers this test class with {@link TestBase} using {@link #ATC_NAME}.
     */
    public ATC01_SauceDemoTest() {
        super(ATC01_SauceDemoTest.class.getSimpleName());
    }

    /**
     * Happy path: standard user logs in, adds a product, completes checkout,
     * and verifies the order confirmation message.
     */
    @Test
    @Story("Standard user completes a full purchase")
    @Description("""
            Given the user is on the SauceDemo login page
            When they log in with valid credentials
            And add the Sauce Labs Backpack to the cart
            And complete the checkout form with customer info
            Then the order confirmation page should display 'Thank you for your order!'
            """)
    @Severity(SeverityLevel.CRITICAL)
    public void verifyHappyPathPurchase() {
        SauceDemoPage page = new SauceDemoPage(getDriver().getDriver());

        login(page);
        validateInventory(page);
        addToCart(page);
        goToCheckout(page);
        fillInfo(page);
        confirmOrder(page);

        logger.info("Happy path purchase completed successfully");
    }

    /**
     * Enters credentials and submits the login form.
     *
     * @param page SauceDemoPage page object
     */
    @Step("Login with valid credentials")
    private void login(SauceDemoPage page) {
        page.setUsername(getTestData().getData("Username", 1));
        page.setPassword(getTestData().getData("Password", 1));
        page.clickLogin();
    }

    /**
     * Asserts the inventory page is displayed after login.
     *
     * @param page SauceDemoPage page object
     */
    @Step("Validate inventory page is displayed")
    private void validateInventory(SauceDemoPage page) {
        page.validateInventoryPage();
    }

    /**
     * Adds the first product to the shopping cart.
     *
     * @param page SauceDemoPage page object
     */
    @Step("Add first product to cart")
    private void addToCart(SauceDemoPage page) {
        page.addFirstProductToCart();
    }

    /**
     * Opens the cart and proceeds to checkout.
     *
     * @param page SauceDemoPage page object
     */
    @Step("Open cart and proceed to checkout")
    private void goToCheckout(SauceDemoPage page) {
        page.openCart();
        page.clickCheckout();
    }

    /**
     * Fills in customer information and continues to the order summary.
     *
     * @param page SauceDemoPage page object
     */
    @Step("Fill customer information and continue")
    private void fillInfo(SauceDemoPage page) {
        page.fillCustomerInfo(
                getTestData().getData("FirstName", 1),
                getTestData().getData("LastName", 1),
                getTestData().getData("ZipCode", 1)
        );
        page.clickContinue();
    }

    /**
     * Places the order and validates the confirmation message.
     *
     * @param page SauceDemoPage page object
     */
    @Step("Finish purchase and validate confirmation")
    private void confirmOrder(SauceDemoPage page) {
        page.clickFinish();
        page.validateOrderConfirmation();
    }
}