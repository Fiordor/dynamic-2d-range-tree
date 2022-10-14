package com.fiordor.dynamic2drangetree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

@SpringBootApplication
@RestController
public class Dynamic2DRangeTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dynamic2DRangeTreeApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello() {
		String html = readResourceFile();
		//html = String.format(html, "c");
		return html;
	}

	@PostMapping("/test")
	public String test (@RequestParam Map<String, String> params) {
		
		return createImage(params.toString(), true);

	}

	/**
	 * Create a image using text
	 * 
	 * @param text Text to draw
	 * @param html Specify if the base64 string is for HTML use
	 * @return image on base64 String
	 */
	private String createImage(String text, boolean html) {

		final String FORMAT = "png";

		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.drawString(text, 10, 10);

		String imgBase64 = "";
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, FORMAT, byteArray);
			imgBase64 = Base64.getEncoder().encodeToString(byteArray.toByteArray());
		} catch (IOException e) {
			imgBase64 = e.getMessage();
		}

		return html ? "data:image/" + FORMAT + ";base64," + imgBase64 : imgBase64;
	}

	private String readResourceFile() {
		try {
			InputStream fileObject = new ClassPathResource("templates/index.html").getInputStream();
			Scanner fileReader = new Scanner(fileObject);
			StringBuilder rawData = new StringBuilder();
			while (fileReader.hasNextLine()) {
				String data = fileReader.nextLine();
				rawData.append(data).append("\n");
			}
			fileReader.close();
			return rawData.toString();
		} catch (IOException e) {
			return "File not found Exception " + e.getMessage();
		}
	}

}
