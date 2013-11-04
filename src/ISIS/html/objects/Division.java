package ISIS.html.objects;

public class Division extends HTMLObject {
	
	public Division() {
		super(Type.DIVISION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Division add(HTMLObject object) {
		return (Division) super.add(object);
	}
}
