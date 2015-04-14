package kazarin.my_money.model;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;
import java.lang.IllegalArgumentException;
import java.text.ParseException;

/**
 * An operation.
*/
public class Operation {
    
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
     * @param howMuch       how much money spent or received
     * @param date          when was the operation
     * @param description   description of operation
     * @param tags          tags
     */
    public Operation(final BigDecimal howMuch, final Date date,
                     final String description, final String... tags) {
        if (howMuch == null || date == null) throw new IllegalArgumentException(
                                                    "howMuch or date == null");

        ids++;
        this.id = ids;
        this.howMuch = howMuch;
        this.date = date;
        if (description == null) this.description = "";
        else this.description = description;
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
     * @param howMuch       how much money spent or received
     * @param description   description of operation
     * @param tags          tags
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

    /*
     * @param newIds    sets newIds
     
    public static final void setIds(final int newIds) {
        ids = newIds;
    }
    */

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
    public final Set<String> getTags() {
        return tags;
    }

    /**
     * @return description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param howMuch   sets howMuch
     */
    public final void setHowMuch(final BigDecimal howMuch) {
        if (howMuch == null)
                        throw new IllegalArgumentException("howMuch == null");

        this.howMuch = howMuch;
    }

    /**
     * @param date  sets date
     */
    public final void setDate(final Date date) {
        if (date == null) throw new IllegalArgumentException("date == null");

        this.date = date;
    }

    /**
     * @param dateString  sets String date
     */
    public final void setDate(final String dateString) {
        if (dateString == null)
                            throw new IllegalArgumentException("date == null");

        SimpleDateFormat dateFormat =
                            new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {               
            this.date = dateFormat.parse(dateString);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("date mast be dd-MM-yyyy");
        }
    }

    /**
     * @param tags  sets tags
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
     * @param description   sets description
     */
    public final void setDescription(final String description) {
        if (description == null) this.description = "";
        else this.description = description;
    }

    /**
     * Returns a string representation of the operation in command format. 
     * @return String   
     */
    public final String toCommandString() {

        String tagsStr = "";
        for (String tag : tags) {
            tagsStr += ", " + tag;
        }        
        tagsStr = tagsStr.substring(2);

        String descriptionStr = "";
        if (description != null) {
            descriptionStr = description;
        }
        SimpleDateFormat dateFormat =
                            new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return "add " + howMuch.toString() + ":" 
                         + dateFormat.format(date) + ":"
                         + descriptionStr + "#" + tagsStr;
    }
    
    @Override
    public final String toString() {        

        String tagsStr = "";
        for (String tag : tags) {
            tagsStr += ", " + tag;
        }        
        tagsStr = tagsStr.substring(2);

        String descriptionStr = "";
        if (description != null) {
            descriptionStr = description;
        }
        SimpleDateFormat dateFormat =
                            new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return id + ": " + howMuch.toString() + " руб " 
                         + dateFormat.format(date) + " "
                         + descriptionStr + " #: " + tagsStr;
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

        if (id != op.getId()) return false;        

        if (howMuch != null ? !howMuch.equals(op.getHowMuch())
                            : op.getHowMuch() != null) return false;
       
        if (date != null ? !date.equals(op.getDate())
                         : op.getDate() != null) return false;
        
        if (tags != null ? !tags.equals(op.getTags())
                         : op.getTags() != null) return false;

        if (description != null ? !description.equals(op.getDescription())
                                : op.getDescription() != null) return false;

        return true;
    }    
}
