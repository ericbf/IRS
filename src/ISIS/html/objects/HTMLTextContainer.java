package ISIS.html.objects;

public abstract class HTMLTextContainer<E> extends HTMLObject<E> implements
		HTMLContainText<E> {
	
	protected HTMLTextContainer(Type type) {
		super(type);
	}
	
	@Override
	public E add(Bold bold) {
		return super.add(bold);
	}
	
	@Override
	public E add(Break br) {
		return super.add(br);
	}
	
	@Override
	public E add(Header header) {
		return super.add(header);
	}
	
	@Override
	public E add(Italic italic) {
		return super.add(italic);
	}
	
	@Override
	public E add(Paragraph paragraph) {
		return super.add(paragraph);
	}
	
	@Override
	public E add(Quote quote) {
		return super.add(quote);
	}
	
	@Override
	public E add(Separator hr) {
		return super.add(hr);
	}
	
	@Override
	public E add(String str) {
		return super.add(new HTMLObject<HTMLObject<?>>(str));
	}
	
}
