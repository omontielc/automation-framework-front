package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;

import config.Config;


/**
 * This class contains the methods responsible for actions on objects 
 * @author Osiris Montiel Campos
 * @version 2025-06-26
 */
public class ObjectRepository {
	/**
	 * objRepository variable used as an instance of the object repository
	 */
	Properties objRepository;
	// Logger variable used to generate logs
	org.apache.logging.log4j.Logger logger=LogManager.getLogger(ObjectRepository.class);
	/**
	 * Class constructor responsible for initializing the object repository
	 * @param sistema Variable containing the name of the system being worked on
	 */
	public ObjectRepository (String sistema) {
		try {
			// Create the file for object creation
			File src = new File(Config.OBJECT_REPOSITORY_PATH + "ObjectRepository"+ sistema +".properties");
			FileInputStream objFile = new FileInputStream(src);
			// Generate the object that will contain the object repository
			objRepository = new Properties();
			// Load the object repository
			objRepository.load(objFile);
			logger.info("Repository file loaded successfully");
		}catch(Exception e) {
			objRepository = new Properties();
			logger.info("Could not load the repository file");
		}
	}
	/**
	 * Method responsible for returning the objRepository instance, which contains the system's object repository 
	 * @return objRepository Object containing the loaded object repository 
	 */ 
	public Properties getObjectRepository(){	
		return objRepository;
	}
	
}