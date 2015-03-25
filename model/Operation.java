package kazarin.my_money.model;

import java.util.Date;
import java.util.Set;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Operation
*/
public class Operation implements Serializable{
	private static final long serialVersionUID = 1L;

	private BigDecimal howMuch;
	private Date date;
	private Set<String> tags;
	private String description;

	public Operation(BigDecimal howMuch, Date date, 
					 String description, String... tags){
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
		this.howMuch = howMuch;
		this.date = new Date();
		this.description = description;
		this.tags = new HashSet<>();
		if (tags != null){
			for (String tag : tags){
				this.tags.add(tag);				
			}
		}
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

		return howMuch.toString() + " руб " + date.toString() + " " 
				+ descriptionStr + " тэги: " + tagsStr;
	}
	
	@Override
	public int hashCode(){
		int code = 0;
		if (howMuch != null) code += howMuch.hashCode();
		if (date != null) code += date.hashCode();
		return code;
	}

	@Override
	public boolean equals(Object obj){
		if (obj == null) return false;
		
		if ( !(obj instanceof Operation) ) return false;

		Operation op = (Operation) obj;

		if (this.hashCode() != op.hashCode()) return false;

		if (this.howMuch.equals(op.getHowMuch()) &&
			this.date.equals(op.getDate()) &&
			this.tags.equals(op.getTags()) &&
			this.description.equals(op.getDescription()) ) return true;

		return false;
	}
	/**
	 * Thrown when an application tries to call operation 
	 * that has null howMuch or date field
	*/
	private static class NullOperationException extends RuntimeException{

	}
}