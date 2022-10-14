package com.fiordor.dynamic2drangetree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
@RestController
public class Dynamic2DRangeTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dynamic2DRangeTreeApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello() {
		String html = readFile();
		//html = String.format(html, "c");
		return html;
	}

	@PostMapping("/test")
	public String test (@RequestParam Map<String, String> params) {
		return "test" + params.toString();
	}

	private String readFile() {
		try {
			File fileObject = ResourceUtils.getFile("classpath:templates/index.html");
			Scanner fileReader = new Scanner(fileObject);
			StringBuilder rawData = new StringBuilder();
			while (fileReader.hasNextLine()) {
				String data = fileReader.nextLine();
				rawData.append(data).append("\n");
			}
			fileReader.close();
			return rawData.toString();
		} catch (FileNotFoundException e) {
			return "File not found Exception";
		}
	}

}
