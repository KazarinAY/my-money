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
 
    public Accounting(String dbName) {        
        entryList = new ArrayList<>();        
        String propertyFile = Env.FULL_DATA_DIR + Env.SEP + dbName + Env.EXTENSION_PROPERTIES;
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertyFile));
            name = properties.getProperty(Env.DB_NAME_PROPERTY);
            dao = DaoFactory.getDao(properties);
            entryList = dao.getAll();
        } catch (IOException e) {
            ModelLogger.warning("Failed to load properties file.", e);
            throw new ModelException("Accounting Constructor faild.", e);
        } catch (DaoException de) {
            ModelLogger.warning("Failed to getDao");
            throw new ModelException("Accounting Constructor faild.", de);
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

}
