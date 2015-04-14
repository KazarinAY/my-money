package kazarin.my_money.model;

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

        if (line == null || line == "") throw new WrongCommandException();

        if (!line.startsWith("add ")) throw new WrongCommandException();

        line = line.substring(4);

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

        try {   
            tokens[0] = tokens[0].trim();            
            summ = new BigDecimal(tokens[0]);
        } catch (NumberFormatException nfe) {
            throw new WrongCommandException();
        }

        if (tokens.length > 2) { 
            tokens[1] = tokens[1].trim();
            
            if (tokens[1].matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                tokens[1] = tokens[1].replaceAll("\\.", "-");
            }

            if (tokens[1].matches("\\d{2}-\\d{2}-\\d{4}")){

                date = parseDate(tokens[1]); 
                
            } else {
                description = tokens[1];
            }  
        } 

        if (tokens.length == 3 ) {
            if (description == null){
                description = tokens[2];
            } else {                        
                date = parseDate(tokens[2]); 
            }            
        }       

        if (date == null) {
            date = new Date();
        }

        list.add(new Operation(summ, date, description, tagsArr));
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
    public void delete(final String line) throws WrongCommandException {
        try {
            System.out.println(line.split(" ")[1]);
            int id = Integer.parseInt(line.split(" ")[1]);
            System.out.println("id = " + id + " ids = " + Operation.getIds());
            if (id > Operation.getIds()) {
                throw new WrongCommandException();
            }
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Operation op = (Operation) iterator.next();
                if (op.getId() == id) {
                    iterator.remove();
                }
            }
        } catch (NumberFormatException e) {
            throw new WrongCommandException();
        }

    }

    /**
     * Changes an operation in operations list.
     * @param line      command line
     * @throws WrongCommandException if bad command line
     */
    public void change(final String line) throws WrongCommandException {
        int id = 0;
        BigDecimal newHowMatch = null;
        Date newDate = null;
        String newDescription = null;
        String[] newTags = null;
        int indexOfLineWithowtChange = 6;
        String lineWithowtChange = line.substring(indexOfLineWithowtChange);
        
        StringTokenizer st = new StringTokenizer(lineWithowtChange, ":");
        if (st.hasMoreTokens()) {
            id = Integer.parseInt(st.nextToken().trim());
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.contains("#")) {
                    String[] tmp = token.split("#");
                    token = tmp[0];
                    newTags = tmp[1].split(",");
                }
                token = token.trim();
                if (token.matches("\\d+(\\.0*)?")) {
                    newHowMatch = new BigDecimal(token);
                } else if (token.matches("\\d{2}-\\d{2}-\\d{4}")) {
                    
                    try {
                        newDate = dateFormat.parse(token);
                    } catch (ParseException pe) {
                        throw new WrongCommandException();
                    }
                } else if (!token.trim().equals("")) {
                    newDescription = token;
                }
            }
        } else {
            throw new WrongCommandException();
        }
        int count = findOperationById(id);
        if (count != 0) {
            Operation operationToChange = list.get(count);
            if (newHowMatch != null) {
                operationToChange.setHowMuch(newHowMatch);
            }

            if (newDate != null) {
                operationToChange.setDate(newDate);
            }

            if (newTags != null) {
                operationToChange.setTags(newTags);
            }

            if (newDescription != null) {
                operationToChange.setDescription(newDescription);
            }
        } else {
            throw new WrongCommandException();
        }
    }

    /**
     * @param id    operation id
     * @return int   the index of the first occurrence of the
     * specified element in this list, or -1 if this list does not
     * contain the element.
     */
    private int findOperationById(final int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return list
     */
    public  List<Operation> getList() {
        return list;
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
