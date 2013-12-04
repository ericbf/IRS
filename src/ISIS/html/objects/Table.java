package ISIS.html.objects;

public class Table extends HTMLObjectContainer<Table> {
	int	columns, rows;
	
        /**
         * Public constructor: new Table instance
         * @param columns
         * @param rows 
         */
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
	
        /**
         * @pre - none
         * @post - column added to table
         * @return 
         */
	public Cell[] addColumn() {
		for (HTMLObject<?> r : this.objects) {
			((Row) r).add(new Cell());
		}
		return this.getColumn(this.columns++);
	}
	
	/**
         * @pre - none
         * @post - Row added to Table
	 * @return The added row
	 */
	public Cell[] addRow() {
		Row r = new Row();
		for (int i = 0; i < this.columns; i++) {
			r.add(new Cell());
		}
		super.add(r);
		return this.getRow(this.rows++);
	}
	
        /**
         * @pre - none
         * @post - gets cell
         * @param column
         * @param row
         * @return 
         */
	public Cell get(int column, int row) {
		return (Cell) this.objects.get(row).objects.get(column);
	}
	
        /**
         * @pre - none
         * @post - gets column
         * @param column
         * @return 
         */
	public Cell[] getColumn(int column) {
		Cell temp[] = new Cell[this.rows];
		for (int row = 0; row < this.rows; row++) {
			temp[row] = (Cell) this.objects.get(row).objects.get(column);
		}
		return temp;
	}
	
        /**
         * @pre - none
         * @post - column count
         * @return 
         */
	public int getColumnCount() {
		return this.columns;
	}
	
        /**
         * @pre - none
         * @post - gets row
         * @param row
         * @return 
         */
	public Cell[] getRow(int row) {
		Cell temp[] = new Cell[this.columns];
		Row row2 = (Row) this.objects.get(row);
		for (int column = 0; column < this.columns; column++) {
			temp[column] = (Cell) row2.objects.get(column);
		}
		return temp;
	}
	
        /**
         * @pre none
         * @post gets row count
         * @return 
         */
	public int getRowCount() {
		return this.rows;
	}
	
        /**
         * @pre none
         * @post header column set
         * @param column
         * @return 
         */
	public Table setHeaderColumn(int column) {
		for (HTMLObject<?> c : this.objects) {
			((Cell) c.objects.get(column)).setHeader();
		}
		return this;
	}
	
        /**
         * @pre none
         * @post header row set
         * @param row
         * @return 
         */
	public Table setHeaderRow(int row) {
		for (HTMLObject<?> r : this.objects.get(row).objects) {
			((Cell) r).setHeader();
		}
		return this;
	}
	
        /**
         * @pre none
         * @post header column unset
         * @param column
         * @return 
         */
	public Table unsetHeaderColumn(int column) {
		for (HTMLObject<?> c : this.objects) {
			((Cell) c.objects.get(column)).unsetHeader();
		}
		return this;
	}
	
        /**
         * @pre none
         * @post header row unset
         * @param row
         * @return 
         */
	public Table unsetHeaderRow(int row) {
		for (HTMLObject<?> r : this.objects.get(row).objects) {
			((Cell) r).unsetHeader();
		}
		return this;
	}
}
