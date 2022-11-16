
package DynamicTreeStructures.imagegenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Fiordor
 */
public class Image {

    private Font font;
    private ImageLabel[] labels;
    private ImageLine[] lines;

    private int minWidth;
    private int minHeight;

    private Color lastColor;

    public Image(ImageRedBlackTree redBlackTree) {
        font = redBlackTree.getFont();
        labels = redBlackTree.getLabels();
        lines = redBlackTree.getLines();

        minWidth = redBlackTree.getWidth();
        minHeight = redBlackTree.getHeight();

        lastColor = Color.BLACK;
    }

    public BufferedImage create() {
        return this.create(minWidth, minHeight, 16);
    }

    public BufferedImage create(int width, int height) {
        return this.create(width, height, 16);
    }

    public BufferedImage create(int width, int height, int margin) {

        int fullWidth = width + margin * 2;
        int fullHeight = height + margin * 2;

        BufferedImage bufferedImage = new BufferedImage(fullWidth, fullHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, fullWidth, fullHeight);
        g2d.setColor(Color.BLACK);

        for (int i = 0; i < lines.length; i++) {
            drawLine(lines[i], margin, g2d);
        }

        for (int i = 0; i < labels.length; i++) {
            drawString(labels[i], margin, g2d);
        }

        return bufferedImage;
    }

    private void drawLine(ImageLine line, int margin, Graphics2D g2d) {
        if (lastColor != line.getC()) {
            lastColor = line.getC();
            g2d.setColor(lastColor);
        }
        g2d.drawLine(
                line.getX1() + margin,
                line.getY1() + margin,
                line.getX2() + margin,
                line.getY2() + margin);
    }

    private void drawString(ImageLabel label, int margin, Graphics2D g2d) {

        int lbX = label.getX() + margin;
        int lbY = label.getY() + margin;
        int bgX = lbX;
        int bgY = lbY - label.getH();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(bgX, bgY, label.getW(), label.getH());

        if (lastColor != label.getC()) {
            lastColor = label.getC();
        }
        g2d.setColor(lastColor);
        g2d.drawString(label.getLabel(), lbX, lbY);
    }
}
