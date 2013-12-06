package ISIS.html.objects;

public class Cell extends HTMLTextContainer<Cell> implements
		HTMLContainObjects<Cell> {
	protected final static Cell	cell	= null;
	protected final static Cell	header	= null;
	
	protected Cell() {
		super(Type.TABLE_CELL);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.html.objects.HTMLContainObjects#add(ISIS.html.objects.Division)
	 */
	@Override
	public Cell add(Division division) {
		return super.add(division);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.html.objects.HTMLContainObjects#add(ISIS.html.objects.Table)
	 */
	@Override
	public Cell add(Table table) {
		return super.add(table);
	}
	
	/**
	 * @return
	 * @pre - none
	 * @post - Cell set as header
	 */
	public Cell setHeader() {
		this.type = Type.TABLE_HEADER;
		return this;
	}
	
	/**
	 * @return
	 * @pre - none
	 * @post - Cell unset as header
	 */
	public Cell unsetHeader() {
		this.type = Type.TABLE_CELL;
		return this;
	}
}
