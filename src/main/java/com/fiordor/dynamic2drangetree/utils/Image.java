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
    
    /**
     * Create image in Base64 with html format, in png and white color
     * 
     * @param text String to print on the picture
     * @return Image converted to html Base64
     */
    public static String create(String text) {
        return create(text, true, "png", Color.WHITE);
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
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        g2d.setFont(font);
		g2d.setColor(color);

		int width = stringWidth(text, g2d, font);
		int height = stringHeight(text, g2d, font);

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

    private static int stringHeight(String text, Graphics2D g2d, Font font) {
        FontMetrics fontMetrics = g2d.getFontMetrics(font);
		Rectangle2D bounds = fontMetrics.getStringBounds(text, g2d);
        double raw = bounds.getHeight();
        return raw % 1 == 0 ? (int)raw : (int)raw + 1;
    }

    private static int stringWidth(String text, Graphics2D g2d, Font font) {
        FontMetrics fontMetrics = g2d.getFontMetrics(font);
		Rectangle2D bounds = fontMetrics.getStringBounds(text, g2d);
        double raw = bounds.getWidth();
        return raw % 1 == 0 ? (int)raw : (int)raw + 1;
    }

}
