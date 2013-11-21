package ISIS.html.objects;

public class Table extends HTMLObjectContainer<Table> {
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
			this.add(r);
		}
	}
	
	public Table addColumn() {
		for (HTMLObject<?> r : this.objects) {
			((Row) r).add(new Cell());
		}
		this.columns++;
		return this;
	}
	
	public Table addRow() {
		Row r = new Row();
		for (int i = 0; i < this.columns; i++) {
			r.add(new Cell());
		}
		this.rows++;
		return super.add(r);
	}
	
	public Cell get(int column, int row) {
		return (Cell) this.objects.get(row).objects.get(column);
	}
	
	public Cell[] getColumn(int column) {
		Cell temp[] = new Cell[this.rows];
		for (int row = 0; row < this.rows; row++) {
			temp[row] = (Cell) this.objects.get(row).objects.get(column);
		}
		return temp;
	}
	
	public int getColumnCount() {
		return this.columns;
	}
	
	public Cell[] getRow(int row) {
		Cell temp[] = new Cell[this.columns];
		Row row2 = (Row) this.objects.get(row);
		for (int column = 0; column < this.columns; column++) {
			temp[column] = (Cell) row2.objects.get(column);
		}
		return temp;
	}
	
	public int getRowCount() {
		return this.rows;
	}
	
	public Table setHeaderColumn(int column) {
		for (HTMLObject<?> c : this.objects) {
			((Cell) c.objects.get(column)).setHeader();
		}
		return this;
	}
	
	public Table setHeaderRow(int row) {
		for (HTMLObject<?> r : this.objects.get(row).objects) {
			((Cell) r).setHeader();
		}
		return this;
	}
	
	public Table unsetHeaderColumn(int column) {
		for (HTMLObject<?> c : this.objects) {
			((Cell) c.objects.get(column)).unsetHeader();
		}
		return this;
	}
	
	public Table unsetHeaderRow(int row) {
		for (HTMLObject<?> r : this.objects.get(row).objects) {
			((Cell) r).unsetHeader();
		}
		return this;
	}
}
