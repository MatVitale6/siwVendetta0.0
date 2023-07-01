package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ReviewService {

    @Autowired ReviewRepository reviewRepository;
    @Autowired MovieRepository movieRepository;
    @Autowired UserRepository userRepository;

    public Review addReview(@ModelAttribute("review") Review review, @PathVariable("movie_id") Long movieID,
                            @PathVariable("user_id") Long userID, Model model) {
        User user = userRepository.findById(userID).orElse(null);
        Movie movie = movieRepository.findById(movieID).orElse(null);

        if(!this.reviewRepository.existsByWriterAndFilm(user, movie)) {
            review.setWriter(user);
            review.setReviewed(movie);
            //se vuoi puoi aggiungere la data con LocalDate.now()
            user.addWritten(review);
            movie.getReviews().add(review);
            review = reviewRepository.save(review);
            movieRepository.save(movie);
            userRepository.save(user);
            return review;
        }
        return null;
    }

    @Transactional
    public Review deleteReview(Long id) {
        Review rev = reviewRepository.findById(id).orElse(null);
        rev.getReviewed().getReviews().remove(rev);
        movieRepository.save(rev.getReviewed());
        rev.getWriter().getWrittens().remove(rev);
        userRepository.save(rev.getWriter());
        reviewRepository.delete(rev);
        return rev;
    }

    public boolean existReviewByWriterAndFilm (Long movieId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Movie movie = movieRepository.findById(movieId).orElse(null);

        return this.reviewRepository.existsByWriterAndFilm(user, movie);
    }

}
