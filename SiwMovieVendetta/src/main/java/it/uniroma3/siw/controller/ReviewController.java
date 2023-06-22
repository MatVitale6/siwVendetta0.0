package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;


@Controller
public class ReviewController {
	@Autowired ReviewRepository reviewRepository;
	@Autowired ReviewValidator reviewValidator;
	@Autowired MovieService movieService;
	
	@GetMapping("/formNewReview/{idMovie}")
	public String formNewReview(@PathVariable("idMovie") Long idMovie, Model model) {
		model.addAttribute("review", new Review());
		model.addAttribute("movie", this.movieService.findMovieByID(idMovie));
		return "/formNewReview.html";
	}
	
	@PostMapping("/review/{idMovie}")
	public String newReview(@Valid @ModelAttribute("review")Review review,@PathVariable("idMovie") Long idMovie,BindingResult bindingResult, Model model) {
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.reviewRepository.save(review);
			model.addAttribute("review", review);
			
			Movie movie = this.movieService.findMovieByID(idMovie);
			movie.getReviews().add(review);
			review.setReviewed(movie);
			this.movieService.createNewMovie(movie);
			this.reviewRepository.save(review);
			model.addAttribute("review",review);
			model.addAttribute("movie", movie);
			return "/review.html";
		}
		else {
			return "/formNewReview.html";
		}
	}
	@GetMapping("/review/{id}")
	public String getReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("review", this.reviewRepository.findById(id).get());
		return "/review.html"; 
	}
}
