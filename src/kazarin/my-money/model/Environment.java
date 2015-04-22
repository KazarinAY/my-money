package kazarin.my_money.model;

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
    private Logger logger;
    
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

        String directory = System.getProperty("user.home") + "/mymoney";
        this.propertyDir = Paths.get(directory);
        this.propertyFile = Paths.get(directory + "/mymoney.properties");
        logger.info("directory = " + directory + "\n\t" + "propertyFile = " + this.propertyFile);

        if (!Files.exists(propertyFile)) {
            if (!Files.exists(this.propertyDir)) {
                createDirectories();
            }            
            createProperiesFile();
            properties = new Properties();
            properties.setProperty("txtDataFile", directory + "/data.txt");
            try {
                properties.store(new FileWriter(propertyFile.toString()),
                                                                    "comment");
            } catch (IOException e) {
                logger.warning("ERROR: failed to store properties file.");
            }
        }
        try {
            properties = new Properties();
            properties.load(new FileReader(propertyFile.toString()));
        } catch (IOException e) {
                logger.warning("ERROR: failed to load properties file.");
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
     * @return String   Returns path to text data file.
     */
    public String getTxtDataFile(){
        return properties.getProperty("txtDataFile");
    }

    /**
     * Saves list to txt file.
     * @param fileName      path to file.
     */
    public void saveToTxt(String fileName) {
        Path file = Paths.get(fileName);
        if (Files.exists(file)) {
            try (BufferedWriter bw = new BufferedWriter(
                                                    new FileWriter(fileName))) {

                Operations operations = Operations.getInstance();
                List<Operation> list = operations.getList();
                for (Operation op : list) {
                    bw.write(op.toCommandString());
                    bw.newLine();
                }
                logger.info("Saved");

            } catch (IOException e) {
                logger.warning("ERROR: failed to save data to file "
                                                                    + fileName);
            }
        } else {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                logger.warning("ERROR: failed to create "
                                                        + fileName + " file!");
            }
            saveToTxt(fileName);             // recursive calling
        }

    }

    /**
     * Loads list from txt file.
     * @param fileName      path to file.
     */
    public void loadFromTxt(String fileName) {
        Path file = Paths.get(fileName);
        if (!Files.exists(file)) {
            logger.warning("LOAD" + fileName + " file not found.");
            return;
        }
        
        try (BufferedReader reader =
                    new BufferedReader(new FileReader(fileName))) {

            Operations operations = Operations.getInstance();
            String readedLine = "";
            while (reader.ready()) {
                readedLine = reader.readLine();                
                operations.add(readedLine);
            }            

        } catch (WrongCommandException wce) {
            logger.warning("ERROR: failed to load data from"
                                                        + fileName + " file!");
            logger.warning("");
        } catch (IOException e) {
                logger.warning("ERROR: failed to load data from"
                                                        + fileName + " file!");
            }
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
                || properties.getProperty("url") == null
                || properties.getProperty("driver") == null) {
            logger.info("ISREADY: environment isn't ready");
            return false;
        }
        logger.info("ISREADY: environment is ready");
        return true;
    }

    public void prepare(String user, String password, String url, String db) {
        //user = "guest";
        //password = "12345678";   
        //url = "jdbc:mysql://localhost/MYMONEY";
        //driver = "com.mysql.jdbc.Driver";
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        if (db.equals("MySQL")){
             properties.setProperty("driver", "com.mysql.jdbc.Driver");
             properties.setProperty("url", "jdbc:mysql:" + url);
        } else if (db.equals("HSQL")) {
            properties.setProperty("driver", "org.hsqldb.jdbc.JDBCDriver");
            properties.setProperty("url", "jdbc:hsqldb:file:" + url);            
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
    
}
