package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name; //si intende nome d'arte
	
	private String surname;
	
	
	private LocalDate dateOfBirth;
	
	
	private String nationality;
	
	@OneToMany(mappedBy="director")
	private List<Movie> directored; //si intede film diretti
	
	@ManyToMany
	private List<Movie>actedMovies; //si intende i film in cui ha recitato!

	

	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, name, nationality, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist other = (Artist) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(name, other.name)
				&& Objects.equals(nationality, other.nationality) && Objects.equals(surname, other.surname);
	}
	
	public Long getId() {
		return id;
	}
    // seguono metodi setter e getter
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public List<Movie> getDirectored() {
		return directored;
	}

	public void setDirectored(List<Movie> directored) {
		this.directored = directored;
	}

	public List<Movie> getActedMovies() {
		return actedMovies;
	}

	public void setActedMovies(List<Movie> actedMovies) {
		this.actedMovies = actedMovies;
	}

}
