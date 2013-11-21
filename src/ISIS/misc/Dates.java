package ISIS.misc;

import java.util.Date;

import ISIS.session.Session;
import ISIS.user.User;

/**
 * Generic class for various date information that is not typically set or
 * modified explicitly by the user. The fields are modification date/user and
 * creation date/user. All fields are final. Invariants: All fields are
 * populated.
 * 
 * @modDate != null && modBy != null && createdDate != null && createdBy != null
 */
public class Dates {
	
	private Date	createDate;
	private User	createUser;
	private Date	modDate;
	private User	modUser;
	private boolean	dateChanged	= false;	// means the date needs to be saved
											
	/**
	 * Public constructor. Convenience method that uses the current time and
	 * logged in user.
	 */
	public Dates() {
		this.createDate = new Date();
		this.createUser = Session.getCurrentSession().getUser();
		this.modDate = new Date();
		this.modUser = Session.getCurrentSession().getUser();
		this.dateChanged = true;
	}
	
	/**
	 * Public constructor. Meant to populate a date from the database.
	 */
	public Dates(Date createdDate, User createdBy, Date modDate, User modUser) {
		this.createDate = createdDate;
		this.createUser = createdBy;
		this.modDate = modDate;
		this.modUser = modUser;
	}
	
	/**
	 * Gets the user that created the object referencing this instance.
	 */
	public User getCreatedBy() {
		return this.createUser;
	}
	
	/**
	 * Gets the creation date of the object referencing this instance.
	 */
	public Date getCreatedDate() {
		return this.createDate;
	}
	
	/**
	 * Gets the user that modified the object referencing this instance.
	 */
	public User getModBy() {
		return this.modUser;
	}
	
	/**
	 * Gets the modification date of the object referencing this instance.
	 */
	public Date getModDate() {
		return this.modDate;
	}
	
	/**
	 * Checks whether the date needs to be saved.
	 */
	public boolean modified() {
		return this.dateChanged;
	}
	
	/**
	 * Sets the modification date to now, and the modification user to the user
	 * currently logged in.
	 */
	public void modify() {
		this.modDate = new Date();
		this.modUser = Session.getCurrentSession().getUser();
		this.dateChanged = true;
	}
}
