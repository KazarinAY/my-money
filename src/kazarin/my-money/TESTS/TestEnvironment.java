package kazarin.my_money.TESTS;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import kazarin.my_money.model.Operations;
import kazarin.my_money.model.Environment;
import java.io.IOException;

public class TestEnvironment {	
	@Test
	public void testLoadAndSave() throws IOException{
		Environment env = Environment.getInstance();		
	}
}