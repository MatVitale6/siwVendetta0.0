package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;

/**
 * il service fa da tramite tra le operazioni che vengono offerte e tutt
 * @author matte
 *
 */

@Service
public class MovieService {

	@Autowired MovieRepository movieRepository;
	
	@Transactional
	public void createNewMovie (Movie movie) {
		this.movieRepository.save(movie);
	}
	
	@Transactional
	public Movie findMovieByID(Long id) {
		return this.movieRepository.findById(id).get();
	}
	
	@Transactional
	public Iterable<Movie> findAllMovie() {
		return this.movieRepository.findAll();
	}
	
	@Transactional
	public List<Movie> findMovieByYear(Integer year) {
		return this.movieRepository.findMovieByYear(year);
	}
}
