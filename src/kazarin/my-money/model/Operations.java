package kazarin.my_money.model;

import java.io.Externalizable;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.lang.NumberFormatException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.text.ParseException;


public class Operations implements Externalizable{
	private static final long serialVersionUID = 3L;
	private static Operations instance;
	private List<Operation> list;

	private Operations(){
		list = new ArrayList<>();
	}
	
	public static Operations getInstance(){
		if (instance == null) {
			instance = new Operations();			
		}		
		return instance;
	}	

	public void showAllList(){
		System.out.println("All list:");
		for (Operation el : list){
			System.out.println(el);
		}
	}

	public void printStatistic(){
		if (list.size() == 0){
			System.out.println("No operations");
			return;
		}
		BigDecimal income = BigDecimal.ZERO;
		BigDecimal consumption = BigDecimal.ZERO;		
		for (Operation op : list){
			if (op.getHowMuch().compareTo(BigDecimal.ZERO) == -1){
				consumption = consumption.add(op.getHowMuch());
			} else if (op.getHowMuch().compareTo(BigDecimal.ZERO) == 1){
				income = income.add(op.getHowMuch());
			}
		}
		BigDecimal balance = income.add(consumption);
		System.out.println("Total income = " + income + 
						   ", total consumption = " + consumption);
		System.out.println("Balance = " + balance + ", total operations = " + list.size());
	}	

	/**
	 * @param
	 */
	public void add(String line) throws WrongCommandException{		
		BigDecimal summ = null;
		Date date = null;
		String description = "";
		String[] tagsArr = null;		
		String[] tokens = line.split(":");
		try {			
			summ = new BigDecimal(tokens[0].split(" ")[1]);

			if (summ == null){
				throw new WrongCommandException();
			}			
			if (tokens.length > 1){
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
				date = dateFormat.parse(tokens[1]);
			}
			if (tokens.length > 2){
				String[] descAndTags = tokens[2].split("#");
				if (descAndTags.length > 0)
					description = descAndTags[0];
				if (descAndTags.length > 1){
					tagsArr = descAndTags[1].split(",");
				}

			}
		} catch (Exception e){
			throw new WrongCommandException();
		}
		if (date == null){
			date = new Date();
		}		
		list.add(new Operation(summ, date, description, tagsArr));		
	}

	/**
	 * @param
	 */
	public void delete(String line) throws WrongCommandException{
		try{
			System.out.println(line.split(" ")[1]);
			int id = Integer.parseInt(line.split(" ")[1]);
			System.out.println("id = " + id + " ids = " + Operation.getIds());
			if (id > Operation.getIds()){
				throw new WrongCommandException();
			}
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				Operation op = (Operation) iterator.next();
				if (op.getId() == id){
					iterator.remove();
				}
			}
		} catch (NumberFormatException e){
			throw new WrongCommandException();
		}

	}

	/**
	 * @param
	 */
	public void change(String line) throws WrongCommandException{
		int id = 0;
		BigDecimal newHowMatch = null;
		Date newDate = null;
		String newDescription = null;
		String[] newTags = null;
		line = line.substring(6);
		System.out.println(line);
		StringTokenizer st = new StringTokenizer(line, ":");
		if (st.hasMoreTokens()){
			id = Integer.parseInt(st.nextToken().trim());
			while(st.hasMoreTokens()){
				String token = st.nextToken();
				if (token.contains("#")){
					String[] tmp = token.split("#");
					token = tmp[0];
					newTags = tmp[1].split(",");
				}
				token = token.trim();
				if (token.matches("\\d+(\\.0*)?")){
					newHowMatch = new BigDecimal(token);
				} else if (token.matches("\\d{2}-\\d{2}-\\d{4}")){
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
					try{
						newDate = dateFormat.parse(token);
					} catch (ParseException pe){
						throw new WrongCommandException();
					}
				} else if (!token.trim().equals("")){
					newDescription = token;
				}
			}
		} else {
			throw new WrongCommandException();
		}
		int count = findOperationById(id);
		if (count != 0){
			Operation operationToChange = list.get(count);
			if (newHowMatch != null) operationToChange.setHowMuch(newHowMatch);

			if (newDate != null) operationToChange.setDate(newDate);

			if (newTags != null) operationToChange.setTags(newTags);

			if (newDescription != null) operationToChange.setDescription(newDescription);
		} else {
			throw new WrongCommandException();
		}
	}
	
	/**
	 * @param
	 * @return
	 */
	private int findOperationById(int id){		
		for (int i = 0; i < list.size(); i++){
			if (list.get(i).getId() == id){				
				return i;
			}
		}
		return 0;
	}

	/**
	 * @return
	 */
	public List getList(){
		return list;
	}

	@Override
	public int hashCode(){
		int code = 0;
		if (list != null) code = list.hashCode();		
		return code;
	}

	@Override
	public boolean equals(Object obj){
		if (this == null) return false;

		if (obj == null || getClass() != obj.getClass()) return false;	
		
		Operations ops = (Operations) obj;

		if (list != null ? !list.equals(ops.getList()) : ops.getList() != null) return false;

		return true;
	}	
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(list);        
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        list = (List) in.readObject();  
        int max = 0;
        for (Operation op : list){
        	if (op.getId() > max)
        		max = op.getId();
        }
        Operation.setIds(max);      
    }
}