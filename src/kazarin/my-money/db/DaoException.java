package kazarin.my_money.db;

public class DaoException extends RuntimeException {

	public DaoException(String messege,  Throwable cause) {
		super(messege, cause);
	}

	public DaoException(String messege) {
		super(messege);
	}
}