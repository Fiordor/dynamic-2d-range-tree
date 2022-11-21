package DynamicTreeStructures.imagegenerator;

import DynamicTreeStructures.interfaces.TreeImage;
import DynamicTreeStructures.structure.RedBlackTree;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Fiordor
 */
public class Image {

    private final Color BACKGROUND = new Color(200, 200, 200);

    private String type;

    private Font font;
    private ImageLabel[] labels;
    private ImageLine[] lines;

    private int minWidth;
    private int minHeight;

    private Color lastColor;

    public Image(TreeImage treeImage, String type) {

        this.type = type;

        font = treeImage.getFont();
        labels = treeImage.getLabels();
        lines = treeImage.getLines();

        minWidth = treeImage.getWidth();
        minHeight = treeImage.getHeight();

        lastColor = Color.BLACK;
    }

    public BufferedImage create() {
        return this.create(minWidth, minHeight, 16);
    }

    public BufferedImage create(int margin) {
        return this.create(minWidth, minHeight, margin);
    }

    public BufferedImage create(int width, int height) {
        return this.create(width, height, 16);
    }

    public BufferedImage create(int width, int height, int margin) {

        int[] stringBounds = stringBounds(type);
        int bufferedImageWidth = minWidth + (margin * 2);
        int bufferedImageHeight = minHeight + (margin * 2) + stringBounds[1] + 16;

        int marginX = margin;
        int marginY = 16 + stringBounds[1] + margin;

        if (width > bufferedImageWidth) {
            marginX = (width - bufferedImageWidth) / 2;
            bufferedImageWidth = width;
        }

        if (stringBounds[0] > bufferedImageWidth) {
            marginX = (stringBounds[0] - bufferedImageWidth) / 2;
            bufferedImageWidth = stringBounds[0];
        }

        if (height > bufferedImageHeight) {
            marginY = height - bufferedImageHeight;
            bufferedImageHeight = height;
        }
        
        System.out.println(stringBounds[0]);
        System.out.println(minWidth);
        System.out.println(bufferedImageWidth);
        System.out.println(marginX);

        BufferedImage bufferedImage = new BufferedImage(bufferedImageWidth, bufferedImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(BACKGROUND);
        g2d.fillRect(0, 0, bufferedImageWidth, bufferedImageHeight);
        g2d.setColor(Color.BLACK);

        g2d.drawString(type, bufferedImageWidth / 2 - stringBounds[0] / 2, margin + stringBounds[1]);

        for (int i = 0; i < lines.length; i++) {
            drawLine(lines[i], marginX, marginY, g2d);
        }

        for (int i = 0; i < labels.length; i++) {
            drawString(labels[i], marginX, marginY, g2d);
        }

        return bufferedImage;
    }

    private void drawLine(ImageLine line, int marginX, int marginY, Graphics2D g2d) {
        if (lastColor != line.getC()) {
            lastColor = line.getC();
            g2d.setColor(lastColor);
        }
        g2d.drawLine(
                line.getX1() + marginX,
                line.getY1() + marginY,
                line.getX2() + marginX,
                line.getY2() + marginY);
    }

    private void drawString(ImageLabel label, int marginX, int marginY, Graphics2D g2d) {

        int lbX = label.getX() + marginX;
        int lbY = label.getY() + marginY;
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

    private int[] stringBounds(String text) {

        BufferedImage calcBoundsImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D calcBoundsG2D = calcBoundsImg.createGraphics();
        FontMetrics calcBoundsFontMetrics = calcBoundsG2D.getFontMetrics(this.font);

        Rectangle2D bounds = calcBoundsFontMetrics.getStringBounds(text, calcBoundsG2D);
        double rawWidth = bounds.getWidth();
        double rawHeight = bounds.getHeight();

        int width = rawWidth % 1 == 0 ? (int) rawWidth : (int) rawWidth + 1;
        int height = rawHeight % 1 == 0 ? (int) rawHeight : (int) rawHeight + 1;

        return new int[]{width, height};
    }
}
