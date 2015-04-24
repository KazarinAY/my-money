package kazarin.my_money.model;

import kazarin.my_money.db.DBTypes;

import java.util.logging.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

/**
 * Environment.
 * Singletone.
 */
public final class Environment {
    private static Logger logger;
    
    static {
        try {
            logger = Logger.getLogger(Environment.class.getName());
            FileHandler fh = new FileHandler("/tmp/Environment.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }

    /**
     * Properties.
     */
    private Properties properties;

    /**
     * Directory.
     */
    private Path propertyDir;

    /**
     * File.
     */
    private Path propertyFile;

    /**
     * An instance of Operations.
     */
    private static Environment instance;    

    /**
     * Constructor.
     * Creates .properties file and directories if they don't exist.
     */
    private Environment() {        

        String directory = System.getProperty("user.home") + "/mymoney";
        this.propertyDir = Paths.get(directory);
        this.propertyFile = Paths.get(directory + "/mymoney.properties");
        logger.info("directory = " + directory + "\n\t" + "propertyFile = " + this.propertyFile);

        if (!Files.exists(propertyFile)) {
            if (!Files.exists(this.propertyDir)) {
                createDirectories();
            }            
            createProperiesFile();
            try {
                properties = new Properties();
                properties.store(new FileWriter(propertyFile.toString()),
                                                                        "comment");
            } catch (IOException e) {
                    logger.warning("ERROR: failed to load properties file.");
            }
        } else {
            try {
                properties = new Properties();
                properties.load(new FileReader(propertyFile.toString()));
            } catch (IOException e) {
                    logger.warning("ERROR: failed to load properties file.");
            }
        }
    }

    /**
     * @return instanse of Environment
     */
    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    /**
     * Creates properties file.
     */    
    private void createProperiesFile() {
        try {
            Files.createFile(propertyFile);
        } catch (IOException e) {
            logger.warning("ERROR: failed to create data file!");
        }
    }
    
    /**
     * Creates properties directory.
     */    
    private void createDirectories() {
        try {
            Files.createDirectories(propertyDir);
            logger.info(propertyDir + "created");
        } catch (IOException e) {
            logger.warning("ERROR: failed to create property directories!");
        }
    }

    public Properties getPropertiesDB() {        
        return properties;
    }

    public boolean isReady() {
        if (       properties.getProperty("user") == null
                || properties.getProperty("password") == null
                || properties.getProperty("DB type") == null
                || properties.getProperty("url") == null
                || properties.getProperty("driver") == null) {
            logger.info("ISREADY: environment isn't ready");
            return false;
        }
        logger.info("ISREADY: environment is ready");
        return true;
    }

    public void prepare(String user, String password, String host, String dbName, String db) {
        //user = "guest";
        //password = "12345678";   
        //url = "jdbc:mysql://localhost/MYMONEY";
        //driver = "com.mysql.jdbc.Driver";
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        if (db.equals("MySQL")){
            properties.setProperty("DB type", "MySQL");
            properties.setProperty("driver", "com.mysql.jdbc.Driver");
            properties.setProperty("url", "jdbc:mysql:" + "//" + host + "/" + dbName);
        } else if (db.equals("HSQL")) {
            properties.setProperty("DB type", "HSQL");
            properties.setProperty("driver", "org.hsqldb.jdbc.JDBCDriver");
            properties.setProperty("url", "jdbc:mysql:" + "//" + host + "/" + dbName);            
        } else {
            logger.info("PREPARE: unknown DB");
        }
       
        try {
                properties.store(new FileWriter(propertyFile.toString()),
                                                                    "comment");
        } catch (IOException e) {
           logger.warning("ERROR: failed to store properties file.");
        }
    }
    
    public DBTypes getDBType() {
        if (properties.getProperty("DB type").equals("MySQL")) return DBTypes.MYSQL;
        if (properties.getProperty("DB type").equals("HSQL")) return DBTypes.HSQL;
        return null;
    }
}
