package kazarin.my_money.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Operations implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Operations instance;
	private List<Operation> list;

	private Operations(){
		list = new ArrayList<>();
	}

	public static Operations getInstance(){
		if (instance == null) {
			instance = new Operations();
		}
		return instance;
	}

	public void add(Operation operation){
		list.add(operation);
	}

	public List getList(){
		return list;
	}

	@Override
	public int hashCode(){
		int code = 0;
		if (list != null) code = list.hashCode();		
		return code;
	}

	@Override
	public boolean equals(Object obj){
		if (this == null) return false;

		if (obj == null || getClass() != obj.getClass()) return false;	
		
		Operations ops = (Operations) obj;

		if (list != null ? !list.equals(ops.getList()) : ops.getList() != null) return false;

		return true;
	}
}