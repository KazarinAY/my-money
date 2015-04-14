package kazarin.my_money.model;

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
        propertyDir = Paths.get(directory);
        propertyFile = Paths.get(directory + "/mymoney.properties");

        if (!Files.exists(propertyFile)) {
            if (!Files.exists(propertyDir)) {
                createDirectories();
            }            
            createProperiesFile();
            properties = new Properties();
            properties.setProperty("txtDataFile", directory + "/data.txt");
            try {
                properties.store(new FileWriter(propertyFile.toString()),
                                                                    "comment");
            } catch (IOException e) {
                System.err.println("ERROR: failed to store properties file.");
            }
        }
        try {
            properties = new Properties();
            properties.load(new FileReader(propertyFile.toString()));
        } catch (IOException e) {
                System.err.println("ERROR: failed to load properties file.");
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
                System.out.println("Saved");

            } catch (IOException e) {
                System.err.println("ERROR: failed to save data to file "
                                                                    + fileName);
            }
        } else {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                System.err.println("ERROR: failed to create "
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
            System.out.println(fileName + " file not found.");
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
            System.err.println("ERROR: failed to load data from"
                                                        + fileName + " file!");
            System.err.println("");
        } catch (IOException e) {
                System.err.println("ERROR: failed to load data from"
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
            System.err.println("ERROR: failed to create data file!");
        }
    }
    
    /**
     * Creates properties directory.
     */    
    private void createDirectories() {
        try {
            Files.createDirectories(propertyDir);
        } catch (IOException e) {
            System.err.println("ERROR: failed to create property directories!");
        }
    }
    
}
