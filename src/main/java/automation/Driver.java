package automation;

import java.util.Set;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Manages the initialization and lifecycle of the WebDriver session.
 *
 * @author Osiris Montiel Campos
 * @version 2025-05-22
 */
public class Driver {

    /**
     * Active WebDriver session.
     */
    private WebDriver driver;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Driver.class);

    /**
     * Initializes a new WebDriver session for the specified browser.
     *
     * @param browser browser to launch: {@code "CHROME"}, {@code "FIREFOX"}, or {@code "EDGE"}
     * @throws IllegalArgumentException if the browser name is not recognized
     */
    public Driver(String browser) {
        switch (browser.toUpperCase()) {

            case "CHROME":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--disable-save-password-bubble");
                chromeOptions.setExperimentalOption("prefs", Map.of(
                        "credentials_enable_service", false,
                        "profile.password_manager_enabled", false
                ));
                if (System.getenv("CI") != null) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--allow-file-access-from-files");
                    chromeOptions.addArguments("--disable-web-security");
                }
                driver = new ChromeDriver(chromeOptions);
                logger.info("Chrome browser initialized — headless: {}", System.getenv("CI") != null);
                break;

            case "FIREFOX":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (System.getenv("CI") != null) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }
                driver = new FirefoxDriver(firefoxOptions);
                logger.info("Firefox browser initialized — headless: {}", System.getenv("CI") != null);
                break;

            case "EDGE":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (System.getenv("CI") != null) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--no-sandbox");
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    edgeOptions.addArguments("--disable-gpu");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                driver = new EdgeDriver(edgeOptions);
                logger.info("Edge browser initialized — headless: {}", System.getenv("CI") != null);
                break;

            default:
                logger.error("Invalid browser provided: {}", browser);
                throw new IllegalArgumentException(
                        "Invalid browser: '" + browser + "'. Valid options: CHROME, FIREFOX, EDGE");
        }
    }

    // ------------------------------------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------------------------------------

    /**
     * Returns the active {@link WebDriver} instance.
     *
     * @return current WebDriver session
     */
    public WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Ends the test session by closing all open browser windows.
     * <p>
     * Equivalent to calling {@link #closeAll()}. Prefer this method in
     * teardown hooks for readability.
     */
    public void finishExecution() {
        closeAll();
    }

    /**
     * Maximizes the currently active browser window.
     */
    public void maximizeWindows() {
        this.driver.manage().window().maximize();
        logger.info("Browser window maximized");
    }

    /**
     * Closes all browser windows and terminates the WebDriver session.
     */
    public void closeAll() {
        if (driver != null) {
            logger.info("Closing all browser windows");
            driver.quit();
        } else {
            logger.warn("No active browser windows to close");
        }
    }

    /**
     * Navigates back to the previous page in the browser history.
     */
    public void back() {
        this.driver.navigate().back();
    }

    /**
     * Navigates forward to the next page in the browser history.
     */
    public void forward() {
        this.driver.navigate().forward();
    }

    /**
     * Reloads the current page.
     */
    public void refresh() {
        this.driver.navigate().refresh();
    }

    /**
     * Navigates to the specified URL.
     *
     * @param url fully qualified URL (e.g., {@code "https://example.com"})
     */
    public void goTo(String url) {
        this.driver.get(url);
    }

    /**
     * Closes the browser and ends the WebDriver session.
     *
     * @see #closeAll()
     */
    public void closeBrowser() {
        this.driver.quit();
    }

    /**
     * Closes the currently active browser tab without ending the session.
     * Use {@link #closeAll()} to terminate the entire session.
     */
    public void closeCurrentTab() {
        this.driver.close();
    }

    /**
     * Returns the URL of the currently active page.
     *
     * @return current page URL as a {@link String}
     */
    public String getCurrentUrl() {
        return this.driver.getCurrentUrl();
    }

    /**
     * Deletes all cookies from the current browser session.
     */
    public void deleteAllCookies() {
        this.driver.manage().deleteAllCookies();
    }

    /**
     * Returns all cookies present in the current browser session.
     *
     * @return unmodifiable {@link Set} of {@link Cookie} objects
     */
    public Set<Cookie> getAllCookies() {
        return this.driver.manage().getCookies();
    }

    /**
     * Returns the cookie with the specified name.
     *
     * @param cookie name of the cookie to retrieve
     * @return matching {@link Cookie}, or {@code null} if not found
     */
    public Cookie getCookie(String cookie) {
        return this.driver.manage().getCookieNamed(cookie);
    }

    /**
     * Adds a cookie to the current browser session.
     *
     * @param cookie {@link Cookie} to add
     */
    public void addCookie(Cookie cookie) {
        this.driver.manage().addCookie(cookie);
    }
}
