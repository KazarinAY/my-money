package kazarin.my_money.model;

import kazarin.my_money.db.OperationsDao;

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

/**
 * Operations.
 * Singletone.
 */
public final class Operations {

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
    private Operations() {
        list = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    }

    /**
     * Gets instance of Operations.
     * @return instance
     */
    public static Operations getInstance() {
        if (instance == null) {
            instance = new Operations();
        }
        return instance;
    }

    /**
     * Shows all operations list.
     */
    public void showAllList() {
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
            if (op.getHowMuch().compareTo(BigDecimal.ZERO) == -1) {
                consumption = consumption.add(op.getHowMuch());
            } else if (op.getHowMuch().compareTo(BigDecimal.ZERO) == 1) {
                income = income.add(op.getHowMuch());
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
//LOG        System.out.println("************ADD*****************" );
//LOG        System.out.println("Line: " + line);
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

        if (bars == 1) {            
            if (line.indexOf('#') < line.lastIndexOf(':')) throw new WrongCommandException();

            String argsLine = line.split("#")[1];
            tagsArr = argsLine.split(",");
            for (String arg : tagsArr) {
                arg = arg.trim();
            }

            line = line.split("#")[0];
        }

        String[] tokens = line.split(":");        
//LOG        System.out.println("Line to parse: " + line);
        Operation newOp = parseOperation(tokens);

//LOG        System.out.println("after parse: " + newOp.getHowMuch() + " " + newOp.getDate() + " "
//LOG                                                    + newOp.getDescription() );
        
        if (newOp.getHowMuch() == null) throw new WrongCommandException();
        if (newOp.getDate() == null) newOp.setDate(new Date()); 
        newOp.setTags(tagsArr);
        
        OperationsDao opDao = new OperationsDao();
        opDao.add(newOp);
        setList(opDao.getAll());
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
        if (line == null || line.equals("")) throw new WrongCommandException();
        
        if (!line.startsWith("delete ")) throw new WrongCommandException();
        
        line = line.substring(7);  //size of "delete "
        line = line.trim();
        try {            
            int id = Integer.parseInt(line);            
            
            Iterator iterator = list.iterator();
            
            OperationsDao opDao = new OperationsDao();
            while (iterator.hasNext()) {
                Operation op = (Operation) iterator.next();
                
                if (op.getId() == id) {                    
                    opDao.delete(op);                 
                    break;
                }
            }
            setList(opDao.getAll());

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
//LOG        System.out.println("************CHANGE*****************" );
//LOG        System.out.println("Line: " + line);
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

            String argsLine = line.split("#")[1];
            newTags = argsLine.split(",");

            line = line.split("#")[0];
        }        

        int idToFind = -1;        

        String[] newTokens = line.split(":");
        if (newTokens.length > 0) {
            try {
                idToFind = Integer.parseInt(newTokens[0].trim());
            } catch (NumberFormatException nfe){
                throw new WrongCommandException();
            }
        }
        System.out.println("idToFind: " + idToFind);
//LOG        System.out.println("size(): " + size());
        int index = findOperationIndexById(idToFind);
        System.out.println("index: " + index);
        if (index == -1) throw new WrongCommandException();

        String[] parameters = new String[newTokens.length - 1];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = newTokens[i + 1];
        }
        Operation changeOp = parseOperation(parameters);       

        Operation operationToChange = list.get(index);

        if (changeOp.getHowMuch() != null) operationToChange.setHowMuch(changeOp.getHowMuch());
        if (changeOp.getDate() != null) operationToChange.setDate(changeOp.getDate());
        if (changeOp.getDescription() != null) operationToChange.setDescription(changeOp.getDescription());
        if (newTags != null) operationToChange.setTags(newTags);
        OperationsDao opDao = new OperationsDao();
        opDao.update(operationToChange);
        //list.set(index, operationToChange);
    }

    private Operation parseOperation(String[] prmtrs) throws WrongCommandException {
//LOG        System.out.println("\tparseOperation start: ");
        Operation operation = new Operation();
        if (prmtrs.length > 0) {
            parseWordForOperation(operation, prmtrs, 0);
        }

        if (prmtrs.length > 1) {
            parseWordForOperation(operation, prmtrs, 1);
        }

        if (prmtrs.length > 2) {
            parseWordForOperation(operation, prmtrs, 2);
        }

//LOG        System.out.println("\tparseOperation end:" + operation.getHowMuch() + " " + operation.getDate() + " "
//LOG                                                    + operation.getDescription() );
        return operation;
    }

    private void parseWordForOperation(Operation operation, String[] prmtrs, int index) 
                                                                throws WrongCommandException {
        // "[+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][+-]?\\d+)?"
//LOG        System.out.println("\t\tparseWordForOperation: " + prmtrs[index]);
        if (prmtrs[index].trim().equals("")) throw new WrongCommandException();

        if (prmtrs[index].trim().matches("[+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+)")) {
            if (operation.getHowMuch() != null) throw new WrongCommandException();
            operation.setHowMuch(new BigDecimal(prmtrs[index].trim()));            
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
//LOG        System.out.println("\t\tparseWordForOperation: good");
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
    
}
