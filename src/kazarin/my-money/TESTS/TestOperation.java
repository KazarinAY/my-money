package kazarin.my_money.TESTS;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import kazarin.my_money.model.Operation;

import java.math.BigDecimal;
import java.lang.IllegalArgumentException;
import java.util.Date;

public class TestOperation {
	Operation testOperation = new Operation(1, new BigDecimal("100"),
                     "test description", new String[]{"test tag1", "test tag2", "test tag3"});

	@Test(expected=IllegalArgumentException.class)
	public void testSetHowMuchNull(){		
		testOperation.setHowMuch(null);		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetDateNull(){	
		Date date = null;	
		testOperation.setDate(date);		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetDateNullString(){	
		String date = null;	
		testOperation.setDate(date);		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetDateWrongString(){		
		testOperation.setDate("qwe-asdzxc-asdqw");		
	}
}
