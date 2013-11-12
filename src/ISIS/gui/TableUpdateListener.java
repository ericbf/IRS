package ISIS.gui;

import javax.swing.*;

public abstract class TableUpdateListener extends AbstractAction {

    public TableUpdateListener() {
        super();
    }

    public void setKey(Integer key) {
        this.putValue("pkey", key);
    }

    protected int getPkey() {
        return ((Integer) this.getValue("pkey"));
    }
}
