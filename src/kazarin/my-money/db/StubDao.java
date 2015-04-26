/*
 * StubDao for tests.
 */
package kazarin.my_money.db;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

class StubDao implements Dao<Object> {
	public List<Object> getAll() {
		return null;
	}

	public void add(Object object) {/*NON*/}
	
	public void update(Object object) {/*NON*/}
	
	public void delete(Object object) {/*NON*/}

	public void createDB(String dbName) {/*NON*/}
}