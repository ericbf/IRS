/**
 * 
 */
package ISIS.html.objects;

/**
 * @author eric
 * @param <E>
 */
public abstract class HTMLObjectContainer<E> extends HTMLObject<E> implements
		HTMLContainObjects<E> {
	
	protected HTMLObjectContainer(Type type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public E add(Division division) {
		return super.add(division);
	}
	
	@Override
	public E add(Header header) {
		return super.add(header);
	}
	
	@Override
	public E add(Paragraph paragraph) {
		return super.add(paragraph);
	}
	
	@Override
	public E add(Table table) {
		return super.add(table);
	}
}
