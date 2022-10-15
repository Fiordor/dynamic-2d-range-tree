package com.fiordor.dynamic2drangetree.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;

public class Resource {
    

	/**
	 * Read index.html file from resources
	 * 
	 * @return index file as plain text
	 */
    public static String readIndexAsString() {
        return readAsString("templates/index.html");
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
