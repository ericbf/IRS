/**
 * 
 */
package ISIS.html.objects;

/**
 * @author eric
 */
public interface HTMLContainText<E> {
	public E add(Bold bold);
	
	public E add(Break br);
	
	public E add(Header header);
	
	public E add(Italic italic);
	
	public E add(Paragraph paragraph);
	
	public E add(Quote quote);
	
	public E add(Separator hr);
	
	public E add(String str);
}
