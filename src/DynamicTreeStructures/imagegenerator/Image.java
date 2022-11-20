package DynamicTreeStructures.imagegenerator;

import DynamicTreeStructures.interfaces.TreeImage;
import DynamicTreeStructures.structure.RedBlackTree;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Fiordor
 */
public class Image {

    private final Color BACKGROUND = new Color(200, 200, 200);

    private Font font;
    private ImageLabel[] labels;
    private ImageLine[] lines;

    private int minWidth;
    private int minHeight;

    private Color lastColor;

    public Image(TreeImage treeImage) {
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

    public BufferedImage create(int width, int height) {
        return this.create(width, height, 16);
    }

    public BufferedImage create(int width, int height, int margin) {

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(BACKGROUND);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);

        for (int i = 0; i < lines.length; i++) {
            drawLine(lines[i], g2d);
        }

        for (int i = 0; i < labels.length; i++) {
            drawString(labels[i], g2d);
        }

        g2d.setColor(Color.red);
        int[] bounds = cutImage(bufferedImage);
        if (bounds != null) {
            g2d.fillRect(0, 0, bounds[0], height);
            g2d.fillRect(bounds[1], 0, width - bounds[1], height);
        }

        /*
        g2d.fillRect(0, 0, fullWidth, margin);
        g2d.fillRect(0, fullHeight - margin, fullWidth, margin);
        g2d.fillRect(0, 0, margin, fullHeight);
        g2d.fillRect(fullWidth - margin, 0, margin, fullHeight);
         */
        return bufferedImage;
    }

    private int[] cutImage(BufferedImage base) {

        int left = -1;
        int right = -1;

        int middle = base.getWidth() / 2;
        int iLeft = 0, iRight = base.getWidth() - 1;
        while (iLeft < middle) {
            for (int j = 0; j < base.getHeight(); j++) {
                if (left == -1 && base.getRGB(iLeft, j) != BACKGROUND.getRGB()) {
                    left = iLeft;
                    System.out.println("left " + left);
                }

                if (right == -1 && base.getRGB(iRight, j) != BACKGROUND.getRGB()) {
                    right = iRight + 1;
                    System.out.println("right " + right);
                }

                if (left > 0 && right > 0) {
                    return new int[]{left, right};
                }
            }
            iLeft++;
            iRight--;
        }

        return null;
    }

    private void drawLine(ImageLine line, Graphics2D g2d) {
        if (lastColor != line.getC()) {
            lastColor = line.getC();
            g2d.setColor(lastColor);
        }
        g2d.drawLine(line.getX1(),line.getY1(),line.getX2(),line.getY2());
    }

    private void drawString(ImageLabel label, Graphics2D g2d) {

        int lbX = label.getX();
        int lbY = label.getY();
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
