package ISIS.misc;

import ISIS.user.User;
import java.util.Date;

/**
 * Generic class for various date information that is not typically set or
 * modified explicitly by the user. The fields are modification date/user and
 * creation date/user. All fields are final.
 *
 * Invariants:
 *
 * All fields are populated.
 *
 * @modDate != null && modBy != null && createdDate != null && createdBy != null
 */
public class Dates {
	/* Fields omitted */

	/**
	 * Public constructor. Meant for initial instantiation of a Dates object.
	 * Modification date and user are set to the same value as the creation date
	 * and user.
	 */
	public Dates(Date createdDate, User createdBy) {
	}

	/**
	 * Public constructor. Meant for modifying an existing instance of a Dates
	 * object.
	 */
	public Dates(Dates modifiedDate, Date modDate, User modBy) {
	}

	/**
	 * Gets the creation date of the object referencing this instance.
	 */
	public Date getCreatedDate() {
	}

	/**
	 * Gets the user that created the object referencing this instance.
	 */
	public User getCreatedBy() {
	}

	/**
	 * Gets the modification date of the object referencing this instance.
	 */
	public Date getModDate() {
	}

	/**
	 * Gets the user that modified the object referencing this instance.
	 */
	public User getModBy() {
	}
}
