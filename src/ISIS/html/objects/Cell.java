package ISIS.html.objects;

public class Cell extends HTMLTextContainer {
	protected final static Cell	cell	= null;
	protected final static Cell	header	= null;
	
	protected Cell() {
		super(Type.TABLE_CELL);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Cell add(HTMLObject object) {
		return (Cell) super.add(object);
	}
	
	public Cell setHeader() {
		type = "th";
		return this;
	}
	
	public Cell unsetHeader() {
		type = "td";
		return this;
	}
}
