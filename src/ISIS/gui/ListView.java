package ISIS.gui;

/**
 * Abstract class for views that consist of a list that can be searched.
 */
public abstract class ListView extends View {
    
    /**
     * This type of view needs not be saved.
     */
    @Override
    public boolean needsSave(){
        return false;
    }
}
