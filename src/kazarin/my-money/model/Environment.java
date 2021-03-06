package kazarin.my_money.model;

import kazarin.my_money.db.Dao;
import kazarin.my_money.db.DaoFactory;
import kazarin.my_money.db.DaoException;

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

    private List<Accounting> accountings;
    
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
    private Environment() throws ModelException {        

        propertyFile = Paths.get(directory + "/accountings.properties");
        ModelLogger.info("directory = " + directory + "\n\t" + "propertyFile = " + this.propertyFile);
        accountings = new ArrayList<Accounting>();
        properties = new Properties();
        if (!Files.exists(propertyFile)) {        
            ModelLogger.warning("Property File doesn't exist.");
            //throw new ModelException("Property File doesn't exist.");            
        } else {
            try {                
                properties.load(new FileReader(propertyFile.toString()));
                String accountingsList = properties.getProperty(DB_LIST_PROPERTY);
                    ModelLogger.info("accountingsList: " + accountingsList);
                String[] opsArray = accountingsList.split(",");
                for (String ops : opsArray) {
                    accountings.add(new Accounting(ops.trim()));
                }
                
            } catch (IOException e) {
                    ModelLogger.warning("Failed to load properties file.");
            } 
        }
    }

    /**
     * @return instanse of Environment
     */
    public static Environment getInstance() throws ModelException {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

   
    
    /**
     * Creates properties directory.
     */    
    private void createDirectories() {
        try {
            Files.createDirectories(propertyDir);
            ModelLogger.info(propertyDir + "created");
        } catch (IOException e) {
            ModelLogger.warning("Failed to create property directories!");
        }
    }

    public Properties getPropertiesDB() {        
        return properties;
    }

    public boolean isReady() {
        if ( properties.getProperty("dbNames") == null) {                
            ModelLogger.info("ISREADY: environment isn't ready");
            return false;
        }
        ModelLogger.info("ISREADY: environment is ready");
        return true;
    }

    public void createNewAccounting(String user, String password, String host,
                                String dbName, String db) throws ModelException {
        ModelLogger.info("Start creating...");
        Properties pr = createProperiesDB(user, password, host, dbName, db);             
        try {
            Dao<Entry> dao = DaoFactory.getDao(pr);
            dao.createDB(dbName);
            if (!Files.exists(propertyFile)) {
                if (!Files.exists(propertyDir)) {
                    createDirectories();
                }
                createProperiesFile(propertyFile);
                addNewDbToProperties(dbName); 
            }
        } catch (DaoException de) {
            Path pathToFile = Paths.get(directory + "/" + dbName + ".properties");
            try {
                Files.delete(pathToFile);
            } catch (IOException ioe) {
                ModelLogger.warning(ioe.getMessage());
            }
            
            ModelLogger.warning("Failed to createNewAccounting");
            throw new ModelException("createNewAccounting faild.\t"
                                                        + de.getMessage());


        }     

    }

    public void connectToExistingAccounting(String user, String password,
                    String host, String dbName, String db) throws ModelException {
            createProperiesDB(user, password, host, dbName, db);
            accountings.add(new Accounting(dbName));
            addNewDbToProperties(dbName);
    } 

    private void addNewDbToProperties(String newDbName) {
        String dbList = properties.getProperty(DB_LIST_PROPERTY);
            if (dbList == null) {
                dbList = newDbName;
            } else {
                dbList += "," + newDbName;
            }
            properties.setProperty(DB_LIST_PROPERTY, dbList);
            try {
                properties.store(new FileWriter(propertyFile.toString()),
                                                                    "comment");
            } catch (IOException e) {
                    ModelLogger.warning("Failed to addNewDbToProperties.");
            }
    }

    private Properties createProperiesDB(String user, String password,
                                    String host, String dbName, String db) {
        ModelLogger.info("createProperiesDB");
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
                newProps.setProperty("url", "jdbc:hsqldb:" + host               //TODO if host == file...
                                                            + "/" + dbName);            
            } else {
                ModelLogger.info("createProperiesDB: unknown DB");
            }
        
        try {
            newProps.store(new FileWriter(pathToFile.toString()), "comment");
        }  catch (IOException e) {
            ModelLogger.warning("createProperiesDB: "
                                        + "failed to store properties file.");
            ModelLogger.warning(e.getMessage());
        } 
        return newProps;
    }
    public List<Accounting> getAccountings() {
        return accountings;
    }

    public Accounting getOperationsByName(String dbName) {
        for (Accounting acc : accountings) {
            if (acc.getName().equals(dbName)) return acc;
        }
        ModelLogger.warning("failed to getOperationsByName");
        return null;
    }
}
