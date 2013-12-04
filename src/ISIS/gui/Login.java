package ISIS.gui;

import ISIS.session.Session;
import ISIS.user.AuthenticationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Login extends JFrame {
    private HintField username;
    private JPasswordField password;
    private JButton submit, editUser, newUser;

    public Login() {
        this.populateElements();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.pack();
        this.setMinimumSize(new Dimension(300, 0));
        this.setLocation(
                (int) (dimension.getWidth() - this.getWidth()) / 2,
                (int) (dimension.getHeight() - this.getHeight()) / 2);

        this.getRootPane().setDefaultButton(this.submit);
        this.setVisible(true);
    }

    private void populateElements() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;
        int x = 0, y = 0;

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
        c.fill = GridBagConstraints.BOTH;
        this.add(this.username = new HintField("Username"), c);

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
        c.fill = GridBagConstraints.BOTH;
        this.add(this.password = new JPasswordField(), c);

        this.submit = new JButton("Login");
        this.submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Session.startNewSession(Login.this.username.getText(), new String(Login.this.password.getPassword()));
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    MainWindow frame = new MainWindow();
                                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    frame.pack();
                                    frame.setMinimumSize(new Dimension(800, 400));
                                    frame.setVisible(true);
                                    Login.this.setVisible(false);
                                    Login.this.dispose();
                                }
                            });
                        }
                    });
                } catch (SQLException e1) {
                    ErrorLogger.error(e1, "Authentication failure", true, true);
                } catch (AuthenticationException e1) {
                    ErrorLogger.error(e1, "Username or password incorrect.", false, true);
                }
            }
        });

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x;
        c.gridy = y++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.submit, c);

        this.editUser = new JButton("Edit user");
        this.editUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Session.startNewSession(Login.this.username.getText(), new String(Login.this.password.getPassword()));
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    AddEditUser panel = new AddEditUser(true, Login.this);
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
                    ErrorLogger.error(e1, "Username or password incorrect.", false, true);
                    if(e1.type == AuthenticationException.exceptionType.ACTIVE) {
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        int result = JOptionPane.showConfirmDialog(Login.this, "Reactivate user?",
                                                                                   "Reactivate", JOptionPane.YES_NO_OPTION);
                                        if (result == JOptionPane.YES_OPTION) {
                                            try {
                                                Session.activateUser(Login.this.username.getText());
                                            } catch (SQLException e2) {
                                                ErrorLogger.error(e2, "Failed to reactivate user", true, true);
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

        c = new GridBagConstraints();
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.editUser, c);

        this.newUser = new JButton("Add user");
        this.newUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Session.startNewSession(Login.this.username.getText(), new String(Login.this.password.getPassword()));
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    AddEditUser panel = new AddEditUser(false, Login.this);
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
                    ErrorLogger.error(e1, "Username or password incorrect.", false, true);
                }
            }
        });

        c = new GridBagConstraints();
        c.gridx = x--;
        c.gridy = y++;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.newUser, c);
    }
}
