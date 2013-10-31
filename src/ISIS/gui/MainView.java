package ISIS.gui;

/**
 * The main view, used directly in the MainWindow
 */
public class MainView extends View {

    public MainView() {
    }

    @Override
    public boolean needsSave() {
	return false;
    }

    @Override
    public void save() {
	throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void cancel() {
	throw new UnsupportedOperationException("Not supported.");
    }
}
