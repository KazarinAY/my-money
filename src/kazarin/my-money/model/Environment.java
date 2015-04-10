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

/**
 * Environment.
 * Singletone.
 */
public final class Environment {

    /**
     * An instance of Operations.
     */
    private static Environment instance;    

    /**
     * Constructor.
     */
    private Environment() {
        /*
        String directory = System.getProperty("user.home") + "/mymoney";
        dataDir = Paths.get(directory);

        if (!Files.exists(dataDir)) {
            createDirectories();
        }
        dataFile = Paths.get(directory + "/data");
        */
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
            System.out.println("All list:");
            for (Operation el : operations.getList()) {
                System.out.println(el);
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
     * Creates data file.
     */
    /*
    private void createDataFile() {
        try {
            Files.createFile(dataFile);
        } catch (IOException e) {
            System.err.println("ERROR: failed to create data file!");
        }
    }
    */

    /**
     * Creates data directory.
     */
    /*
    private void createDirectories() {
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            System.err.println("ERROR: failed to create data directories!");
        }
    }
    */
}
