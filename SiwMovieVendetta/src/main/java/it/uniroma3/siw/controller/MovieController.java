package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
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
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;

/* La notazione Controller identifica che MovieController e' un classe Controller */
@Controller
public class MovieController {
	
	@Autowired MovieService movieService;
	@Autowired MovieValidator movieValidator;
	@Autowired ArtistRepository artistRepository;
	@Autowired ReviewRepository reviewRepository;
	@Autowired ReviewValidator reviewValidator;
	@Autowired CredentialsService credentialsService;
	@Autowired ReviewService reviewService;
	
	@GetMapping("/indexMovie")
	public String getIndexMovie () {
		return "indexMovie.html";
	}
	
	@GetMapping("/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "/admin/formNewMovie.html";
	}
	
	@PostMapping("/movies")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie,BindingResult bindingResult,Model model) {
		/* Se la richiesta può essere soddisfatta entro nell'if altrimenti nel blocco else produco
		/* un messaggio di errore */
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) { 
			this.movieService.createNewMovie(movie);
			model.addAttribute("movie", movie);
			return "/movie.html";
		} 
		else {
			return "/admin/formNewMovie.html";
		} 
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

	@GetMapping("/movies/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {

		model.addAttribute("movie", this.movieService.findMovieByID(id));
		model.addAttribute("review", new Review());
		model.addAttribute("credentials", this.getCredentials());
		if(this.getCredentials() != null) {
			model.addAttribute("giaRecensito", this.reviewService.existReviewByWriterAndFilm(id, this.getCredentials().getUser().getId()));
		}
		return "/movie.html";
	}
	
	@GetMapping("/movies")
	public String showMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "/movies.html";
	}
	
	@GetMapping("/formSearchMovies") 
	public String formSearchMovies() {
		return "/formSearchMovies.html";
	}
	
	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam Integer year) {
		model.addAttribute("movies", this.movieService.findMovieByYear(year));
		return "/foundMovies.html";
	}
	
	@GetMapping("/admin/manageMovies")
	public String manageMovie(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "/admin/manageMovies.html"; 
	}

	@GetMapping("/admin/formUpdateMovie/{id}")
	public String updateMovie (@PathVariable("id") Long id,Model model) {
		model.addAttribute("movie", this.movieService.findMovieByID(id));
		return "/admin/formUpdateMovie.html";
	}

	@GetMapping("/admin/addDirectorToMovie/{id}")
	public String addDirectorToMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		model.addAttribute("movie", this.movieService.findMovieByID(id));
		return "/admin/addDirectorToMovie.html";
	}

	@GetMapping("/admin/setDirectorToMovie/{id1}/{id2}")
	public String setDirectorToMovie(@PathVariable("id1") Long id1,@PathVariable("id2") Long id2 , Model model) {
		Movie movie = this.movieService.findMovieByID(id2);
		Artist director = this.artistRepository.findById(id1).get();
		movie.setDirector(director);
		model.addAttribute("movie",movie);
		model.addAttribute("artist", director);
		this.movieService.createNewMovie(movie);
		return "/admin/formUpdateMovie.html";
	}


	@GetMapping("/admin/manageActorsMovie/{idMovie}")
	public String manageActors(@PathVariable("idMovie") Long id, Model model) {
		Movie movie = this.movieService.findMovieByID(id);
		model.addAttribute(movie);
		model.addAttribute("artists", this.artistRepository.findAll());
		model.addAttribute("artists1", this.artistRepository.findAllByActedMoviesIsContaining(movie));
		model.addAttribute("artists2", this.artistRepository.findAllByActedMoviesIsNotContaining(movie));
		return "/admin/manageActors.html";
	}

	@GetMapping("/admin/addActorToMovie/{idActor}/{idMovie}")
	public String addActorToMovie(@PathVariable("idActor") Long id1, @PathVariable("idMovie") Long id2, Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		Movie movie = this.movieService.findMovieByID(id2);
		Artist actor = this.artistRepository.findById(id1).get();
		movie.getActors().add(actor);
		actor.getActedMovies().add(movie);
		this.movieService.createNewMovie(movie); //questa é la save
		this.artistRepository.save(actor);
		model.addAttribute("artists1", this.artistRepository.findAllByActedMoviesIsContaining(movie));
		model.addAttribute("artists2", this.artistRepository.findAllByActedMoviesIsNotContaining(movie));
		model.addAttribute("movie", movie);
		model.addAttribute("artist", actor);
		return "/admin/manageActors.html";  
	}

	@GetMapping("/admin/removeActorFromMovie/{idActor}/{idMovie}")
	public String removeActroFromMovie(@PathVariable("idActor") Long id1, @PathVariable("idMovie") Long id2, Model model) {
		Movie movie = this.movieService.findMovieByID(id2);
		Artist actor = this.artistRepository.findById(id1).get();
		movie.getActors().remove(actor);
		actor.getActedMovies().remove(movie);
		this.movieService.createNewMovie(movie);
		this.artistRepository.save(actor);
		model.addAttribute("artists", this.artistRepository.findAll());
		model.addAttribute("artists1", this.artistRepository.findAllByActedMoviesIsContaining(movie));
		model.addAttribute("artists2", this.artistRepository.findAllByActedMoviesIsNotContaining(movie));
		model.addAttribute("movie", movie);
		model.addAttribute("artist", actor);
		return "/admin/manageActors.html";
	
	}
}
