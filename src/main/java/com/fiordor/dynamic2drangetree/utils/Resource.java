package com.fiordor.dynamic2drangetree.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;

public class Resource {
    

	/**
	 * Read index.html file from resources adding styles and scrpits
	 * 
	 * @return index file as plain text
	 */
    public static String readIndexAsString() {

		String index = readAsString("templates/index.html");

		String cssViewer = readAsString("static/viewer.min.css");
		String css = readAsString("static/styles.css");
		String jsView = readAsString("static/viewer.min.js");
		String js = readAsString("static/script.js");
		

		cssViewer = "<!-- viewer.min.css -->\n<style>" + cssViewer + "</style>";
		css = "<!-- style.css -->\n<style>" + css + "</style>";
		jsView = "<!-- viewer.min.js -->\n<script>" + jsView + "</script>";
		js = "<!-- script.js -->\n<script>" + js + "</script>";

		index = index.replace("<link rel=\"stylesheet\" href=\"viewer.min.css\">", cssViewer);
		index = index.replace("<link rel=\"stylesheet\" href=\"style.css\">", css);
		index = index.replace("<script src=\"viewer.min.js\"></script>", jsView);
		index = index.replace("<script src=\"script.js\"></script>", js);

        return index;
    }


	/**
	 * Read files from resources directory.
	 * 
	 * @param path String with the path with resources/ root
	 * @return File data as plain text
	 */
    public static String readAsString(String path) {

        try {
			InputStream fileObject = new ClassPathResource(path).getInputStream();
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
