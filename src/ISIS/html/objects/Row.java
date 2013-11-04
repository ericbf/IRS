package ISIS.html.objects;

public class Row extends HTMLObject {
	
	protected Row() {
		super(Type.TABLE_ROW);
		// TODO Auto-generated constructor stub
	}
	
	public Row add(Cell object) {
		super.add(object);
		return this;
	}
}
