package util;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import automation.Driver;

/**
 * This class handles the waiting time functionality for elements on the screen
 * @author Osiris Montiel Campos
 * @version 2025-08-28
 */
public class WaitElements {
	//------------------------------------------------------------------------------------------------
	// Global Variables
	//------------------------------------------------------------------------------------------------
	// Logger variable used to generate logs
    org.apache.logging.log4j.Logger logger = LogManager.getLogger(Driver.class);
	/**
	 * Driver variable used as an instance of the current session
	 */
	private Driver driver;
    /**
     * Variable responsible for the number of attempts to locate an element
     */
	static int contador = 0;
	/**
	 * Class constructor responsible for initializing the driver instance
	 * @param driver Current session instance
	 */
	public WaitElements(Driver driver) {
		this.driver = driver;
	}
	/**
	 * Method responsible for waiting for the page to finish loading in the browser
	 */
	public void pageComplete() {
		Duration PAGETIMEOUT = Duration.ofMinutes(3);
		WebDriverWait waitPage = new WebDriverWait(driver.getDriver(), PAGETIMEOUT);
		try {
			logger.info("Starting page load for: " + driver.getDriver().getTitle());
			waitPage.until(webDriver -> ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete"));			
			logger.info("The page " + driver.getDriver().getTitle() + " loaded successfully");
		}catch(Exception e) {
			logger.error("The page could not be loaded");
			driver.finishExecution();
		}
	}
	/**
	 * Method responsible for waiting for an element to finish loading and become visible in the browser
	 * @param by Locator type used to search for the element
	 * @param localizador Locator value
	 * @return Flag that returns True if the element was found or False if not
	 */
	public boolean visibilityElement(String by, String localizador) {
		Duration OBJECTOUTPAGE = Duration.ofSeconds(30);
		boolean ban = false;
		WebDriverWait wait = new WebDriverWait(driver.getDriver(), OBJECTOUTPAGE);
		switch (by) {
		case "id":
			try {
				// Search and wait for the element to appear on screen
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(localizador)));
				ban = true;
			}catch(Exception e){
				ban = false;
				logger.error("Wait time expired for object {}", localizador);
			}
			break;
		case "name":
			try {
				// Search and wait for the element to appear on screen
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(localizador)));
				ban = true;
			}catch(Exception e){
				ban = false;
				logger.error("Wait time expired for the object");
			}
			break;
		case "cssSelector":
			try {
				// Search and wait for the element to appear on screen
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(localizador)));
				ban = true;
			}catch(Exception e){
				ban = false;
				logger.error("Wait time expired for the object");
			}
			break;
		case "linkText":
			try {
				// Search and wait for the element to appear on screen
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(localizador)));
				ban = true;
			}catch(Exception e){
				ban = false;
				logger.error("Wait time expired for the object");
			}
			break;
		case "xpath":
			try {
				// Search and wait for the element to appear on screen
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(localizador)));
				ban = true;
			}catch(Exception e){
				ban = false;
				logger.error("Wait time expired for the object");
			}
			break;
		default:
			// If the element does not load
			ban = false;
			logger.info("Element did not load on screen");
			break;
		}
		// Return the found locator status
		return ban;
	}
	/**
	 * Method responsible for pausing the execution for a specific amount of time
	 * @param seconds Number of seconds the execution will stop
	 */
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
	}
}