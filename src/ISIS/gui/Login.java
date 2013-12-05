package ISIS.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import ISIS.session.Session;
import ISIS.user.AuthenticationException;

public class Login extends JFrame {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private HintField			username;
	private PasswordHintField	password;
	private JButton				submit, editUser, newUser;
	
	public Login() {
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.populateElements();
		this.getRootPane().setDefaultButton(this.submit);
		
		this.pack();
		this.setSize((int) (this.getWidth() * 1.10), this.getHeight());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void populateElements() {
		JPanel content = (JPanel) this.getContentPane();
		content.setLayout(new GridBagLayout());
		content.setBorder(new EmptyBorder(10, 5, 6, 5));
		GridBagConstraints c;
		int x = 0, y = 0;
		
		c = new GridBagConstraints();
		c.insets = new Insets(0, 7, 0, 0);
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		content.add(new JLabel("Username"), c);
		
		c = new GridBagConstraints();
		c.insets = new Insets(0, 7, 0, 7);
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		content.add(this.username = new HintField("Username"), c);
		
		c = new GridBagConstraints();
		c.insets = new Insets(0, 7, 0, 0);
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		content.add(new JLabel("Password"), c);
		
		c = new GridBagConstraints();
		c.insets = new Insets(0, 7, 0, 7);
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		content.add(this.password = new PasswordHintField(), c);
		
		JPanel buttons = new JPanel(new GridBagLayout());
		
		c = new GridBagConstraints();
		c.gridx = x = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		buttons.add(this.editUser = new JButton("Edit user"), c);
		
		c = new GridBagConstraints();
		c.gridx = ++x;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		buttons.add(this.newUser = new JButton("Add user"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = ++x;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		buttons.add(new JPanel(), c);
		
		c = new GridBagConstraints();
		c.gridx = ++x;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		buttons.add(this.submit = new JButton("Login"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		content.add(buttons, c);
		
		this.editUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Session.startNewSession(Login.this.username.getText(),
							new String(Login.this.password.getPassword()));
					java.awt.EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									AddEditUser panel = new AddEditUser(true,
											Login.this);
									JFrame frame = new JFrame();
									frame.setContentPane(panel);
									frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
									frame.pack();
									frame.setMinimumSize(new Dimension(500, 0));
									frame.setVisible(true);
									Login.this.dispose();
								}
							});
						}
					});
				} catch (SQLException e1) {
					ErrorLogger.error(e1, "Authentication failure", true, true);
				} catch (AuthenticationException e1) {
					ErrorLogger.error(e1, "Username or password incorrect.",
							false, true);
					if (e1.type == AuthenticationException.exceptionType.ACTIVE) {
						java.awt.EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										int result = JOptionPane
												.showConfirmDialog(
														Login.this,
														"Reactivate user?",
														"Reactivate",
														JOptionPane.YES_NO_OPTION);
										if (result == JOptionPane.YES_OPTION) {
											try {
												Session.activateUser(Login.this.username
														.getText());
											} catch (SQLException e2) {
												ErrorLogger
														.error(e2,
																"Failed to reactivate user",
																true, true);
											}
										}
									}
								});
							}
						});
					}
				}
			}
		});
		this.newUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Session.startNewSession(Login.this.username.getText(),
							new String(Login.this.password.getPassword()));
					java.awt.EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									AddEditUser panel = new AddEditUser(false,
											Login.this);
									JFrame frame = new JFrame();
									frame.setContentPane(panel);
									frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
									frame.pack();
									frame.setMinimumSize(new Dimension(500, 0));
									frame.setVisible(true);
									Login.this.setVisible(false);
								}
							});
						}
					});
				} catch (SQLException e1) {
					ErrorLogger.error(e1, "Authentication failure", true, true);
				} catch (AuthenticationException e1) {
					ErrorLogger.error(e1, "Username or password incorrect.",
							false, true);
				}
			}
		});
		this.submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Session.startNewSession(Login.this.username.getText(),
							new String(Login.this.password.getPassword()));
					java.awt.EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									MainWindow frame = new MainWindow();
									frame.setVisible(true);
									Login.this.dispose();
								}
							});
						}
					});
				} catch (SQLException e1) {
					ErrorLogger.error(e1, "Authentication failure", true, true);
				} catch (AuthenticationException e1) {
					ErrorLogger.error(e1, "Username or password incorrect.",
							false, true);
				}
			}
		});
	}
}
