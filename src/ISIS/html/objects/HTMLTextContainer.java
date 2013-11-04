package ISIS.html.objects;

public class HTMLTextContainer extends HTMLObject {
	
	protected HTMLTextContainer(Type type) {
		super(type);
	}
	
	public HTMLTextContainer add(HTMLTextContainer object) {
		return (HTMLTextContainer) super.add(object);
	}
	
	public HTMLTextContainer add(String str) {
		objects.add(new HTMLObject(str));
		return this;
	}
}
