package config;

import java.io.File;

/**
 * Central repository of framework-wide path constants.
 * @author Osiris Montiel Campos
 * @version 2025-05-22
 */
public class Config {

    private Config() {
        throw new IllegalStateException("Config is a utility class and must not be instantiated");
    }

    /** 
     * Absolute path to the root directory of the Maven project. 
     */
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    /**
     * Absolute path to the current OS user's home directory.
     */
    private static final String USER_HOME = System.getProperty("user.home");

    /** 
     * Path to the {@code properties/} directory containing object repository files. 
     */
    public static final String PROPERTIES_PATH = PROJECT_DIR
            + File.separator + "properties" + File.separator;

    /** 
     * Path to the {@code reporte/} directory where evidence reports are saved. 
     */
    public static final String REPORTER_PATH = PROJECT_DIR
            + File.separator + "reporte" + File.separator;

    /** 
     * Path to the {@code testData/} directory containing test data files. 
     */
    public static final String TESTDATA_PATH = PROJECT_DIR
            + File.separator + "testData" + File.separator;

    /**
     * Name of the Excel file that holds test input data.
     */
    public static final String TESTDATA_FILENAME = "TestData.xlsx";

    /**
     * Path to the directory that holds object repository files.
     */
    public static final String OBJECT_REPOSITORY_PATH = PROJECT_DIR
            + File.separator + "properties" + File.separator;

    /**
     * Default download directory of the current OS user.
     */
    public static final String DOWNLOAD_PATH = USER_HOME
            + File.separator + "Downloads" + File.separator;

    /** 
     * Path to the dataOutput directory for files produced by test cases. 
     */
    public static final String OUTPUT_PATH = PROJECT_DIR
            + File.separator + "dataOutput" + File.separator;

    /** 
     * Path to the {@code dataInput/} directory for files consumed by test cases. 
     */
    public static final String INPUT_PATH = PROJECT_DIR
            + File.separator + "dataInput" + File.separator;
}
