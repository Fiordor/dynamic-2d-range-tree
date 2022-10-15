package com.fiordor.dynamic2drangetree;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiordor.dynamic2drangetree.utils.Image;
import com.fiordor.dynamic2drangetree.utils.Resource;

@SpringBootApplication
@RestController
public class Dynamic2DRangeTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dynamic2DRangeTreeApplication.class, args);
	}

	@GetMapping("/")
	public String home() {
		return Resource.readIndexAsString();
	}

	@PostMapping("/add")
	public String test (@RequestParam Map<String, String> params) {
		return Image.create(params.toString());
	}
}
