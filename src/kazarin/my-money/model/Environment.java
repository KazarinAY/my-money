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
import java.util.ArrayList;
import java.util.Properties;
import java.util.Arrays;

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

    private List<Operations> accountings;
    
    /**
     * Properties.
     */
    private Properties properties;

    private String directory;

    /**
     * Directory.
     */
    private Path propertyDir;

    /**
     * File.
     */
    private Path propertyFile;

    /**
     * An instance of Environment.
     */
    private static Environment instance;    

    /**
     * Constructor.
     * Creates .properties file and directories if they don't exist.
     */
    private Environment() {        

        directory = System.getProperty("user.home") + "/mymoney";
        this.propertyDir = Paths.get(directory);
        this.propertyFile = Paths.get(directory + "/mymoney.properties");
        logger.info("directory = " + directory + "\n\t" + "propertyFile = " + this.propertyFile);
        accountings = new ArrayList<Operations>();
        if (!Files.exists(propertyFile)) {
            if (!Files.exists(this.propertyDir)) {
                createDirectories();
            }            
            createProperiesFile(propertyFile);
            try {
                properties = new Properties();
                properties.store(new FileWriter(propertyFile.toString()),
                                                                    "comment");
            } catch (IOException e) {
                    logger.warning("ERROR: failed to store properties file.");
            }
        } else {
            try {
                properties = new Properties();
                properties.load(new FileReader(propertyFile.toString()));
                String accountingsList = properties.getProperty("dbNames");
                    logger.info("accountingsList: " + accountingsList);
                String[] opsArray = accountingsList.split(",");
                for (String ops : opsArray) {
                    accountings.add(new Operations(ops.trim()));
                }
                
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
    private void createProperiesFile(Path path) {
        try {
            Files.createFile(path);
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
        if ( properties.getProperty("dbNames") == null) {                
            logger.info("ISREADY: environment isn't ready");
            return false;
        }
        logger.info("ISREADY: environment is ready");
        return true;
    }

    public void createNewAccounting(String user, String password, String host,
                                                    String dbName, String db){
        if (db.equals("MySQL")) {
            logger.info("createNewAccounting: MySQL");

        } else if (db.equals("HSQL")) {
            logger.info("createNewAccounting: HSQL");
        } else {
            logger.info("createNewAccounting: unknown DB");
        }

    }

    public void connectToExistingAccounting(String user, String password,
                                    String host, String dbName, String db) {
            logger.info("connectToExistingAccounting");
            Path pathToFile = Paths.get(directory + "/" + dbName + ".properties");
            createProperiesFile(pathToFile);
            Properties newProps = new Properties();
                newProps.setProperty("user", user);
                newProps.setProperty("password", password);
                newProps.setProperty("dbName", dbName);
                if (db.equals("MySQL")){
                    newProps.setProperty("DB type", "MySQL");
                    newProps.setProperty("driver", "com.mysql.jdbc.Driver");
                    newProps.setProperty("url", "jdbc:mysql:" + "//" + host
                                                                + "/" + dbName);
                } else if (db.equals("HSQL")) {
                    newProps.setProperty("DB type", "HSQL");
                    newProps.setProperty("driver", "org.hsqldb.jdbc.JDBCDriver");
                    newProps.setProperty("url", "jdbc:mysql:" + "//" + host
                                                                + "/" + dbName);            
                } else {
                    logger.info("connectToExistingAccounting: unknown DB");
                }
            
            try {
                newProps.store(new FileWriter(pathToFile.toString()), "comment");
            }  catch (IOException e) {
                logger.warning("connectToExistingAccounting: "
                                            + "failed to stote properties file.");
            }            
            accountings.add(new Operations(dbName));
            String dbNames = properties.getProperty("dbNames");
            if (dbNames == null) {
                dbNames = dbName;
            } else {
                dbNames += "," + dbName;
            }
            properties.setProperty("dbNames", dbNames);
            try {
                properties.store(new FileWriter(propertyFile.toString()),
                                                                    "comment");
            } catch (IOException e) {
                    logger.warning("ERROR: failed to store2 properties file.");
            }

    } 
/*
    public void prepare(String user, String password, String host, 
                                                    String dbName, String db) {
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
    */
    /*
    public DBTypes getDBType() {
        if (properties.getProperty("DB type").equals("MySQL")) return DBTypes.MYSQL;
        if (properties.getProperty("DB type").equals("HSQL")) return DBTypes.HSQL;
        return null;
    }
*/
    public List<Operations> getAccountings() {
        return accountings;
    }

    public Operations getOperationsByName(String dbName) {
        for (Operations ops : accountings) {
            if (ops.getName().equals(dbName)) return ops;
        }
        logger.warning("failed to getOperationsByName");
        return null;
    }
}
