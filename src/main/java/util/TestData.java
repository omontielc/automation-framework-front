package util;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import config.Config;
import org.apache.logging.log4j.LogManager;
/**
 * This class contains the methods for obtaining test data from an Excel file.
 * @author Osiris Montiel Campos
 * @version 14/08/2023
 */
public class TestData {

	//------------------------------------------------------------------------------------------------
	// Global Variables
	//------------------------------------------------------------------------------------------------
	// Logger variable used to generate logs
	org.apache.logging.log4j.Logger logger=LogManager.getLogger(TestData.class);
	/**
	 * ExcelWBook variable used for creating the excel file
	 */
	private static XSSFWorkbook ExcelWBook;
	/**
	 * ExcelWSheet variable used for creating the excel file sheet
	 */
	private static XSSFSheet ExcelWSheet;
	/**
	 * testData matrix used to store test data in memory
	 */
	private String testData [][];
	/**
	 * ATC_Name variable contains the name of the test case being executed
	 */
	private String ATC_Name;
	/**
	 * NUM_ATC constant contains the number of test cases being executed
	 */
	private int NUM_ATC;
	/**
	 * MAX_ROW constant contains the number of rows in the file
	 */
	private int MAX_ROW;
	/**
	 * MAX_COL constant contains the number of columns in the file
	 */
	private int MAX_COL;
	// Generic row variable
	private int row=0;
	// Generic column variable
	private int col=0;
	/**
	 * Class constructor used to initialize the testData instance
	 * @param sheetName Name of the file sheet
	 * @param ATC_Name Name of the test case
	 */
	public TestData(String sheetName, String ATC_Name) {
		try {
			// Assign value to the ATC_Name variable
			this.ATC_Name = ATC_Name;
			// Create the testData instance
			crearTestData(sheetName);
			// Get the number of rows
			MAX_ROW = getNumRows();
			// Get the number of columns
			MAX_COL = getNumCols();
			// Get the number of test cases to be executed
			NUM_ATC = getNumATC();
			// Create the testData matrix
			testData = new String [NUM_ATC+1][MAX_COL];
			// Load test data into the matrix
			cargaTestData(ATC_Name);
//			for(row=0;row<NUM_ATC+1;row++) {
//				for(col=0;col<MAX_COL;col++) {
//					System.out.print(testData[row][col]+" | ");
//				}
//				System.out.println();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method responsible for creating and initializing the testData file and loading it into memory.
	 * @param SheetName Name of the file sheet
	 * @throws Exception Throws an exception in case of file access failure
	 */
	private void crearTestData(String SheetName) throws Exception {
		try {
			logger.info("Starting the creation of the TestData file");
			// Create input and output file
			//System.out.println(Config.TESTDATA_PATH + Config.TESTDATA_FILENAME + ".xlsx");
			FileInputStream ExcelFile = new FileInputStream(Config.TESTDATA_PATH + Config.TESTDATA_FILENAME);
			
			// Create workbook
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			// Create worksheet
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			logger.info("TestData file created successfully");
		} catch (Exception e){
			throw (e);
		}
	}
	/**
	 * Method responsible for obtaining the number of rows in the file
	 * @return Number of rows in the file
	 */
	private int getNumRows() {
		row=0;
		// Loop to iterate through all rows until the value is null
		while(ExcelWSheet.getRow(row) != null) {
			row++;
		}
		// Return maximum value of rows found
		return row--; 
	}
	/**
	 * Method responsible for obtaining the number of columns in the file
	 * @return Number of columns in the file
	 */
	private int getNumCols() {
		row=0;
		col=0;
		// Loop to iterate through all columns until the value is null
		while(ExcelWSheet.getRow(row).getCell(col) != null) {
			col++;
		}
		// Return maximum value of columns found
		return col; 
	}
	/**
	 * Method responsible for obtaining the number of executions for the test case
	 * @return Number of test cases
	 */
	private int getNumATC() {
		row=0;
		col=0;
		int numAtc=0;
		// Loop to iterate through all rows until the value is null
		while(ExcelWSheet.getRow(row) != null) {
			// Check if the record belongs to the active test case and if it should be executed
			if(ExcelWSheet.getRow(row).getCell(col).toString().equals(ATC_Name) && 
					ExcelWSheet.getRow(row).getCell(1).toString().equalsIgnoreCase("SI")) {
				// Increase the test case count
				numAtc++;
			}
			row++;
		}
		// Return the number of test cases found
		return numAtc; 
	}
	/**
	 * Method responsible for loading test data headers into the testData matrix
	 */
	private void loadHeaders() {
		// Loop to iterate through all header columns
		for (col = 0; col < MAX_COL; col++) {
			// Load header
			testData[0][col]=ExcelWSheet.getRow(0).getCell(col).toString();
		}
	}
	/**
	 * Method responsible for loading test data into the testData matrix
	 * @param ATC_Name Case name
	 */
	private void loadData(String ATC_Name) {
		row=0;
		col=0;
		int rowTestData=1;
		// Loop to iterate through all header rows
		for (row = 0; row < MAX_ROW; row++) {
			// If the cell value matches the test case name and if it should be executed
			if(ExcelWSheet.getRow(row).getCell(col).toString().equals(ATC_Name) && 
					ExcelWSheet.getRow(row).getCell(1).toString().equalsIgnoreCase("SI")) {
					// Loop to iterate through all header columns
					for (col = 0; col < MAX_COL; col++) {
						// Validate that the cell value is not Null
						if(ExcelWSheet.getRow(row).getCell(col)!= null) {
							// Load the data into the testData matrix
							testData[rowTestData][col]=ExcelWSheet.getRow(row).getCell(col).toString();
						}
					}
					// Increment the testData matrix row by 1
					rowTestData++;
			}
			// Reset the col variable to 0
			col=0;
		}
	}
	/**
	 * Method responsible for loading headers and test data into the testData matrix
	 * @param ATC_Name Test case name
	 */ 
	private void cargaTestData(String ATC_Name) {
		// Call header loading method
		loadHeaders();
		// Call data loading method
		loadData(ATC_Name);
	}
	/**
	 * Method responsible for obtaining a specific test data item, specifying the data header
	 * @param dataName Test case name
	 * @param numATC Test case number being worked on
	 * @return Returns the specific test data or "NotDataFound" if the element is not found
	 */ 
	public String getData(String dataName,int numATC) {
		String data="";
		// Loop to iterate through all header columns
			for (col = 0; col < MAX_COL; col++) {
				// If header data matches dataName, perform action
				if(testData[0][col].equals(dataName)) {
					// Obtain the data
					data=testData[numATC][col];
				}
			}		
			// If data is not null, return the data
			if(data!=null) {
				return data;
			}else {
			// If data is null, return NotDataFound
				return "NotDataFound";
			}
	}
	/**
	 * Method responsible for obtaining the number of test cases to be executed
	 * @return Number of test cases to be executed
	 */
	public int getNUM_ATC() {
		return NUM_ATC;
	}
	/*
	/**
	 * Method responsible for writing a specific piece of data, indicating the cell coordinates
	 * @param dato Data to be written in the cell
	 * @param row Row number
	 * @param col Column number
	 */
	/*
	private void setCellData(String dato,  int row, int col){
		try{
			// Create a row
			XSSFRow Row = ExcelWSheet.getRow(row);
			// Add a column to the cell
			XSSFCell cell = Row.getCell(col); 
			// Validate if the cell already exists
			if (cell == null) {
				// Create the cell
				cell = Row.createCell(col);
				// Write to the cell
				cell.setCellValue(dato);
			} else {
				// If it exists, write to the cell
				cell.setCellValue(dato);
			}
			// Create the file for writing
			FileOutputStream fileOut = new FileOutputStream(Config.TESTDATA_PATH + Config.TESTDATA_FILENAME);
			// Write the file
			ExcelWBook.write(fileOut);
			// Confirm changes made
			fileOut.flush();
			// Close the file
			fileOut.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
}