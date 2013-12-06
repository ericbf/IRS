package ISIS.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import ISIS.database.Record;
import ISIS.session.Session;
import ISIS.user.User;

public class AddEditUser extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private User				user				= null;
	private HintField			username, fname, lname, note;
	private JPasswordField		password;
	private JCheckBox			active;
	private final Login			loginWindow;
	JButton						save, cancel;
	
	public AddEditUser(boolean editMode, Login loginWindow) {
		super(null);
		this.populateElements();
		this.loginWindow = loginWindow;
		
		if (editMode) {
			this.disableFields(this.username, this.fname, this.lname);
			this.user = Session.getCurrentSession().getUser();
			this.username.setText(this.user.getUsername());
			this.fname.setText(this.user.getFirstName());
			this.lname.setText(this.user.getLastName());
			this.note.setText(this.user.getNote());
		}
	}
	
	private void backToLogin() {
		this.getParent().setVisible(false);
		((JFrame) this.getRootPane().getParent()).dispose();
		Session.endCurrentSession();
		this.loginWindow.setVisible(true);
	}
	
	/**
	 * Discards any modifications.
	 */
	@Override
	public void cancel() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
	 */
	@Override
	public Record getCurrentRecord() {
		if (!this.isAnyFieldDifferentFromDefault()) {
			return null;
		}
		if (this.user == null) {
			if (this.username.isEmpty()) {
				ErrorLogger.error("No username.", false, true);
				return null;
			}
			this.user = new User(this.username.getText(),
					this.active.isSelected(), new String(
							this.password.getPassword()), this.fname.getText(),
					this.lname.getText(), this.note.getText());
			this.disableFields(this.username, this.fname, this.lname);
		} else {
			this.user.setActive(this.active.isSelected());
			this.user.setNote(this.note.getText());
			if (this.password.getPassword().length > 0) {
				this.user.setPassword(new String(this.password.getPassword()));
			}
		}
		return this.user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		boolean same = this.active.isSelected() && this.username.isEmpty()
				&& this.password.getPassword().length == 0
				&& this.fname.isEmpty() && this.lname.isEmpty()
				&& this.note.isEmpty();
		return !same;
	}
	
	/**
	 * Draws all necessary components on the window.
	 */
	private void populateElements() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		int x = 0, y = 0;
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditUser.this.backToLogin();
			}
		});
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(cancel, c);
		
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					AddEditUser.this.save();
					AddEditUser.this.backToLogin();
				} catch (SQLException e1) {
					ErrorLogger.error(e1, "Failed to save the user.", true,
							true);
				}
			}
		});
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(save, c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Active"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.active = new JCheckBox("", true), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Username"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.username = new HintField("Username")).make(), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Password"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.password = new JPasswordField(), c); // shown in
															// plaintext, but
															// not stored in
															// plaintext.
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("First Name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.fname = new HintField("First Name")).make(), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Last Name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.lname = new HintField("Last Name")).make(), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Note"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.note = new HintField("Note")).make(), c);
	}
	
	@Override
	protected void postSave() throws SQLException {}
	
}
