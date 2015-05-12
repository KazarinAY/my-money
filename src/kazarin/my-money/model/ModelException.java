package kazarin.my_money.model;
/**
 *
 */
public class ModelException extends RuntimeException{
	public ModelException(String messege, Throwable cause) {
		super(messege, cause);
	}

	public ModelException(String messege) {
		super(messege);
	}
}