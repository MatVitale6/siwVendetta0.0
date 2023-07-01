package it.uniroma3.siw.repository;

import java.util.List;

import it.uniroma3.siw.model.User;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long>  {
	
	public List<Review> findByFilm(Movie film);
	
	public boolean existsByTitleAndVote(String title, Integer Vote);

	public boolean existsByWriterAndFilm(User user, Movie movie);
}
