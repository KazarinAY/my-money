/*
 * StubDao for tests.
 */
package kazarin.my_money.db;

import kazarin.my_money.model.Operation;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

public class StubDao implements Dao<Operation> {

	public List<Operation> getAll() {
		List <Operation> list = new ArrayList<Operation>();
		list.add(new Operation(0, new BigDecimal("100"),
                     "test description0", new String[]{"test tag1", "test tag2", "test tag3"}));
		list.add(new Operation(1, new BigDecimal("-100"),
                     "test description1", new String[]{"test tag1", "test tag3"}));
		list.add(new Operation(2, new BigDecimal("200"),
                     "test description2", new String[]{"test tag1", "test tag2"}));
		list.add(new Operation(3, new BigDecimal("300"),
                     "test description3", new String[]{"test tag2", "test tag3"}));
		list.add(new Operation(4, new BigDecimal("-200"),
                     "test description4", new String[]{}));
 		return list;
	}	
	
	public void add(Operation object) {/*NON*/}
	
	public void update(Operation object) {/*NON*/}
	
	public void delete(Operation object) {/*NON*/}
}