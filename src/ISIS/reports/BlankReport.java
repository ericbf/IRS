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
	private static final String	names[]			= { "Eric", "Michael", "Keith",
			"J.P.", "Sirang"					};
	private static final String	descriptions[]	= { "Beautiful", "Magnificent",
			"Stud", "Cool", "Hot", "Sexy", "Loser", "Stupid", "Lame",
			"Annoying", "Undesirable", "Hateful", "Rude", "Ignorant",
			"Impolite", "Graceless", "Dirty", "Indelicate", "Nasty", "Harmful",
			"Bad", "Catastrophic", "Ruinous"	};
	
	/**
	 * @param title
	 */
	public BlankReport() {
		super("Blank Report");
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.reports.Report#populateBuilder()
	 */
	@Override
	public void populateBuilder() {
		Table t = new Table(2, BlankReport.names.length + 1);
		
		t.setHeaderRow(0);
		
		String titles[] = { "Name", "Description" };
		int i = 0;
		for (Cell c : t.getRow(0)) {
			c.add(titles[i++]);
		}
		
		Random rand = new Random();
		for (int row = 1; row < t.getRowCount(); row++) {
			t.get(0, row).add(BlankReport.names[row - 1]);
			int description;
			if (row - 1 == 0) {
				while ((description = rand
						.nextInt(BlankReport.descriptions.length)) > 5) {
					;
				}
			} else {
				description = rand.nextInt(BlankReport.descriptions.length);
			}
			t.get(1, row).add(BlankReport.descriptions[description]);
		}
		
		this.b.add(t);
	}
}
