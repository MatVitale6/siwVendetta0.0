package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/* Con la notazione @Entity comunico al framework che bisogna creare una tabella nel DB;
 * Il framework cerca tutte le classi @Entity e per ciascuna di 
 * esse crea un'opportuna tabella nel database  */
@Entity
@Table
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
    private String title;
	@NotNull
	@Min(1900)
	@Max(2023)
    private Integer year;
    
	
	
    @ManyToOne
    private Artist director;
    
    @ManyToMany(mappedBy="actedMovies")
    private List<Artist>actors;
    
    @OneToMany(mappedBy="film")
    private List<Review> reviews;
    
    @OneToMany
	private Set<Picture> images;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Set<Picture> getImmagini() {
		return images;
	}

	public void setImmagini(Set<Picture> immagini) {
		this.images = immagini;
	}
	
    public Artist getDirector() {
		return director;
	}

	public void setDirector(Artist director) {
		this.director = director;
	}

	public List<Artist> getActors() {
		return actors;
	}

	public void setActors(List<Artist> actors) {
		this.actors = actors;
	}
	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	
	// seguono metodi equals e hashCode: due oggetti Movie sono uguali se hanno
    //									 stesso titolo e stesso anno
	@Override
	public int hashCode() {
		return Objects.hash(id, title, year);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return Objects.equals(title, other.title)
				&& Objects.equals(year, other.year);
	}
}
