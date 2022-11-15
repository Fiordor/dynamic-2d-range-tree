/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DynamicTreeStructures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Fiordor
 */
public class Canvas extends JPanel {

    private BufferedImage bufferedImage;
    
    private int x;
    private int y;
    private double zoom;

    public Canvas() {
        this(null);
    }

    public Canvas(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.zoom = 1.0;
    }

    public void setImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.x = 0;
        this.y = 0;
        this.zoom = 1.0;
    }
    
    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int[] getPoint() {
        return new int[]{this.x, this.y};
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (bufferedImage != null) {

            //g.drawImage(bufferedImage, 0, 0, null);
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(zoom, zoom);
            g2d.drawImage(bufferedImage, x, y, this);
        }
    }
}
