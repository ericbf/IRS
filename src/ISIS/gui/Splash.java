/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ISIS.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author michaelm
 */
public class Splash extends JPanel {
//    private BufferedImage image;
    public Image image;
    public Splash(URL url) {
        super();
        this.image = new ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
