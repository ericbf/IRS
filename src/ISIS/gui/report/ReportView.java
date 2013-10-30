package ISIS.gui.report;

import ISIS.gui.View;

/**
 *
 * Abstract class for the report GUIs.
 */
public abstract class ReportView extends View {

	/**
	 * Public constructor.
	 */
	public ReportView() {
	}

	/**
	 * Reports are not saved, but regenerated as necessary.
	 */
	@Override
	public boolean needsSave() {
		return false;
	}

	/**
	 * Not supported.
	 */
	@Override
	public void save() {
		throw new UnsupportedOperationException("Not supported.");
	}

	/**
	 * Not supported.
	 */
	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Not supported.");

	}
}
