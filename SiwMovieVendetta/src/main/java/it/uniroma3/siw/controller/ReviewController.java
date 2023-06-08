package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.model.Review;

@Controller
public class ReviewController {
	
	
	@GetMapping ("/formNewReview")
	public String formNewArtist(Model model) {
		model.addAttribute("review", new Review());
		return "/review/formNewReview.html";
	}
	
	
}
