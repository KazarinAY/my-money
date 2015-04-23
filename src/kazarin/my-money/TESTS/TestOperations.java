package kazarin.my_money.TESTS;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Operation;
import kazarin.my_money.model.WrongCommandException;
import java.math.BigDecimal;
import java.io.IOException;

public class TestOperations {	
	Operations operations = Operations.getInstance(true);

//ADD

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
		operations.add("add 135 : 13042015 : test description # test tag1, testTag2");
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

	@Test
	public void testAdd15() throws WrongCommandException {
		operations.add("add 999 : 13-04-2015 : test description #test tag1,testTag2");
	}

	@Test
	public void testAdd16() throws WrongCommandException {		
		operations.add("add -135 : 13-04-2015 : test description # test tag1, testTag2");
	}

	@Test
	public void testAdd17() throws WrongCommandException {
		operations.add("add 135 : 13-04-2015 : test description");
	}

	@Test
	public void testAdd18() throws WrongCommandException {
		operations.add("add 135.75 : 13-04-2015 # test tag1, testTag2");
	}

	@Test
	public void testAdd19() throws WrongCommandException {
		operations.add("add +135 : test description # test tag1, testTag2");		
	}

	@Test
	public void testAdd20() throws WrongCommandException {		
		operations.add("add -135.04 # test tag1, testTag2");		
	}

	@Test
	public void testAdd21() throws WrongCommandException {
		operations.add("add 135.85");		
	}

	@Test
	public void testAdd22() throws WrongCommandException {
		operations.add("add 135 : 13-04-2015 : test, description # test tag1, testTag2");		
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd23() throws WrongCommandException {	
		operations.add("add -135 : 13.04.2015 : test description # test tag1, testTag2");
	}

	@Test
	public void testAdd24() throws WrongCommandException {		
		operations.add("add -135 : test description ");
	}

	@Test
	public void testAdd25() throws WrongCommandException {		
		operations.add("add test description : 13-04-2015 : -135  # test tag1, testTag2");
	}

	@Test
	public void testAdd26() throws WrongCommandException {		
		operations.add("add 13-04-2015 : test description : -135  # test tag1, testTag2");		
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd27() throws WrongCommandException {		
		operations.add("add 13-04-2015 : 14-05-2015 : -135  # test tag1, testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testAdd28() throws WrongCommandException {		
		operations.add("add 500 : -135  # test tag1, testTag2");
	}

//DELETE

	@Test(expected=WrongCommandException.class)
	public void testDelete0() throws WrongCommandException {
		operations.delete(null);
	}

	@Test(expected=WrongCommandException.class)
	public void testDelete1() throws WrongCommandException {
		operations.delete("");
	}

	@Test(expected=WrongCommandException.class)
	public void testDelete2() throws WrongCommandException {
		operations.delete("delete");
	}

	@Test(expected=WrongCommandException.class)
	public void testDelete3() throws WrongCommandException {
		operations.delete("delete wer");
	}

	@Test
	public void testDelete4() throws WrongCommandException {
		operations.delete("delete " + (operations.size() + 10));
	}

	@Test(expected=WrongCommandException.class)
	public void testDelete5() throws WrongCommandException {
		operations.delete("asdfasdf");
	}

	@Test
	public void testDelete6() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.delete("delete " + (size - 1));
	}

	@Test
	public void testDelete7() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.delete("delete " + (size / 2));			
	}

//CHANGE

	@Test(expected=WrongCommandException.class)
	public void testChange0() throws WrongCommandException {
		operations.change(null);
	}

	@Test(expected=WrongCommandException.class)
	public void testChange1() throws WrongCommandException {
		operations.change("");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange2() throws WrongCommandException {
		operations.change("some text");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange3() throws WrongCommandException {
		operations.change("change some text");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange4() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2));
	}

	@Test(expected=WrongCommandException.class)
	public void testChange5() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + " some text");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange6() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + " 125 : description");
	}

	@Test
	public void testChange7() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": "); //changes description
	}

	@Test(expected=WrongCommandException.class)
	public void testChange8() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + " 125 : description");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange9() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 : description: description2");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange10() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 # test tag1, testTag2: description");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange11() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 # test tag1 # testTag2");
	}

	@Test(expected=WrongCommandException.class)
	public void testChange12() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change # test tag1 # testTag2");
	}

	@Test
	public void testChange13() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 : 12-12-2004: new description "
																+ "# test tag1, testTag2");
	}

	@Test
	public void testChange14() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 : 12-12-2004: new description ");
	}

	@Test
	public void testChange15() throws WrongCommandException {		
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 : 12-12-2004: # test tag1, testTag2");
	}

	@Test
	public void testChange16() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 : new description "
																+ "# test tag1, testTag2");
	}

	@Test
	public void testChange17() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 125 # test tag1, testTag2");
	}

	@Test
	public void testChange18() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + "# test tag1, testTag2");
	}

	@Test
	public void testChange19() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 12-12-2004: new description "
																+ "# test tag1, testTag2");
	}

	@Test
	public void testChange20() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 12-12-2004 "
																+ "# test tag1, testTag2");
	}

	@Test
	public void testChange21() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": new description "
																+ "# test tag1, testTag2");
	}

	@Test
	public void testChange22() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": new description");
	}

	@Test
	public void testChange23() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": 12-12-2004");
	}

	@Test
	public void testChange24() throws WrongCommandException {
		int size = operations.size();
		if (size == 0) return;
		operations.change("change " + (size/2) + ": -125");
	}

}
