package automation;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import util.Reporter;

/**
 * Provides high-level interaction methods for web UI elements.
 *
 * @author Osiris Montiel Campos
 * @version 2025-05-22
 */
public class UIElement {

    /**
     * Active driver session.
     */
    private Driver driver;

    /**
     * Last located web element.
     */
    private WebElement element;

    /**
     * Reporter instance for evidence steps.
     */
    private Reporter reporte;

    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UIElement.class);

    /**
     * Creates a new {@code UIElement} bound to the given driver session.
     *
     * @param driver  active driver session
     * @param reporte shared Reporter instance initialized by the test
     */
    public UIElement(Driver driver, Reporter reporte) {
        this.driver = driver;
        this.reporte = reporte;
    }

    // ------------------------------------------------------------------------------------------------
    // Locator factory — single source of truth for By conversion
    // ------------------------------------------------------------------------------------------------

    /**
     * Converts a locator type string and value into a Selenium {@link By} object.
     *
     * @param by          locator strategy (case-sensitive)
     * @param localizador locator value
     * @return corresponding {@link By} instance
     * @throws IllegalArgumentException if {@code by} is not one of the supported strategies
     */
    public static By toBy(String by, String localizador) {
        return switch (by) {
            case "id" -> By.id(localizador);
            case "name" -> By.name(localizador);
            case "cssSelector" -> By.cssSelector(localizador);
            case "xpath" -> By.xpath(localizador);
            case "linkText" -> By.linkText(localizador);
            default ->
                    throw new IllegalArgumentException("Unsupported locator type: '" + by + "'. " + "Allowed values: id, name, cssSelector, xpath, linkText");
        };
    }

    /**
     * Navigates to the specified URL and optionally records an evidence step.
     *
     * @param url        fully qualified URL to open
     * @param reportar   {@code true} to add a step to the evidence report
     * @param screenShot {@code true} to attach a screenshot to the report step
     */
    public void goToURL(String url, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Opening URL: \"" + url + "\"";
        driver.goTo(url);
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Finds a single element on the active page using the specified locator.
     *
     * @param by          locator strategy — see {@link #toBy(String, String)} for valid values
     * @param localizador locator value
     * @return the located {@link WebElement}, or {@code null} if not found
     */
    public WebElement findElement(String by, String localizador) {
        try {
            this.element = driver.getDriver().findElement(toBy(by, localizador));
            logger.info("Element found by {}: {}", by, this.element);
        } catch (NoSuchElementException e) {
            this.element = null;
            logger.error("Element not found — locator '{}' ({}): {}", localizador, by, e.getMessage());
        }
        return this.element;
    }

    /**
     * Clicks an element and optionally records an evidence step.
     *
     * @param elemento    element to click
     * @param nomElemento descriptive name used in the report step
     * @param reportar    {@code true} to add a step to the evidence report
     * @param screenShot  {@code true} to attach a screenshot to the report step
     */
    public void click(WebElement elemento, String nomElemento, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Clicking on \"" + nomElemento + "\"";
        elemento.click();
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Clicks a button element and optionally records an evidence step.
     *
     * @param button     button element to click
     * @param nomButton  descriptive name used in the report step
     * @param reportar   {@code true} to add a step to the evidence report
     * @param screenShot {@code true} to attach a screenshot to the report step
     * @throws RuntimeException wrapping the original exception if the click fails
     */
    public void clickButton(WebElement button, String nomButton, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Clicking button \"" + nomButton + "\"";
        try {
            button.click();
        } catch (Exception e) {
            logger.error("Failed to click button '{}': {}", nomButton, e.getMessage());
            throw new RuntimeException("Click failed on button: " + nomButton, e);
        }
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Clicks a radio button and optionally records an evidence step.
     *
     * @param radioButton    radio button element to click
     * @param nomRadioButton descriptive name used in the report step
     * @param reportar       {@code true} to add a step to the evidence report
     * @param screenShot     {@code true} to attach a screenshot to the report step
     */
    public void clickRadioButton(WebElement radioButton, String nomRadioButton, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Clicking radio button \"" + nomRadioButton + "\"";
        radioButton.click();
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Clicks a checkbox and optionally records an evidence step.
     *
     * @param checkButton    checkbox element to click
     * @param nomCheckButton descriptive name used in the report step
     * @param reportar       {@code true} to add a step to the evidence report
     * @param screenShot     {@code true} to attach a screenshot to the report step
     */
    public void clickCheckButton(WebElement checkButton, String nomCheckButton, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Clicking checkbox \"" + nomCheckButton + "\"";
        checkButton.click();
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Clicks a hyperlink and optionally records an evidence step.
     *
     * @param link       link element to click
     * @param nomLink    descriptive name used in the report step
     * @param reportar   {@code true} to add a step to the evidence report
     * @param screenShot {@code true} to attach a screenshot to the report step
     */
    public void clickLink(WebElement link, String nomLink, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Clicking link \"" + nomLink + "\"";
        link.click();
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Types text into the specified input element and optionally records an evidence step.
     *
     * @param elemento    target input element
     * @param nomElemento descriptive name used in the report step
     * @param texto       text to type
     * @param reportar    {@code true} to add a step to the evidence report
     * @param screenShot  {@code true} to attach a screenshot to the report step
     */
    public void setText(WebElement elemento, String nomElemento, String texto, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Typing text into \"" + nomElemento + "\"";
        elemento.sendKeys(texto);
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Selects an option from a {@code <select>} element by its zero-based index.
     *
     * @param lista      {@code <select>} element
     * @param nomLista   descriptive name used in the report step
     * @param opcion     zero-based index of the option to select
     * @param reportar   {@code true} to add a step to the evidence report
     * @param screenShot {@code true} to attach a screenshot to the report step
     */
    public void selectListByIndex(WebElement lista, String nomLista, Integer opcion, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Selecting option " + (opcion + 1) + " from list \"" + nomLista + "\"";
        new Select(lista).selectByIndex(opcion);
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Selects an option from a {@code <select>} element by its {@code value} attribute.
     *
     * @param lista      {@code <select>} element
     * @param nomLista   descriptive name used in the report step
     * @param opcion     value of the {@code value} attribute of the option to select
     * @param reportar   {@code true} to add a step to the evidence report
     * @param screenShot {@code true} to attach a screenshot to the report step
     */
    public void selectListByValue(WebElement lista, String nomLista, String opcion, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Selecting \"" + opcion + "\" by value from list \"" + nomLista + "\"";
        new Select(lista).selectByValue(opcion);
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Selects an option from a {@code <select>} element by its visible text.
     *
     * @param lista      {@code <select>} element
     * @param nomLista   descriptive name used in the report step
     * @param opcion     visible text of the option to select
     * @param reportar   {@code true} to add a step to the evidence report
     * @param screenShot {@code true} to attach a screenshot to the report step
     */
    public void selectListByText(WebElement lista, String nomLista, String opcion, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Selecting \"" + opcion + "\" by text from list \"" + nomLista + "\"";
        new Select(lista).selectByVisibleText(opcion);
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Deselects all selected options from a multi-select {@code <select>} element.
     *
     * @param lista      {@link Select} wrapper for the list element
     * @param nomLista   descriptive name used in the report step
     * @param reportar   {@code true} to add a step to the evidence report
     * @param screenShot {@code true} to attach a screenshot to the report step
     */
    public void deselectAllList(Select lista, String nomLista, Boolean reportar, Boolean screenShot) {
        String stepDescription = "Deselecting all options from list \"" + nomLista + "\"";
        lista.deselectAll();
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Records a validation step in the evidence report.
     *
     * @param stepDescription text describing the validation result
     * @param reportar        to add a step to the evidence report
     * @param screenShot      to attach a screenshot to the report step
     */
    public void validacion(String stepDescription, Boolean reportar, Boolean screenShot) {
        logger.info(stepDescription);
        if (reportar) {
            reporte.agregarPaso(stepDescription, this.driver, screenShot);
        }
    }

    /**
     * Scrolls the active page by the specified pixel amounts.
     *
     * @param pxHorizontal horizontal scroll distance in pixels (positive = right, negative = left)
     * @param pxVertical   vertical scroll distance in pixels (positive = down, negative = up)
     */
    public void scrollByValue(Integer pxHorizontal, Integer pxVertical) {
        logger.info("Scrolling page by ({}, {}) pixels", pxHorizontal, pxVertical);
        new Actions(this.driver.getDriver()).scrollByAmount(pxHorizontal, pxVertical).perform();
    }
}
