package com.fiordor.dynamic2drangetree.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class Image {

    public static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    public static final String FORMAT = "png";
    
    /**
     * Create image in Base64 with html format, in png and white color
     * 
     * @param text String to print on the picture
     * @return Image converted to html Base64
     */
    public static String create(String text) {
        return create(text, true, FORMAT, Color.WHITE);
    }

    /**
     * Create image.
     * 
     * @param text String to print on the picture
     * @param html true for the return is ready to print in html file
     * @param format format of image
     * @param color color to print data
     * @return Image converted to Base64
     */
    public static String create(String text, boolean html, String format, Color color) {

		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(FONT);
		g2d.setColor(color);

		int width = stringWidth(text);
		int height = stringHeight(text);

        int x = (500 / 2) - (width / 2);
        int y = (500 / 2) - (height / 2);

        g2d.drawString(text, x, y);

		String imgBase64 = "";
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, format, byteArray);
			imgBase64 = Base64.getEncoder().encodeToString(byteArray.toByteArray());
		} catch (IOException e) {
			imgBase64 = e.getMessage();
		}

		return html ? "data:image/" + format + ";base64," + imgBase64 : imgBase64;

    }

    public static String create(ImageLabel[] labels, ImageLine[] lines, int width, int height) {

        final int MARGIN = 16;

		BufferedImage bufferedImage = new BufferedImage(width + MARGIN * 2, height + MARGIN * 2, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(FONT);
		g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width + MARGIN * 2, height + MARGIN * 2);
        g2d.setColor(Color.BLACK);

        g2d.setColor(Color.BLACK);
        for (int i = 0; i < lines.length; i++) {
            drawLine(lines[i], MARGIN, g2d);
        }

        for (int i = 0; i < labels.length; i++) {
            drawString(labels[i], Color.BLACK, Color.WHITE, MARGIN, g2d);
        }
        
		String imgBase64 = "";
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, FORMAT, byteArray);
			imgBase64 = Base64.getEncoder().encodeToString(byteArray.toByteArray());
		} catch (IOException e) {
			imgBase64 = e.getMessage();
		}

		return "data:image/" + FORMAT + ";base64," + imgBase64;

    }

    public static int stringHeight(String text) {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
        FontMetrics fontMetrics = g2d.getFontMetrics(FONT);
		Rectangle2D bounds = fontMetrics.getStringBounds(text, g2d);
        double raw = bounds.getHeight();
        return raw % 1 == 0 ? (int)raw : (int)raw + 1;
    }

    public static int stringWidth(String text) {

        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
        FontMetrics fontMetrics = g2d.getFontMetrics(FONT);
		Rectangle2D bounds = fontMetrics.getStringBounds(text, g2d);
        double raw = bounds.getWidth();
        return raw % 1 == 0 ? (int)raw : (int)raw + 1;
    }

    private static void drawLine(ImageLine line, int margin, Graphics2D g2d) {
        g2d.drawLine( line.x1() + margin, line.y1() + margin, line.x2() + margin, line.y2() + margin );
    }

    private static void drawString(ImageLabel label, Color c, Color bg, int margin, Graphics2D g2d) {

        int labelX = label.x() + margin;
        int labelY = label.y() + margin;

        g2d.setColor(bg);
        g2d.fillRect(labelX, labelY - label.h(), label.w(), label.h());
        g2d.setColor(c);
        g2d.drawString(label.label(), labelX, labelY);
    }
}
