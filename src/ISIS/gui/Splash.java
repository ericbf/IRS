package ISIS.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author michaelm
 */
public class Splash extends JPanel {
	private static final long	serialVersionUID	= 1L;
	// private BufferedImage image;
	public Image				image;
	
	public Splash(URL url) {
		super();
		this.image = new ImageIcon(url).getImage();
	}
	
	/*
	 * @pre - Gaben graphic object received
	 * @post - splash screen displayed, All Hail Gaben
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
