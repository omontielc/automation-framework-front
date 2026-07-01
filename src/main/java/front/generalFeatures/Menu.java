package front.generalFeatures;

import java.util.Properties;

import org.openqa.selenium.WebElement;

import automation.Driver;
import automation.UIElement;
import util.ObjectRepository;
import util.WaitElements;
import util.Reporter;

/**
 * Encapsulates navigation interactions with the Nearpedia main menu.
 *
 * @author Osiris Montiel Campos
 * @version 2025-08-29
 */
public class Menu {

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

    /**
     * Reusable reference to the last located menu element.
     */
    private WebElement menu;

    /**
     * Creates a new Menu helper bound to the given driver session.
     *
     * @param driver active driver session
     */
    public Menu(Driver driver, Reporter reporte) {
        uiElement = new UIElement(driver, reporte);
        wait = new WaitElements(driver);
        objRepository = new ObjectRepository("Nearpedia").getObjectRepository();
    }

    /**
     * Clicks a menu item identified by its object repository key.
     *
     * @param nombreMenu name of the menu item (used for logging and evidence reports)
     * @param properties key in the object repository that maps to the element's XPath locator
     */
    public void clickMenu(String nombreMenu, String properties) {
        if (wait.visibilityElement("xpath", objRepository.getProperty(properties))) {
            menu = uiElement.findElement("xpath", objRepository.getProperty(properties));
            uiElement.clickButton(menu, nombreMenu, true, true);
        }
    }
}
