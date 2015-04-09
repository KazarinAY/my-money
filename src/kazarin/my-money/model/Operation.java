package kazarin.my_money.model;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * An operation.
*/
public class Operation implements Serializable {
	/**
	 *	A version number, which is used during deserialization.
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * A number of all ids. Any single id is less then ids.
	 */
	private static int ids = 0;

	/**
	 * The operation id.
	 */
	private int id;

	/**
	 * The amount of money.
	 */
	private BigDecimal howMuch;

	/**
	 * When was the operation.
	 */
	private Date date;

	/**
	 * Tags.
	 */
	private Set<String> tags;

	/**
	 * Description of the operation.
	 */
	private String description;

	/**
	 * Constructor.
	 * @param howMuch     	how much money spent or received
	 * @param date 			when was the operation
	 * @param description 	description of operation
	 * @param tags 			tags
	 */
	public Operation(final BigDecimal howMuch, final Date date,
					 final String description, final String... tags) {
		ids++;
		this.id = ids;
		this.howMuch = howMuch;
		this.date = date;
		this.description = description;
		this.tags = new HashSet<>();
		if (tags != null) {
			for (String tag : tags) {
				this.tags.add(tag);
			}
		}
	}

	/**
	 * Constructor.
	 * Same as public Operation(BigDecimal, Date, String, String... ),
	 * except the date. Sets current date.
	 * @param howMuch     	how much money spent or received
	 * @param description 	description of operation
	 * @param tags 			tags
	 */
	public Operation(final BigDecimal howMuch,
					 final String description, final String... tags) {
		this(howMuch, new Date(), description, tags);
	}

	/**
	 * @return ids
	 */
	public static final int getIds() {
		return ids;
	}

	/**
	 * @param newIds 	sets newIds
	 */
	public static final void setIds(final int newIds) {
		ids = newIds;
	}

	/**
	 * @return id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return howMuch
	 */
	public final BigDecimal getHowMuch() {
		return howMuch;
	}

	/**
	 * @return date
	 */

	public final  Date getDate() {
		return date;
	}

	/**
	 * @return tags
	 */
	public final Set getTags() {
		return tags;
	}

	/**
	 * @return description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @param howMuch 	sets howMuch
	 */
	public final void setHowMuch(final BigDecimal howMuch) {
		this.howMuch = howMuch;
	}

	/**
	 * @param date 	sets date
	 */
	public final void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * @param tags 	sets tags
	 */
	public final void setTags(final String[] tags) {
		this.tags.clear();
		if (tags != null) {
			for (String tag : tags) {
				this.tags.add(tag);
			}
		}
	}

	/**
	 * @param description 	sets description
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public final String toString() {
		if (howMuch == null || date == null) {
			throw new NullOperationException();
		}
		String tagsStr = "";
		for (String tag : tags) {
			tagsStr += tag + " ";
		}
		String descriptionStr = "";
		if (description != null) {
			descriptionStr = description;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		return id + ": " + howMuch.toString() + " руб " + dateFormat.format(date) + " "
				+ descriptionStr + " тэги: " + tagsStr;
	}

	@Override
	public final int hashCode() {
		int code = id;
		code = 31 * code + (howMuch != null ? howMuch.hashCode() : 0);
		code = 31 * code + (date != null ? date.hashCode() : 0);
		return code;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == null) return false;
		
		if (obj == null || getClass() != obj.getClass()) return false;

		Operation op = (Operation) obj;

		if (id != op.getId()) {
			return false;
		}

		if (howMuch != null ? !howMuch.equals(op.getHowMuch())
							: op.getHowMuch() != null) {
		 	return false;
		}

		if (date != null ? !date.equals(op.getDate())
						 : op.getDate() != null) {
			return false;
		}

		if (tags != null ? !tags.equals(op.getTags())
						 : op.getTags() != null) {
			return false;
		}
		if (description != null ? !description.equals(op.getDescription())
								: op.getDescription() != null) {
			return false;
		}

		return true;
	}

	/**
	* NullOperationException.
	 * Thrown when an application tries to call operation
	 * that has null howMuch or date field
	*/
	private static class NullOperationException extends RuntimeException {

	}
}
