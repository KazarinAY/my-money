package kazarin.my_money.TESTS;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;

public class TestEnvironment {	
	@Test
	public void testLoadAndSave(){
		Environment env = Environment.getInstance(true);
		Operations ops = env.load();
		env.save();
	}
}