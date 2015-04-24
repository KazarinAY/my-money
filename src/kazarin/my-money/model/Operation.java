package kazarin.my_money.model;

import java.util.logging.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;
import java.lang.IllegalArgumentException;
import java.text.ParseException;
import java.io.IOException;

/**
 * An operation.
*/
public class Operation {

    private static SimpleDateFormat dateFormat;
    
    private static Logger logger;

    static {
        try {
            logger = Logger.getLogger(Operation.class.getName());
            FileHandler fh = new FileHandler("/tmp/Operation.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    }

    /**
     * The operation id.
     */
    private int id;

    /**
     * The amount of money.
     */
    private BigDecimal sum;

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
     * @param sum       how much money spent or received
     * @param date          when was the operation
     * @param description   description of operation
     * @param tags          tags
     */
    public Operation(final int id, final BigDecimal sum, final Date date,
                     final String description, final String... tags) {
        this.id = id;
        this.sum = sum;
        this.date = date;
        if (description == null) this.description = "";
        else this.description = description;
        this.tags = new HashSet<>();
        if (tags != null) {
            for (String tag : tags) {
                this.tags.add(tag);
            }
        }

        logger.info("new Operation Constructed");
        logger.info(toString());
    }

    /**
     * Constructor.
     * Same as public Operation(BigDecimal, Date, String, String... ),
     * except the date. Sets current date.
     * @param sum       how much money spent or received
     * @param description   description of operation
     * @param tags          tags
     */
    public Operation(final int id, final BigDecimal sum,
                     final String description, final String... tags) {
        this(id, sum, new Date(), description, tags);
    }

    /**
     * Constructor for adding and changing.
     */
    public Operation() { 
        this.sum = null;
        this.date = null;
        this.description = null;
        this.tags = null;
    } 

    /**
     * @return id
     */
    public final int getId() {
        return id;
    }

    /**
     * @return sum
     */
    public final BigDecimal getSum() {
        return sum;
    }

    /**
     * @return date
     */
    public final  Date getDate() {
        return date;
    }

    /**
     * @return date string
     */
    public final  String getDateStr() {
        return dateFormat.format(date);
    }

    /**
     * @return tags
     */
    public final Set<String> getTags() {
        return tags;
    }

    /**
     * @return tags string
     */
    public final String getTagsStr() {
        if (tags == null) return null;
        String tagsStr = "";
        for (String tag : tags) {
            tagsStr += ", " + tag.trim();
        } 
        if (tagsStr.startsWith(", "))      
            tagsStr = tagsStr.substring(2);
        return tagsStr;
    }

    /**
     * @return description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param id   sets id
     */
    public final void setId(final int id) {
        this.id = id;
    }

    /**
     * @param sum   sets sum
     */
    public final void setSum(final BigDecimal sum) {
        if (sum == null)
                        throw new IllegalArgumentException("sum == null");

        this.sum = sum;
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
        if (this.tags == null) {
            this.tags = new HashSet<>();
        } else {
            this.tags.clear();
        }        
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

        String tagsStr = getTagsStr();
        
        String descriptionStr = "";
        if (description != null) {
            descriptionStr = description;
        }
        
        return "add " + sum.toString() + ":" 
                         + dateFormat.format(date) + ":"
                         + descriptionStr + "#" + tagsStr;
    }
    
    @Override
    public final String toString() {        
        String sumStr = "null";
        if (sum != null) sumStr = sum.toString();

        String dateStr = "null";
        if (date != null) dateStr = dateFormat.format(date);
        
        String descriptionStr = "";
        if (description != null) {
            descriptionStr = description;
        }

        String tagsStr = "";
        if (tags != null) tagsStr = getTagsStr();
              
        
        
        return id + ": " + sumStr + " руб "
                         + dateStr + " "
                         + descriptionStr + " #: " + tagsStr;
    }

    @Override
    public final int hashCode() {
        int code = id;
        code = 31 * code + (sum != null ? sum.hashCode() : 0);
        code = 31 * code + (date != null ? date.hashCode() : 0);
        return code;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == null) return false;
        
        if (obj == null || getClass() != obj.getClass()) return false;

        Operation op = (Operation) obj;

        if (id != op.getId()) return false;        

        if (sum != null ? !sum.equals(op.getSum())
                            : op.getSum() != null) return false;
       
        if (date != null ? !date.equals(op.getDate())
                         : op.getDate() != null) return false;
        
        if (tags != null ? !tags.equals(op.getTags())
                         : op.getTags() != null) return false;

        if (description != null ? !description.equals(op.getDescription())
                                : op.getDescription() != null) return false;

        return true;
    }    
}
