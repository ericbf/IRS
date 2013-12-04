package ISIS.html.objects;

public class Row extends HTMLObjectContainer<Row> {
	protected Row() {
		super(Type.TABLE_ROW);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * @pre - none
	 * @post - cell added to row
	 */
	public Row add(Cell cell) {
		return super.add(cell);
	}
}
