package kazarin.my_money.model;

import java.util.Date;
import java.util.Set;
import java.math.BigDecimal;

class Operation{
	private BigDecimal howMuch;
	private Date date;
	private Set<String> tags;
	private String description;

	public Operation(BigDecimal howMuch, Date date, 
					 String description, String... tags){
		this.howMuch = howMuch;
		this.date = date;
		this.description = description;
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
		if (tags != null){
			for (String tag : tags){
				this.tags.add(tag);
			}
		}
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

	private static class NullOperationException extends RuntimeException{

	}
}

