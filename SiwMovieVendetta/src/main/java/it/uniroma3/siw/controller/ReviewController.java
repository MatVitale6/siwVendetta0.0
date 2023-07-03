package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;


@Controller
public class ReviewController {
	@Autowired ReviewRepository reviewRepository;
	@Autowired ReviewValidator reviewValidator;
	@Autowired CredentialsService credentialsService;
	@Autowired MovieService movieService;
	@Autowired ReviewService reviewService;
	
	@GetMapping(value="/formNewReview/{idMovie}/{idUser}")
	public String formNewReview(@PathVariable("idMovie") Long idMovie,@PathVariable("idUser") Long idUser, Model model) {
		Movie movie = this.movieService.findMovieByID(idMovie);
		if(movie!=null) {
			model.addAttribute("review", new Review());
			model.addAttribute("movie", movie);
			model.addAttribute("credentials", this.getCredentials());
			return "formNewReview";
		}
		else {
			return "reviewError.html";
		}
	}
	
	@PostMapping("/addReview/{idMovie}/{idUser}")
	public String newReview(@Valid @ModelAttribute("review")Review review, @PathVariable("idMovie") Long idMovie,
							@PathVariable("idUser") long idUser,BindingResult bindingResult, Model model) {
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.reviewService.addReview(review, idMovie, idUser, model);
			model.addAttribute("review", review);
			return "/review.html";
		}
		else{
			return "formNewReview.html";
			}
	}

	@GetMapping("/review/{id}")
	public String getReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("review", this.reviewRepository.findById(id).get());
		return "/review.html";
	}

	private Credentials getCredentials() {
		if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			return credentials;
		}
		return null;
	}
}
