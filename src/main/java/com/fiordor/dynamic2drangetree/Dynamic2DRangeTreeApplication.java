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

import com.fiordor.dynamic2drangetree.redblacktree.RedBlackTree;
import com.fiordor.dynamic2drangetree.utils.Image;
import com.fiordor.dynamic2drangetree.utils.ImageLabel;
import com.fiordor.dynamic2drangetree.utils.ImageRedBlackTree;
import com.fiordor.dynamic2drangetree.utils.Resource;

@SpringBootApplication
@RestController
public class Dynamic2DRangeTreeApplication {

	public static RedBlackTree<Double> redBlackTree = null;

	public static void main(String[] args) {
		SpringApplication.run(Dynamic2DRangeTreeApplication.class, args);
	}

	@GetMapping("/")
	public String home(HttpServletRequest request) {
		
		List<Double> redBlackTreeList = (List<Double>)request.getSession().getAttribute("red-black-tree");

		if (redBlackTreeList == null) {
			redBlackTreeList = new ArrayList<Double>();
			request.getSession().setAttribute("red-black-tree", redBlackTreeList);
		}

		
		StringBuilder stBuilder = new StringBuilder().append("<div id=\"listPoints\" class=\"list-points\">");
		for (int i = 0; i < redBlackTreeList.size(); i++) {
			
			Double p = Double.valueOf( redBlackTreeList.get(i) );
			stBuilder.append(p);
		}
		stBuilder.append("</div>");
		

		String html = Resource.readIndexAsString();
		html = html.replace("<div id=\"listPoints\" class=\"list-points\"></div>", stBuilder.toString());
		
		return html;
	}

	@PostMapping("/add-2d-range-tree")
	public String add2DRangeTree(@RequestParam Map<String, String> params, HttpServletRequest request) {

		return "Not developed yet";
	}

	@PostMapping("/add-red-black-tree")
	public String addRedBlackTree(@RequestParam Map<String, String> params, HttpServletRequest request) {

		Double k = Double.parseDouble(params.get("k"));
		( (List<Double>) request.getSession().getAttribute("red-black-tree") ).add(k);

		if (redBlackTree == null) { redBlackTree = new RedBlackTree<>(k); }
		else { redBlackTree.insert(k); }

		ImageRedBlackTree<Double> imageRBT = new ImageRedBlackTree<>(redBlackTree);

		System.out.println("Labels ------------");
		ImageLabel[] labels = imageRBT.getLabels();
		for (int i = 0; i < labels.length; i++) {
			System.out.println(labels[i].label() + " " + labels[i].x() + " " + labels[i].y());
		}
		System.out.println("------------------");


		return Image.create(imageRBT.getLabels(), imageRBT.getLines(), imageRBT.getWidth(), imageRBT.getHeight());
	}
}

