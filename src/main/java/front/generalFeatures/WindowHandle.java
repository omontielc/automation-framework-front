package front.generalFeatures;

import java.util.Set;

import org.apache.logging.log4j.LogManager;

import automation.Driver;
import util.WaitElements;

/**
 * This class handles the window switching functionality of the system.
 * @author Osiris Montiel Campos
 * @version 2025-10-16
 */
public class WindowHandle {
	//------------------------------------------------------------------------------------------------
	// Global Variables
	//------------------------------------------------------------------------------------------------
	// Logger variable used to generate logs
	org.apache.logging.log4j.Logger logger = LogManager.getLogger(WindowHandle.class);
	/**
	 * Driver variable used as an instance of the current session 
	 */
	Driver driver;
	/**
	 * Wait variable used as an instance of the Wait class 
	 */
	WaitElements wait;
	//------------------------------------------------------------------------------------------------
	// Object Declaration
	//------------------------------------------------------------------------------------------------
	/**
	 * Class constructor responsible for initializing the driver and wait instances.
	 * @param driver Current session instance
	 */
	public WindowHandle(Driver driver){
		this.driver = driver;
		wait = new WaitElements(driver);
	}
	/**
	 * Method responsible for switching focus to a new window in the system.
	 * @throws InterruptedException Exception thrown in case of Thread failure
	 */
	public void handleNewWindow() throws InterruptedException{
		// Wait for the page to load
		wait.sleep(1);
		wait.pageComplete();
		// Get the ID of the main page 
		String mainTab = driver.getDriver().getWindowHandle();
		// Get the IDs of all active pages 
		Set<String> handlesWindow = driver.getDriver().getWindowHandles();
		// Iterate through current pages
		for(String actual : handlesWindow) {
			// If the page is different from the main page
			if (!actual.equalsIgnoreCase(mainTab)) {
				// Switch to the current page
				driver.getDriver().switchTo().window(actual);
				logger.info("Switching focus to the new page");
			}
		}
		// Wait for the page to load
		wait.pageComplete();
		wait.sleep(10);
	}
	/**
	 * Method responsible for switching focus back to the main window system. 
	 * @param mainTab Main page ID
	 * @throws InterruptedException Exception thrown in case of Thread failure
	 */
	public void mainHandleWindow(String mainTab) throws InterruptedException{
		wait.sleep(3);
		wait.pageComplete();
		// Switch to the main page
		driver.getDriver().switchTo().window(mainTab);
		logger.info("Switching focus to the main page");
	}
}