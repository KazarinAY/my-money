package kazarin.my_money.TESTS;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
import kazarin.my_money.model.WrongCommandException;
import java.math.BigDecimal;
import java.io.IOException;

public class TestOperations {	
	Operations operations = Operations.getInstance();
	/*
	@Test(expected=WrongCommandException.class)
	public void testAdd0() throws WrongCommandException {
		operations.add(null);		
	}
	
	@Test(expected=WrongCommandException.class)
	public void testAdd1() throws WrongCommandException {
		operations.add("");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd2() throws WrongCommandException {
		operations.add("add");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd3() throws WrongCommandException {
		operations.add("add 135 : 13-04-2015 : test description : test tag1, testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd4() throws WrongCommandException {
		operations.add("add 13-04-2015 : test description # test tag1, testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd5() throws WrongCommandException {
		operations.add("add 135 : 13.04.2015 : test description # test tag1, testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd6() throws WrongCommandException {
		operations.add("abb 135 : 13.04.2015 : test description # test tag1, testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd7() throws WrongCommandException {
		operations.add("add 135 : 13.04.2015 : test description # test tag1 # testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd8() throws WrongCommandException {
		operations.add("add - 135 : 13-04-2015 : test description # test tag1, testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd9() throws WrongCommandException {
		operations.add("add 1 35 : 13-04-2015 : test description # test tag1, testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd10() throws WrongCommandException {
		operations.add("add 135 : 2015-13-04 : test description # test tag1, testTag2 : some text");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd11() throws WrongCommandException {
		operations.add("add 135 : 2015-13-04 : test description : test tag1 : testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd12() throws WrongCommandException {
		operations.add("add ");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd13() throws WrongCommandException {
		operations.add("add asaf");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd14() throws WrongCommandException {
		operations.add("add 135 : test description # test tag1, testTag2 : 2015-13-04");
	}

	*/
	@Test
	public void testAddNorm0() throws WrongCommandException {
		operations.add("add 135 : 13-04-2015 : test description # test tag1, testTag2");		
	}

	@Test
	public void testAddNorm1() throws WrongCommandException {		
		operations.add("add -135 : 13-04-2015 : test description # test tag1, testTag2");
	}

	@Test
	public void testAddNorm2() throws WrongCommandException {
		operations.add("add 135 : 13-04-2015 : test description");
	}

	@Test
	public void testAddNorm3() throws WrongCommandException {
		operations.add("add 135.75 : 13-04-2015 # test tag1, testTag2");
	}

	@Test
	public void testAddNorm4() throws WrongCommandException {
		operations.add("add +135 : test description # test tag1, testTag2");		
	}

	@Test
	public void testAddNorm5() throws WrongCommandException {		
		operations.add("add -135.04 # test tag1, testTag2");		
	}

	@Test
	public void testAddNorm6() throws WrongCommandException {
		operations.add("add 135.85");		
	}

	@Test
	public void testAddNorm7() throws WrongCommandException {
		operations.add("add 135 : 13-04-2015 : test, description # test tag1, testTag2");		
	}

	@Test
	public void testAddNorm8() throws WrongCommandException {		
		operations.add("add -135 : 13.04.2015 : test description # test tag1, testTag2");
	}

	@Test
	public void testAddNorm9() throws WrongCommandException {		
		operations.add("add -135 : test description ");
	}
}
