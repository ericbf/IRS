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
public class TeamMemberReport extends Report {
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
	public TeamMemberReport() {
		super("ISIS Member");
		this.populateBuilder();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.reports.Report#populateBuilder()
	 */
	@Override
	public void populateBuilder() {
		Table t = new Table(2, TeamMemberReport.names.length + 1);
		
		t.setHeaderRow(0);
		
		String titles[] = { "Name", "Description" };
		int i = 0;
		for (Cell c : t.getRow(0)) {
			c.add(titles[i++]);
		}
		
		Random rand = new Random();
		for (int row = 1; row < t.getRowCount(); row++) {
			t.get(0, row).add(TeamMemberReport.names[row - 1]);
			int description;
			if (row == 1) {
				while ((description = rand
						.nextInt(TeamMemberReport.descriptions.length)) > 5) {
					;
				}
			} else {
				description = rand
						.nextInt(TeamMemberReport.descriptions.length);
			}
			t.get(1, row).add(TeamMemberReport.descriptions[description]);
		}
		
		this.b.add(t);
	}
}
