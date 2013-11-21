/**
 * 
 */
package ISIS.reports;

import java.util.Random;

import ISIS.html.objects.Cell;
import ISIS.html.objects.Table;

/**
 * @author eric
 */
public class BlankReport extends Report {
	private final String	names[]			= { "Eric", "Michael", "Keith",
			"J.P.", "Sirang"				};
	private final String	descriptions[]	= { "Beautiful", "Magnificent",
			"Stud", "Cool", "Hot", "Sexy", "Loser", "Stupid", "Lame",
			"Annoying", "Undesirable", "Hateful", "Rude", "Ignorant", "Mean",
			"Impolite", "Vulgar", "Evil", "Cruel", "Insolent", "Graceless",
			"Dirty", "Indelicate", "Nasty", "Harmful", "Bad", "Catastrophic",
			"Ruinous"						};
	
	/**
	 * @param title
	 */
	public BlankReport() {
		super("Blank Report");
		Table t = new Table(2, this.names.length + 1);
		
		t.setHeaderRow(0);
		
		String titles[] = { "Name", "Description" };
		int i = 0;
		for (Cell c : t.getRow(0)) {
			c.add(titles[i++]);
		}
		
		Random rand = new Random();
		for (int row = 1; row < t.getRowCount(); row++) {
			t.get(0, row).add(this.names[row - 1]);
			int description;
			if (row - 1 == 0) while ((description = rand
					.nextInt(this.descriptions.length)) > 5)
				;
			else description = rand.nextInt(this.descriptions.length);
			t.get(1, row).add(this.descriptions[description]);
		}
		
		this.b.add(t);
		
	}
}
