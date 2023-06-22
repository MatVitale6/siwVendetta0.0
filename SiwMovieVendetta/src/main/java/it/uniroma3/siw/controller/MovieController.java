package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping("/indexMovie")
	public String getIndexMovie () {
		return "indexMovie.html";
	}
	
	@GetMapping("/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "/movie/formNewMovie.html";
	}
	
	@PostMapping("/movies")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie,BindingResult bindingResult,Model model) {
		/* Se la richiesta può essere soddisfatta entro nell'if altrimenti nel blocco else produco
		/* un messaggio di errore */
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) { 
			this.movieService.createNewMovie(movie);
			model.addAttribute("movie", movie);
			return "/movie/movie.html";
		} 
		else {
			return "/movie/formNewMovie.html";
		} 
	}
	
	@GetMapping("/movies/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findMovieByID(id));
		return "/movie/movie.html"; 
	}
	
	@GetMapping("/movies")
	public String showMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "/movie/movies.html"; 
	}
	
	@GetMapping("/formSearchMovies") 
	public String formSearchMovies() {
		return "/movie/formSearchMovies.html"; 
	}
	
	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam Integer year) {
		model.addAttribute("movies", this.movieService.findMovieByYear(year));
		return "/movie/foundMovies.html"; 
	}
	
	@GetMapping("/manageMovies")
	public String manageMovie(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "/movie/manageMovies.html"; 
	}

	@GetMapping("/formUpdateMovie/{id}")
	public String updateMovie (@PathVariable("id") Long id,Model model) {
		model.addAttribute("movie", this.movieService.findMovieByID(id));
		return "/movie/formUpdateMovie.html";
	}

	@GetMapping("/addDirectorToMovie/{id}")
	public String addDirectorToMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		model.addAttribute("movie", this.movieService.findMovieByID(id));
		return "/movie/addDirectorToMovie.html";
	}

	@GetMapping("/setDirectorToMovie/{id1}/{id2}")
	public String setDirectorToMovie(@PathVariable("id1") Long id1,@PathVariable("id2") Long id2 , Model model) {
		Movie movie = this.movieService.findMovieByID(id2);
		Artist director = this.artistRepository.findById(id1).get();
		movie.setDirector(director);
		model.addAttribute("movie",movie);
		model.addAttribute("artist", director);
		this.movieService.createNewMovie(movie);
		return "/movie/formUpdateMovie.html";
	}


	@GetMapping("/manageActorsMovie/{idMovie}")
	public String manageActors(@PathVariable("idMovie") Long id, Model model) {
		Movie movie = this.movieService.findMovieByID(id);
		model.addAttribute(movie);
		model.addAttribute("artists", this.artistRepository.findAll());
		model.addAttribute("artists1", this.artistRepository.findAllByActedMoviesIsContaining(movie));
		model.addAttribute("artists2", this.artistRepository.findAllByActedMoviesIsNotContaining(movie));
		return "/movie/manageActors.html";
	}

	@GetMapping("/addActorToMovie/{idActor}/{idMovie}")
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
		return "/movie/manageActors.html";  
	}

	@GetMapping("/removeActorFromMovie/{idActor}/{idMovie}")
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
		return "/movie/manageActors.html";
	
	}
	
	
	
	//non lo so se é giusto
	/*
	@GetMapping("/addReviewToMovie/{idReview}/{idMovie}")
	public String addReviewToMovie(@PathVariable("idReview") Long id1, @PathVariable("idMovie") Long id2, Model model) {
		model.addAttribute("reviews", this.reviewRepository.findAll());
		Movie movie = this.movieService.findMovieByID(id2);
		Review review = this.reviewRepository.findById(id1).get();
		movie.getReviews().add(review);
		this.movieService.createNewMovie(movie); //questa é la save
		this.reviewRepository.save(review);
		model.addAttribute("review", this.reviewRepository.findByFilm(movie));
		model.addAttribute("movie", movie);
		return "/movie/movie.html";  
	}
	*/
}
