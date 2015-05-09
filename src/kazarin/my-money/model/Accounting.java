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
 *              db/Dao.java
 *              db.DaoFactory.java
 *              db.DaoException.java
 */
public final class Accounting {

    private static SimpleDateFormat dateFormat;
    private static final String HOME_PATH;
    private static final String PROGRAMM_DATA_PATH = "mymoney";
    private static final String SEPARATOR;
    private static final String EXTENSION_PROPERTIES = ".properties";
    private static final String DB_NAME_PROPERTY = "dbName";
    static {      
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        HOME_PATH = System.getProperty("user.home");
        SEPARATOR = System.getProperty("file.separator");
    }

    private String name;

    private Dao<Entry> dao;

    private List<Entry> entryList;
 
    public Accounting(String dbName) throws ModelException {
        
        entryList = new ArrayList<>();
        String directory = HOME_PATH + SEPARATOR + PROGRAMM_DATA_PATH;
        String propertyFile = directory + SEPARATOR + dbName + EXTENSION_PROPERTIES;
        Properties properties = new Properties();
        try {
                properties.load(new FileReader(propertyFile));
        } catch (IOException e) {
            ModelLogger.warning("Failed to load properties file.", e);
            throw new ModelException("Accounting Constructor faild.\t"
                                                        + e.getMessage());
        }
        name = properties.getProperty(DB_NAME_PROPERTY);
        try {
            dao = DaoFactory.getDao(properties);
            entryList = dao.getAll();
        } catch (DaoException de) {
            ModelLogger.warning("Failed to getDao");
            throw new ModelException("Accounting Constructor faild.\t"
                                                        + de.getMessage());
        }
    }  

    public String getName() {
        return name;
    }

    /**
     * Prints operations statistic.
     * For console.
     */
    public void printStatistic() {
        if (entryList.size() == 0) {
            System.out.println("No entrys");
            return;
        }
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal consumption = BigDecimal.ZERO;
        for (Entry entry : entryList) {
            if (entry.getSum().compareTo(BigDecimal.ZERO) == -1) {
                consumption = consumption.add(entry.getSum());
            } else if (entry.getSum().compareTo(BigDecimal.ZERO) == 1) {
                income = income.add(entry.getSum());
            }
        }
        BigDecimal balance = income.add(consumption);
        System.out.println("Total income = " + income + ", total consumption = "
                                             + consumption);
        System.out.println("Balance = " + balance + ", total operations = "
                                        + entryList.size());
    }
    
    /**     
     * Adds an entry to the entryList.
     * @param entry      
     * @throws ModelException 
     */
    public void add(Entry entry) throws ModelException { 
        try{
            dao.add(entry);
            setList(dao.getAll());
        } catch (DaoException de) {
            throw new ModelException(de.getMessage());
        }
        
    }  

    /**
     * Deletes an operation from the operations entryList.
     * @param entry
     * @throws ModelException 
     */
    public void delete(Entry entry) throws ModelException {
        try{
            dao.delete(entry);
        setList(dao.getAll());
        } catch (DaoException de) {
            throw new ModelException(de.getMessage());
        }
    }

    /**
     * Changes an operation in operations entryList.
     * @param entry
     * @throws ModelException
     */
    public void change(Entry entry) throws ModelException { 
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
