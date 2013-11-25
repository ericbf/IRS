/**
 * 
 */
package ISIS.reports;

import java.math.BigDecimal;
import java.sql.SQLException;

import ISIS.gui.ErrorLogger;
import ISIS.html.CSSStyle;
import ISIS.html.objects.Cell;
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
			
			this.b.add(new CSSStyle(".stamp")
					.pasteAttributes("margin-left:auto; "
							+ "text-align:center; " + "margin-right:auto; "
							+ "width:8.5em; " + "font-size:25px; "
							+ "font-weight:bold; " + "color:red; "
							+ "border:3px solid red; " + "padding:0; "
							+ "padding-top:.175em; " + "border-radius:10px; "
							+ "margin-top:.5em; " + "margin-bottom:.5em"));
			
			Paragraph par = new Paragraph();
			
			par.add("NON-FINALIZED");
			par.addClass("stamp");
			
			this.b.add(par);
		}
		
		try {
			Table cust = new Table(2, 1);
			Cell c[];
			int x;
			
			cust.setHeaderRow(0);
			
			c = cust.getRow(0);
			x = 0;
			c[x++].add("Field");
			c[x++].add("Value");
			
			c = cust.addRow();
			x = 0;
			c[x++].add("Customer Name");
			c[x++].add(this.transaction.getCustomer().getFirstName() + " "
					+ this.transaction.getCustomer().getLastName());
			
			c = cust.addRow();
			x = 0;
			c[x++].add("Shipping Address");
			c[x++].add("TODO" // TODO
			// this.transaction.getAddress().toString()
			);
			
			c = cust.addRow();
			x = 0;
			c[x++].add("Billing Address");
			c[x++].add("TODO" // TODO
			// this.transaction.getBilling().toString()
			);
			
			c = cust.addRow();
			x = 0;
			c[x++].add("Transaction Status");
			c[x++].add(this.transaction.getStatus().toString());
			
			cust.addAttribute("style", "margin-bottom:.5em");
			
			this.b.add(cust);
			
			Table t = new Table(4, 1);
			
			t.setHeaderRow(0);
			c = t.getRow(0);
			x = 0;
			c[x++].add("Name");
			c[x++].add("Price");
			c[x++].add("QTY");
			c[x++].add("Total");
			
			BigDecimal totals = new BigDecimal(0);
			
			for (TransactionLineItem i : this.transaction.getItems()) {
				BigDecimal total = i.getPrice().multiply(i.getQuantity());
				c = t.addRow();
				
				x = 0;
				c[x++].add(i.getItem().getName());
				c[x++].add(i.getPrice().toString());
				c[x++].add(i.getQuantity().toString());
				c[x++].add(String.format("$%.2f", total.floatValue() + .005f));
				totals = totals.add(total);
			}
			
			c = t.addRow();
			x = 0;
			c[x++].add("Total");
			x++;
			x++;
			c[x++].add(String.format("$%.2f", totals.floatValue() + .005f));
			
			this.b.add(t);
		} catch (SQLException e) {
			ErrorLogger.error("failed to get line items", false, true);
			e.printStackTrace();
		}
	}
}
