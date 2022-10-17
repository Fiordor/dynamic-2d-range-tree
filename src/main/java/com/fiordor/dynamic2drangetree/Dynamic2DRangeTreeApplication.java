package com.fiordor.dynamic2drangetree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiordor.dynamic2drangetree.structure.Point;
import com.fiordor.dynamic2drangetree.utils.HTML;
import com.fiordor.dynamic2drangetree.utils.Image;
import com.fiordor.dynamic2drangetree.utils.Resource;

@SpringBootApplication
@RestController
public class Dynamic2DRangeTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dynamic2DRangeTreeApplication.class, args);
	}

	@GetMapping("/")
	public String home(HttpServletRequest request) {
		
		List<Point<String>> points = (List<Point<String>>)request.getSession().getAttribute("points");

		if (points == null) {
			points = new ArrayList<Point<String>>();
			request.getSession().setAttribute("points", points);
		}

		StringBuilder stBuilder = new StringBuilder().append("<div id=\"listPoints\" class=\"list-points\">");
		for (int i = 0; i < points.size(); i++) {
			Point<String> p = (Point<String>) points.get(i);
			stBuilder.append(HTML.parsePoint(p));
		}
		stBuilder.append("</div>");

		String html = Resource.readIndexAsString();
		html = html.replace("<div id=\"listPoints\" class=\"list-points\"></div>", stBuilder.toString());
		
		return html;
	}

	@PostMapping("/add")
	public String add(@RequestParam Map<String, String> params, HttpServletRequest request) {

		String x = params.get("x");
		String y = params.get("y");
		Point<String> p = new Point<String>(x, y);
		( ( List<Point<String>> )( request.getSession().getAttribute("points") ) ).add(p);

		return Image.create(params.toString());
	}
}
