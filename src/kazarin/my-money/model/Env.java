package kazarin.my_money.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Properties;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JFrame;

/**
 * Env = Environment
 * A "static" class
 */
public final class Env {
	private Env() {/*NON*/}
	
	public static final String HOME_PATH;
    public static final String FULL_DATA_DIR;
    public static final String DATA_DIR = "mymoney";
    public static final String PROP_ACCS_FILE = "accountings.properties";
    public static final String LOCALHOST = "localhost";
    public static final String SEP; //separator
    public static final String EXTENSION_PROPERTIES = ".properties";
    public static final SimpleDateFormat DATE_FORMAT;

    public static final String CONNECT_TO_EXISTING = "Connect to existing";
	public static final String NEW_ACCOUNTING = "new accounting";
	public static final int MAX_ACCAUNTINGS_NUMBET = 5;
	public static final boolean NEW = true;
	public static final boolean CONNECT = false;
	public static final String NEW_ENTRY = "New entry";
    public static final String CREATE = "Create";
    public static final String LIST_OF_ACCOUNTING = "List of accounting:";
    public static final String THE_NEW_ACCOUNTING = "The new accounting:";
	
	public static final Properties ACCOUNTINGS_PROPERIES;
	public static final String DB_LIST_PROPERTY = "dbNames";
	public static final String DB_NAME_PROPERTY = "dbName"; 

	private static final Path PROP_FILE_PATH;
	private static final Path DATA_PATH;

    private static JFrame frame;
    
    static {
        HOME_PATH = System.getProperty("user.home");
        SEP = System.getProperty("file.separator");
        FULL_DATA_DIR = HOME_PATH + SEP + DATA_DIR;
        PROP_FILE_PATH = Paths.get(FULL_DATA_DIR + SEP + PROP_ACCS_FILE);
        DATA_PATH = Paths.get(FULL_DATA_DIR);
        ACCOUNTINGS_PROPERIES = new Properties();
        DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    }

    public static final void init(JFrame fr) {
    	frame = fr;
        if (!Files.exists(PROP_FILE_PATH)) {
    		ModelLogger.info("Property File doesn't exist.");
    		createPropFile();
    	} else {
    		try {                
	            ACCOUNTINGS_PROPERIES.load(new FileReader(PROP_FILE_PATH.toString()));
	        } catch (IOException e) {
	                ModelLogger.warning("Failed to load properties file.");
	                throw new ModelException(e.getMessage(), e);
        	}	         
    	}
    } 

    public static JFrame getFrame() {
        return frame;
    }

    public static final String[] getAccountings() {
    	String property = ACCOUNTINGS_PROPERIES.getProperty(DB_LIST_PROPERTY);
    	ModelLogger.info("property " + DB_LIST_PROPERTY + " = " + property);
    	if (property != null) {
    		return property.split(",");
    	}
    	return new String[0];
    }
    
    private static final void createPropFile() {
        try {
            if (!Files.exists(DATA_PATH)) {
                Files.createDirectories(DATA_PATH);
                ModelLogger.info(DATA_PATH + "created");
            }
            Files.createFile(PROP_FILE_PATH);
            ModelLogger.info("File " + PROP_FILE_PATH + " created.");
        }  catch (IOException e) {
            ModelLogger.warning("Failed to create file!\t" + PROP_FILE_PATH);
            throw new ModelException(e.getMessage());
        }
    }
}
