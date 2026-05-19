package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
import org.apache.poi.util.Units;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJcTable;

import automation.Driver;

/**
 * This class contains the methods to generate a report in .docx format and report the results obtained from automated scripts.
 * @author Osiris Montiel Campos
 * @version 2025-05-16
 */
public class Reporter {
	
	//------------------------------------------------------------------------------------------------
	// Global Variables
	//------------------------------------------------------------------------------------------------
	// Logger variable used to generate logs
		org.apache.logging.log4j.Logger logger = LogManager.getLogger(Reporter.class);
	/**
	 * Incremental variable used for the test case number
	 */
	private static int contador;
	/**
	 * Instance of the general document for reporting  
	 */
	public static XWPFDocument documento;
	/**
	 * Class constructor responsible for initializing the step counter variable
	 */
	public Reporter() {
		contador=0;
	}
	
	//------------------------------------------------------------------------------------------------
	// Getters and Setters
	//------------------------------------------------------------------------------------------------
	
	/**
	 * Getter method for the documento variable
	 * @return documento variable
	 */
	public XWPFDocument getDocumento() {
		return documento;
	}
	//------------------------------------------------------------------------------------------------
	// Reporter Class Methods
	//------------------------------------------------------------------------------------------------
	/**
	 * Method responsible for creating and initializing the report and loading it into memory.
	 * @param version Version of the report to generate
	 * @param folio Folio assigned to the script execution
	 * @param descripcion Description of the folio assigned to the script execution
	 * @param autor Person in charge of script execution
	 */
	public void crearReporte(String version, String folio, String descripcion, String autor) {
			// Request document instance creation
			logger.info("Requesting document instance creation");
			creaIntanciaReporte();
			// Request writing the title on the document cover
			logger.info("Requesting title writing on the document cover");
			crearTituloReporte();
			logger.info("Requesting report data table writing");
			// Request writing the report data table
			crearTablaDatos(version, folio, descripcion, autor);
	}
	/**
	 * Method responsible for generating and writing the report in .docx format
	 * @param nombreDocumento Name of the document to be saved in .docx format  
	 * @param path Path where the document will be saved
	 */
	public void generarRepote(String nombreDocumento, String path) {
		try {
			// Report is generated in the selected path
			logger.info("Generating report in the selected path");
			FileOutputStream Reporter = new FileOutputStream(new File(path + "\\" + nombreDocumento + ".docx"));
			documento.write(Reporter);
			Reporter.close();
			logger.info("Report generated successfully");
		} catch (IOException ex) {
			logger.info("Report was not generated");
			Logger.getLogger(Reporter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	/**
	 * Method responsible for writing the report title on the document cover.
	 */
	private void creaIntanciaReporte() {
		try {
			// Create document to work with
			logger.info("Creating report to work with");
			String archivin = System.getProperty("user.dir") + "\\template\\FormatoEvidencias.docx";
			documento = new XWPFDocument(new FileInputStream(new File(archivin)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method responsible for writing the report title on the document cover.
	 */
	private void crearTituloReporte() {
		// Generate 5 line breaks 
		crearSaltosLineas(5);
		// Add a new paragraph and align to center
		XWPFParagraph tituloDoc = documento.createParagraph();
		tituloDoc.setAlignment(ParagraphAlignment.CENTER);
		tituloDoc.setVerticalAlignment(TextAlignment.TOP);
		// Create title and assign properties
		XWPFRun titulo = tituloDoc.createRun();
		titulo.setBold(true);
		titulo.setText("Automated Testing Report");
		titulo.setFontFamily("Lucida Sans Unicode");
		titulo.setFontSize(36);
		titulo.setTextPosition(10);
		titulo.setUnderline(UnderlinePatterns.SINGLE);
		// Generate 12 line breaks 
		crearSaltosLineas(12);
	}
	/**
	 * Method responsible for generating the report data table.
	 * @param version Version of the report to generate
	 * @param folio Folio assigned to the script execution
	 * @param descripcion Description of the folio assigned to the script execution
	 * @param autor Person in charge of script execution
	 */
	private void crearTablaDatos(String version, String folio, String descripcion, String autor) {
		// Generate date for the report date column
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// Create table
		XWPFTable table = documento.createTable();
		table.setRowBandSize(2);
		// Call method to align the table
		setTableAlign(table, ParagraphAlignment.CENTER);

		// Add first row of the table
		XWPFTableRow tableRowOne = table.getRow(0);
		XWPFParagraph paragraph = tableRowOne.getCell(0).addParagraph();
		
		// Create first column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Version", true, false);
		tableRowOne.addNewTableCell();
		paragraph = tableRowOne.getCell(1).addParagraph();
		// Create second column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Date", true, false);
		tableRowOne.addNewTableCell();
		paragraph = tableRowOne.getCell(2).addParagraph();
		// Create third column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Folio", true, false);
		tableRowOne.addNewTableCell();
		paragraph = tableRowOne.getCell(3).addParagraph();
		// Create fourth column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Description", true, false);
		tableRowOne.addNewTableCell();
		paragraph = tableRowOne.getCell(4).addParagraph();
		// Create fifth column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Tester", true, false);
		paragraph = tableRowOne.getCell(4).addParagraph();
		
		// Color the table headers
		for (int i = 0; i < 5; i++) {
			tableRowOne.getCell(i).setColor("0B4DA2");
		}
		// Add second row of the table
		XWPFTableRow tableRowTwo = table.createRow();

		// Create first column in second row
		paragraph = tableRowTwo.getCell(0).addParagraph();
		setRun(paragraph.createRun(), "Arial", 10, "000000",version, false, false);
		// Create second column in second row
		paragraph = tableRowTwo.getCell(1).addParagraph();
		setRun(paragraph.createRun(), "Arial", 10, "000000", dateFormat.format(date), false, false);
		// Create third column in second row
		paragraph = tableRowTwo.getCell(2).addParagraph();
		setRun(paragraph.createRun(), "Arial", 10, "000000", folio, false, false);
		// Create fourth column in second row
		paragraph = tableRowTwo.getCell(3).addParagraph();
		setRun(paragraph.createRun(), "Arial", 10, "000000", descripcion, false, false);
		// Create fifth column in second row
		paragraph = tableRowTwo.getCell(4).addParagraph();
		setRun(paragraph.createRun(), "Arial", 10, "000000", autor, false, false);
		paragraph = tableRowTwo.getCell(4).addParagraph();
		
		// Create line break
		crearSaltoLinea();
		crearSaltoLinea();
	}
	/**
	 * Method responsible for generating a test step with its description and evidence.
	 * @param pDescripcion Test step description
	 * @param driver Instance of the driver used
	 * @param screenShot Flag indicating if the step requires a screenshot or not  
	 */
	public void agregarPaso(String pDescripcion, Driver driver, Boolean screenShot) {
		logger.info("Adding a new step: {}", pDescripcion);
		// Generate new table
		XWPFTable table = documento.createTable();
		// Increase step counter
		contador = contador + 1;
		// Call method to align the table
		setTableAlign(table, ParagraphAlignment.LEFT);
		// Add first row of the table
		XWPFTableRow tableRowOne = table.getRow(0);
		XWPFParagraph paragraph = tableRowOne.getCell(0).addParagraph();
		paragraph.createRun();
		
		// Create first column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Step", true, false);
		tableRowOne.addNewTableCell();
		paragraph = tableRowOne.getCell(1).addParagraph();
		// Create second column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Description", true, false);
		tableRowOne.addNewTableCell();
		paragraph = tableRowOne.getCell(2).addParagraph();
		// Create third column
		setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "Evidence", true, false);
		paragraph = tableRowOne.getCell(2).addParagraph();
	
		// Color table headers
		for (int i = 0; i < 3; i++) {
			tableRowOne.getCell(i).setColor("0B4DA2");
		}
		// Add second row of the table
		XWPFTableRow tableRowTwo = table.createRow();
		// Create first column in second row
		paragraph = tableRowTwo.getCell(0).addParagraph();
		setRun(paragraph.createRun(), "Arial", 10, "000000", "" + contador, false, false);
		// Create second column in second row
		paragraph = tableRowTwo.getCell(1).addParagraph();
		setRun(paragraph.createRun(), "Arial", 10, "000000", pDescripcion, false, false);
		// Create third column in second row
		paragraph = tableRowTwo.getCell(2).addParagraph();
		// Validate if screenshot is required
		if (screenShot == true) {
			setRunPicture(paragraph.createRun(), driver);
		}else {
			setRun(paragraph.createRun(), "Arial", 10, "FFFFFF", "………………………………………………………………………………", false, false);
		}
		paragraph = tableRowTwo.getCell(2).addParagraph();
		logger.info("New step added successfully");
	}
	/**
	 * Method responsible for adding content to a table cell 
	 * @param run Cell location where data will be added
	 * @param fontFamily Font family used
	 * @param fontSize Font size used
	 * @param colorRGB Font color used
	 * @param text Text to be added to the cell
	 * @param bold True if text should be bold, False otherwise
	 * @param addBreak True if a line break is required, False otherwise
	 */
	private void setRun(XWPFRun run, String fontFamily, int fontSize, String colorRGB, String text, boolean bold, boolean addBreak) {
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		run.setColor(colorRGB);
		run.setText(text);
		run.setBold(bold);
		if (addBreak)
			run.addBreak();
	}
	/**
	 * Method responsible for adding a Screenshot to a table cell
	 * @param run Cell location where data will be added
	 * @param driver Instance of the driver used
	 */
	private void setRunPicture(XWPFRun run, Driver driver){
			// Create new paragraph and align to the center of the cell
			XWPFParagraph parrafo = documento.createParagraph();
			parrafo.setAlignment(ParagraphAlignment.CENTER);
			WebDriver webdriver = driver.getDriver();
			try {
				// Get current browser screenshot
				File imagen = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
				InputStream pic = new FileInputStream(imagen);
				// Add image to cell
				run.addPicture(pic, XWPFDocument.PICTURE_TYPE_JPEG, "", Units.toEMU(350), Units.toEMU(235));
			} catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException | IOException e) {
				e.printStackTrace();
			}
	}
	/**
	 * Method responsible for aligning a table within the document
	 * @param table Instance of the table to be aligned
	 * @param align Position to align to
	 */
	private void setTableAlign(XWPFTable table,ParagraphAlignment align) {
	    CTTblPr tblPr = table.getCTTbl().getTblPr();
	    CTJcTable jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
	    STJcTable.Enum en = STJcTable.Enum.forInt(align.getValue()-1);
	    jc.setVal(en);
	}

	/**
	 * Method responsible for adding a line break in the document
	 */
	private void crearSaltoLinea() {
		XWPFParagraph parrafo = documento.createParagraph();
		parrafo.setAlignment(ParagraphAlignment.BOTH);
		XWPFRun r2 = parrafo.createRun();
		r2.addCarriageReturn();
	}
	/**
	 * Method responsible for adding a specific number of line breaks in the document
	 * @param num Number of line breaks to add to the document
	 */
	private void crearSaltosLineas(int num) {
		for (int i = 0; i < num; i++) {
			XWPFParagraph parrafo = documento.createParagraph();
			parrafo.setAlignment(ParagraphAlignment.BOTH);
			XWPFRun r2 = parrafo.createRun();
			r2.addCarriageReturn();
		}
	}

}