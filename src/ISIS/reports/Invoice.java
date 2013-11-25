/**
 * 
 */
package ISIS.reports;

import java.math.BigDecimal;
import java.sql.SQLException;

import ISIS.gui.ErrorLogger;
import ISIS.html.CSSStyle;
import ISIS.html.objects.Cell;
import ISIS.html.objects.Division;
import ISIS.html.objects.Paragraph;
import ISIS.html.objects.Table;
import ISIS.transaction.Transaction;
import ISIS.transaction.Transaction.TransactionStatus;
import ISIS.transaction.TransactionLineItem;

/**
 * @author eric
 */
public class Invoice extends Report {
	Transaction	transaction;
	
	/**
	 * @param title
	 */
	public Invoice(Transaction transaction) {
		super("Invoice");
		this.transaction = transaction;
		this.populateBuilder();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.reports.Report#populateBuilder()
	 */
	@Override
	public void populateBuilder() {
		if (this.transaction.getStatus() == TransactionStatus.ACTIVE) {
			this.b.add(new CSSStyle("@font-face")
					.pasteAttributes("font-family:'Vollkorn'; "
							+ "font-style: normal; "
							+ "font-weight: 700; "
							+ "src: url('ISIS/misc/Vollkorn.woff') format('woff');"));
			
			this.b.add(new CSSStyle(".stamp")
					.pasteAttributes("font-family:'Vollkorn', serif; "
							+ "font-size:45px; " + "line-height:45px; "
							+ "text-transform:uppercase; "
							+ "font-weight:bold; " + "color:red; "
							+ "border:7px solid red; " + "float:left; "
							+ "padding:10px 7px; " + "border-radius:10px; "
							+ "position:relative; " + "opacity:0.8; "
							+ "-webkit-transform:rotate(-10deg); "
							+ "-o-transform:rotate(-10deg); "
							+ "-moz-transform:rotate(-10deg); "
							+ "-ms-transform:rotate(-10deg);"));
			this.b.add(new CSSStyle(".stamp:after")
					.pasteAttributes("position: absolute; " + "content:\" \"; "
							+ "width:100%; " + "height:auto; "
							+ "min-height:100%; " + "top:-10px; "
							+ "left:-10px; " + "padding:10px; "
							+ "background:url(ISIS/misc/noise.png) repeat;"));
			
			Division d = new Division();
			
			d.add(new Paragraph().add("Non-finalized"));
			d.addClass("stamp");
			
			this.b.add(d);
		}
		
		try {
			Table t = new Table(4, 1);
			
			Cell c[] = t.getRow(0);
			int x = 0;
			c[x++].add("Name");
			c[x++].add("Price");
			c[x++].add("QTY");
			c[x++].add("Total");
			
			BigDecimal prices = new BigDecimal(0);
			BigDecimal qtys = new BigDecimal(0);
			BigDecimal totals = new BigDecimal(0);
			
			for (TransactionLineItem i : this.transaction.getItems()) {
				BigDecimal total = i.getPrice().multiply(i.getQuantity());
				c = t.addRow();
				
				x = 0;
				c[x++].add(i.getItem().getName());
				c[x++].add(i.getPrice().toString());
				c[x++].add(i.getQuantity().toString());
				c[x++].add(String.format("$%.2f", total.floatValue() + .005f));
				prices = prices.add(i.getPrice());
				qtys = qtys.add(i.getQuantity());
				totals = totals.add(total);
			}
			
			c = t.addRow();
			x = 0;
			c[x++].add("All totals");
			c[x++].add(prices.toString());
			c[x++].add(qtys.toString());
			c[x++].add(String.format("$%.2f", totals.floatValue() + .005f));
			
			this.b.add(t);
		} catch (SQLException e) {
			ErrorLogger.error("failed to get line items", false, true);
			e.printStackTrace();
		}
	}
}
