package kazarin.my_money.model;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * Operation
*/
public class Operation implements Serializable{
	private static final long serialVersionUID = 3L;

	private static int ids = 0;

	private int id;
	private BigDecimal howMuch;
	private Date date;
	private Set<String> tags;
	private String description;

	public Operation(BigDecimal howMuch, Date date, 
					 String description, String... tags){
		ids++;
		id++;
		this.howMuch = howMuch;
		this.date = date;
		this.description = description;
		this.tags = new HashSet<>();
		if (tags != null){
			for (String tag : tags){
				this.tags.add(tag);
			}
		}
	}

	public Operation(BigDecimal howMuch, 
					 String description, String... tags){
		this(howMuch, new Date(), description, tags);
	}

	public int getId(){
		return id;
	}

	public BigDecimal getHowMuch(){
		return howMuch;
	}

	public Date getDate(){
		return date;
	}

	public Set getTags(){
		return tags;
	}

	public String getDescription(){
		return description;
	}

	@Override
	public String toString(){
		if (howMuch == null || date == null){
			throw new NullOperationException();
		}
		String tagsStr = "";
		for (String tag : tags){
			tagsStr += tag + " ";
		}
		String descriptionStr = "";
		if (description != null){ descriptionStr = description; }
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		return id + ": " + howMuch.toString() + " руб " + dateFormat.format(date) + " " 
				+ descriptionStr + " тэги: " + tagsStr;
	}
	
	@Override
	public int hashCode(){
		int code = id;
		code = 31*code + (howMuch != null ? howMuch.hashCode() : 0);
		code = 31*code + (date != null ? date.hashCode() : 0);		
		return code;
	}

	@Override
	public boolean equals(Object obj){
		if (this == null) return false;
		
		if ( obj == null || getClass() != obj.getClass() ) return false;

		Operation op = (Operation) obj;

		if (id != op.getId()) return false;
		if (howMuch != null ? !howMuch.equals(op.getHowMuch()) : 
							 	op.getHowMuch() != null) return false;
		if (date != null ? !date.equals(op.getDate()) : 
							  op.getDate() != null) return false;
		if (tags != null ? !tags.equals(op.getTags()) :
							op.getTags() != null) return false;
		if (description != null ? !description.equals(op.getDescription()) : 
							  		op.getDescription() != null) return false;
		return true;
	}
	/**
	 * Thrown when an application tries to call operation 
	 * that has null howMuch or date field
	*/
	private static class NullOperationException extends RuntimeException{

	}
}