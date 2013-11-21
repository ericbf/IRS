/**
 * 
 */
package ISIS.html.objects;

/**
 * @author eric
 */
public interface HTMLContainObjects<E> {
	public E add(Division division);
	
	public E add(Header header);
	
	public E add(Paragraph paragraph);
	
	public E add(Table table);
}
