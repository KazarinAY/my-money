package kazarin.my_money.model;

import kazarin.my_money.db.Dao;
import kazarin.my_money.db.DaoFactory;
import kazarin.my_money.db.DaoException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Properties;
import java.io.FileReader;

/*
 * Depends on:  model/Entry.java
 *              model/Env.java
 *              db/Dao.java
 *              db.DaoFactory.java
 *              db.DaoException.java
 */
public final class Accounting {

    private String name;

    private Dao<Entry> dao;

    private List<Entry> entryList;

    private String propertyFile;

    private Properties properties;

    private Accounting() {
        entryList = new ArrayList<Entry>();        
        properties = new Properties();
    }
 
    public Accounting(String name) {
        this();
        try {
            this.name = name;
            propertyFile = Env.FULL_DATA_DIR + Env.SEP + name + Env.EXTENSION_PROPERTIES;
            properties.load(new FileReader(propertyFile));            
            dao = DaoFactory.getDao(properties);
            entryList = dao.getAll();
        } catch (IOException e) {
            ModelLogger.warning("Failed to load properties file.", e);
            throw new ModelException("Accounting Constructor failed.", e);
        } catch (DaoException de) {
            ModelLogger.warning("Failed to getDao");
            throw new ModelException("Accounting Constructor failed.", de);
        }
    } 

    public Accounting(String user, String password, String host,
                        String dbName, String db) {
        this();
        this.name = dbName;
        propertyFile = Env.FULL_DATA_DIR + Env.SEP + name + Env.EXTENSION_PROPERTIES;
        setProperies(user, password, host, dbName, db);             
        try {
            Dao<Entry> dao = DaoFactory.getDao(properties);
            dao.createDB(dbName);
            createProperiesFile();
            addNewDbToProperties();
            entryList = dao.getAll();
        } catch (DaoException de) {
            Path pathToFile = Paths.get(propertyFile);
            try {
                Files.delete(pathToFile);
            } catch (IOException ioe) {
                ModelLogger.warning(ioe.getMessage());
            }
            ModelLogger.warning("Failed to createNewAccounting");
            throw new ModelException("Faild to create New Accounting .", de);
        }
    }

    public String getName() {
        return name;
    }

    /**     
     * Adds an entry to the entryList.
     * @param entry      
     * @throws ModelException 
     */
    public void add(Entry entry) { 
        try{
            dao.add(entry);
            setList(dao.getAll());
        } catch (DaoException de) {
            throw new ModelException("Failed to add entry!", de);
        }
        
    }  

    /**
     * Deletes an operation from the operations entryList.
     * @param entry
     * @throws ModelException 
     */
    public void delete(Entry entry) {
        try{
            dao.delete(entry);
        setList(dao.getAll());
        } catch (DaoException de) {
            throw new ModelException("Failed to delete entry!", de);
        }
    }

    /**
     * Changes an operation in operations entryList.
     * @param entry
     * @throws ModelException
     */
    public void change(Entry entry) { 
       // dao.update(operationToChange);
    }

    /**
     * Gets entryList size.
     */
    public int size(){
        return entryList.size();
    }

    /**
     * @return entryList
     */
    public  List<Entry> getList() {
        return entryList;
    }

    /**
     * @param entryList  Sets up the entryList.
     */
    public  void setList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public int hashCode() {
        int code = 0;
        if (entryList != null) {
            code = entryList.hashCode();
        }
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == null) return false;

        if (obj == null || getClass() != obj.getClass()) return false;

        Accounting acc = (Accounting) obj;

        if (entryList != null ? !entryList.equals(acc.getList())
                         : acc.getList() != null) return false;

        return true;
    }

        /**
     * Saves entryList to txt file.
     * @param fileName      path to file.
     */
    public void saveToTxt(String fileName) {
        Path file = Paths.get(fileName);
        if (Files.exists(file)) {
            try (BufferedWriter bw = new BufferedWriter(
                                                    new FileWriter(fileName))) {                
                ;
                for (Entry entry : entryList) {
                    bw.write(entry.toCommandString());
                    bw.newLine();
                }
                ModelLogger.info("Saved");

            } catch (IOException e) {
                ModelLogger.warning("Failed to save data to file "
                                                                    + fileName);
            }
        } else {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                ModelLogger.warning("Failed to create "
                                                        + fileName + " file!");
            }
            saveToTxt(fileName);             // recursive calling
        }
    }

    /**
     * Loads entryList from txt file.
     * @param fileName      path to file.
     */
    public void loadFromTxt(String fileName) {
        Path file = Paths.get(fileName);
        if (!Files.exists(file)) {
            ModelLogger.warning("LOAD" + fileName + " file not found.");
            return;
        }
        
        try (BufferedReader reader =
                    new BufferedReader(new FileReader(fileName))) {

            String readedLine = "";
            while (reader.ready()) {
                readedLine = reader.readLine();                
                //add(readedLine); //TODO add(entry)
            }
        } catch (IOException e) {
                ModelLogger.warning("Failed to load data from"
                                                        + fileName + " file!");
        }
    }

    private void setProperies(String user, String password,
                                    String host, String dbName, String db) {
        ModelLogger.info("setProperies");
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("dbName", dbName);
        if (db.equals("MySQL")){
            properties.setProperty("DB type", "MySQL");
            properties.setProperty("driver", "com.mysql.jdbc.Driver");
            properties.setProperty("url", "jdbc:mysql:" + "//" + host
                                                        + Env.SEP + dbName);
        } else if (db.equals("HSQL")) {
            properties.setProperty("DB type", "HSQL");
            properties.setProperty("driver", "org.hsqldb.jdbc.JDBCDriver");
            //TODO if host == file...
            properties.setProperty("url", "jdbc:hsqldb:" + host 
                                                        + Env.SEP + dbName);            
        } else {
            ModelLogger.info("setProperies: unknown DB");

        }
    }

    private void createProperiesFile() {
        Path path = Paths.get(propertyFile);
        try {        
            Files.createFile(path);
            ModelLogger.info("File " + propertyFile + " created.");
        }  catch (IOException e) {
            ModelLogger.warning("Failed to create file!\t" + propertyFile);
            throw new ModelException("Failed to create file!", e);
        }
    }

    private void addNewDbToProperties() {
        String dbList = properties.getProperty(Env.DB_LIST_PROPERTY);
        if (dbList == null) {
            dbList = name;
        } else {
            dbList += "," + name;
        }
        properties.setProperty(Env.DB_LIST_PROPERTY, dbList);
        try {
            properties.store(new FileWriter(propertyFile.toString()), "comment");
        } catch (IOException e) {
                ModelLogger.warning("Failed to addNewDbToProperties.");
                throw new ModelException("Failed to addNewDbToProperties.", e);
        }
    }

}
