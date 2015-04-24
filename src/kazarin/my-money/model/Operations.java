package kazarin.my_money.model;

import kazarin.my_money.db.Dao;
import kazarin.my_money.db.MySqlDao;
import kazarin.my_money.db.HSqlDao;
import kazarin.my_money.db.StubDao;
import kazarin.my_money.db.DBTypes;

import java.util.logging.*;

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

/**
 * Operations.
 * Singletone.
 */
public final class Operations {

    private static Logger logger;

    static {
        try {
            logger = Logger.getLogger(Operations.class.getName());
            FileHandler fh = new FileHandler("/tmp/Operations.log");  
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

    private Dao<Operation> dao;
    /**
     * An instance of Operations.
     */
    private static Operations instance;

    /**
     * An operations list.
     */
    private List<Operation> list;

    /**
     * Aimple date format.
     */
    private SimpleDateFormat dateFormat;    

    /**
     * Constructor.
     */
    private Operations(DBTypes type) {

        switch (type) {
            
            case TEST:
                dao = new StubDao();                
                logger.info("new TEST Operations Constructed");
                break;
            
            case MYSQL:                
                dao = new MySqlDao();
                logger.info("new MYSQL Operations Constructed");
                break;
            
            case HSQL:                
                dao = new HSqlDao();
                logger.info("new MYSQL Operations Constructed");
                break;

            default:
                logger.warning("something wrong with DBTypes");
                break;
        }

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        list = new ArrayList<>();
        list = dao.getAll();

    }    

    /**
     * Gets instance of Operations.
     * @return instance
     */
    public static Operations getInstance(DBTypes type) {
        if (instance == null) {
            instance = new Operations(type);
        }
        return instance;
    }

    /**
     * Shows all operations list.
     */
    public void printAllList() {
        System.out.println("All list:");
        for (Operation el : list) {
            System.out.println(el);
        }
    }

    /**
     * Prints operations statistic.
     */
    public void printStatistic() {
        if (list.size() == 0) {
            System.out.println("No operations");
            return;
        }
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal consumption = BigDecimal.ZERO;
        for (Operation op : list) {
            if (op.getSum().compareTo(BigDecimal.ZERO) == -1) {
                consumption = consumption.add(op.getSum());
            } else if (op.getSum().compareTo(BigDecimal.ZERO) == 1) {
                income = income.add(op.getSum());
            }
        }
        BigDecimal balance = income.add(consumption);
        System.out.println("Total income = " + income + ", total consumption = "
                                             + consumption);
        System.out.println("Balance = " + balance + ", total operations = "
                                        + list.size());
    }

    /**
     * Adds an operation to the operations list.
     * @param line      command line
     * @throws WrongCommandException if bad command line
     */
    public void add(String line) throws WrongCommandException {        
        logger.log(Level.INFO,"ADD: Line: " + line);
        if (line == null || line.equals("")) throw new WrongCommandException();

        if (!line.startsWith("add ")) throw new WrongCommandException();

        line = line.substring(4); //size of "add "

        int colons = line.replaceAll("[^:]", "").length();
        int bars = line.replaceAll("[^#]", "").length();

        if (colons > 2 || bars > 1) throw new WrongCommandException();

        BigDecimal summ = null;
        Date date = null;
        String description = null;
        String[] tagsArr = null;

        if (bars == 1 ) {            
            if (line.indexOf('#') != line.length() - 1){
                if (line.indexOf('#') < line.lastIndexOf(':')) throw new WrongCommandException();

                String argsLine = line.split("#")[1];
                tagsArr = argsLine.split(",");
                
                for (int i = 0; i < tagsArr.length; i++) {
                    tagsArr[i] = tagsArr[i].trim();
                }
                logger.log(Level.INFO, "ADD: tagsArr = " + tagsArr.length + " "
                                                                    + Arrays.toString(tagsArr));
            }
            line = line.split("#")[0];
        }

        String[] tokens = line.split(":");        
        logger.log(Level.INFO, "ADD: Line to parse: " + line);
        Operation newOp = parseOperation(tokens);

        logger.log(Level.INFO, "ADD: after parse: " + newOp.getSum() + " " + newOp.getDate() + " "
                                                    + newOp.getDescription() );
        
        if (newOp.getSum() == null) throw new WrongCommandException();
        if (newOp.getDate() == null) newOp.setDate(new Date()); 
        newOp.setTags(tagsArr);

        dao.add(newOp);
        setList(dao.getAll());
    }

    private Date parseDate(String stringDate) throws WrongCommandException{
        try {
            return dateFormat.parse(stringDate); 
        } catch (ParseException pe) {
            throw new WrongCommandException();
        }
    }

    /**
     * Deletes an operation from the operations list.
     * @param line      command line
     * @throws WrongCommandException if bad command line
     */
    public void delete(String line) throws WrongCommandException {
        logger.log(Level.INFO,"DELETE: Line: " + line);
        if (line == null || line.equals("")) throw new WrongCommandException();

        if (!line.startsWith("delete ")) throw new WrongCommandException();
        
        line = line.substring(7);  //size of "delete "
        line = line.trim();

        try {            
            int id = Integer.parseInt(line);            
            
            Iterator<Operation> iterator = list.iterator();

            while (iterator.hasNext()) {
                Operation op = iterator.next();
                
                if (op.getId() == id) {                    
                    dao.delete(op);                 
                    break;
                }
            }
            setList(dao.getAll());

        } catch (NumberFormatException e) {
            throw new WrongCommandException();
        }

    }

    /**
     * Changes an operation in operations list.
     * @param line      command line
     * @throws WrongCommandException if bad command line
     */
    public void change(String line) throws WrongCommandException {        
        logger.log(Level.INFO, "CHANGE: Line: " + line);
        if (line == null || line.equals("")) throw new WrongCommandException();

        if (!line.startsWith("change ")) throw new WrongCommandException();

        line = line.substring(7); //size of "change "

        int colons = line.replaceAll("[^:]", "").length();
        int bars = line.replaceAll("[^#]", "").length();

        if (colons > 3 || bars > 1 || (bars == 0 && colons == 0 ))
                                                          throw new WrongCommandException();
        String[] newTags = null;
        if (bars == 1) {
            if (line.indexOf('#') < line.lastIndexOf(':')) throw new WrongCommandException();

            if (line.indexOf('#') != line.length() - 1) {
                String argsLine = line.split("#")[1];
                logger.log(Level.INFO, "CHANGE: argsLine: " + argsLine);
                newTags = argsLine.split(",");
            } else {
                newTags = new String[0];
            }           

            line = line.split("#")[0];
        }        

        int idToFind = -1;        
        if (line.lastIndexOf(":") == line.length() - 1) {
            line += " ";
        }

        String[] newTokens = line.split(":");
        logger.log(Level.INFO, "CHANGE: newTokens = " + newTokens.length + " "
                                                                    + Arrays.toString(newTokens));
        if (newTokens.length > 0) {
            try {
                idToFind = Integer.parseInt(newTokens[0].trim());
            } catch (NumberFormatException nfe){
                throw new WrongCommandException();
            }
        }
        logger.log(Level.INFO, "CHANGE: idToFind: " + idToFind);

        logger.log(Level.INFO, "CHANGE: size(): " + size());

        int index = findOperationIndexById(idToFind);
        logger.log(Level.INFO, "CHANGE: index: " + index);
        if (index == -1) throw new WrongCommandException();

        String[] parameters = new String[newTokens.length - 1];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = newTokens[i + 1];
        }

        logger.log(Level.INFO, "CHANGE: parameters = " + parameters.length + " "
                                                                    + Arrays.toString(parameters));
        Operation changeOp = parseOperation(parameters);       

        Operation operationToChange = list.get(index);

        if (changeOp.getSum() != null) operationToChange.setSum(changeOp.getSum());
        if (changeOp.getDate() != null) operationToChange.setDate(changeOp.getDate());
        if (changeOp.getDescription() != null) operationToChange.setDescription(changeOp.getDescription());
        if (newTags != null) operationToChange.setTags(newTags);
        
        dao.update(operationToChange);

    }

    private Operation parseOperation(String[] prmtrs) throws WrongCommandException {
        logger.log(Level.INFO, "\tparseOperation start: ");      

        Operation operation = new Operation();
        logger.log(Level.INFO, "\tparseOperation operation: " + operation.toString());
        if (prmtrs.length > 0) {
            logger.log(Level.INFO, "\tparseOperation 0: " + prmtrs[0]);
            parseWordForOperation(operation, prmtrs, 0);
        }

        if (prmtrs.length > 1) {
            logger.log(Level.INFO, "\tparseOperation 1: " + prmtrs[1]);
            parseWordForOperation(operation, prmtrs, 1);
        }

        if (prmtrs.length > 2) {
            logger.log(Level.INFO, "\tparseOperation 2: " + prmtrs[2]);
            parseWordForOperation(operation, prmtrs, 2);
        }

        logger.log(Level.INFO, "\tparseOperation end:" + operation.getSum() + " " + operation.getDate() + " "
                                                    + operation.getDescription() );
        return operation;
    }

    private void parseWordForOperation(Operation operation, String[] prmtrs, int index) 
                                                                throws WrongCommandException {
        // "[+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][+-]?\\d+)?"
        logger.log(Level.INFO, "\t\tparseWordForOperation: " + prmtrs[index]);
        //if (prmtrs[index].trim().equals("")) throw new WrongCommandException();

        if (prmtrs[index].trim().matches("[+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+)")) {
            if (operation.getSum() != null) throw new WrongCommandException();
            operation.setSum(new BigDecimal(prmtrs[index].trim()));            
        } else if (prmtrs[index].trim().matches("\\d{2}-\\d{2}-\\d{4}")) {
            if (operation.getDate() != null) throw new WrongCommandException();
            try {
                    operation.setDate(dateFormat.parse(prmtrs[index].trim()));
                } catch (ParseException pe) {
                    throw new WrongCommandException();
                }
        } else {
            if (operation.getDescription() != null) throw new WrongCommandException();
            operation.setDescription(prmtrs[index].trim());
        }
        logger.log(Level.INFO, "\t\tparseWordForOperation: good");
    }

    /**
     * @param id    operation id
     * @return int   the index of the first occurrence of the
     * specified element in this list, or -1 if this list does not
     * contain the element.
     */
    private int findOperationIndexById(final int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets list size.
     */
    public int size(){
        return list.size();
    }

    /**
     * @return list
     */
    public  List<Operation> getList() {
        return list;
    }

    /**
     * @param list  Sets up the list.
     */
    public  void setList(List<Operation> list) {
        this.list = list;
    }

    @Override
    public int hashCode() {
        int code = 0;
        if (list != null) {
            code = list.hashCode();
        }
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == null) return false;

        if (obj == null || getClass() != obj.getClass()) return false;

        Operations ops = (Operations) obj;

        if (list != null ? !list.equals(ops.getList())
                         : ops.getList() != null) return false;

        return true;
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
                ;
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

            String readedLine = "";
            while (reader.ready()) {
                readedLine = reader.readLine();                
                add(readedLine);
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

}
