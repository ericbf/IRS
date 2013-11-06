package ISIS.gui;

/**
 * Abstract class for views that consist of a list that can be searched.
 */
public abstract class ListView extends View {
	private static final long	serialVersionUID	= 1L;
	
	public ListView(SplitPane splitPane) {
		super(splitPane);
	}
	
	/**
	 * This type of view needs not be saved.
	 */
	@Override
	public boolean needsSave() {
		return false;
	}
	
	/**
	 * Save is not supported.
	 */
	@Override
	public void save() {
		throw new UnsupportedOperationException("Not supported.");
	}
	
	/**
	 * Cancel is not supported.
	 */
	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Not supported.");
	}
}
