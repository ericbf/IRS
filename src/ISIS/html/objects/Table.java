package ISIS.html.objects;

public class Table extends HTMLObject {
	int	columns, rows;
	
	public Table(int columns, int rows) {
		super(Type.TABLE);
		this.rows = rows;
		this.columns = columns;
		for (int i = 0; i < rows; i++) {
			Row r = new Row();
			for (int j = 0; j < columns; j++) {
				r.add(new Cell());
			}
			add(r);
		}
	}
	
	public Table addColumn() {
		for (HTMLObject r : objects) {
			((Row) r).add(new Cell());
		}
		return this;
	}
	
	public Table addRow() {
		Row r = new Row();
		for (int i = 0; i < columns; i++)
			r.add(new Cell());
		return (Table) super.add(r);
	}
	
	public Cell get(int column, int row) {
		return (Cell) objects.get(row).objects.get(column);
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getRows() {
		return rows;
	}
	
	public Table setHeaderColumn(int column) {
		for (HTMLObject c : objects)
			((Cell) c.objects.get(column)).setHeader();
		return this;
	}
	
	public Table setHeaderRow(int row) {
		for (HTMLObject r : objects.get(row).objects)
			((Cell) r).setHeader();
		return this;
	}
	
	public Table unsetHeaderColumn(int column) {
		for (HTMLObject c : objects)
			((Cell) c.objects.get(column)).unsetHeader();
		return this;
	}
	
	public Table unsetHeaderRow(int row) {
		for (HTMLObject r : objects.get(row).objects)
			((Cell) r).unsetHeader();
		return this;
	}
}
