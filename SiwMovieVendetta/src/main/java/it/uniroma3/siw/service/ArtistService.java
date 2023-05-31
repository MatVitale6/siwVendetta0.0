package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import jakarta.transaction.Transactional;


@Service
public class ArtistService {
	
	@Autowired
	protected ArtistRepository artistRepository;
	
	@Transactional
	public boolean creaNewArtist (Artist artist) {
		boolean res = false;
		if(artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
			res = true;
			artistRepository.save(artist);
		}
		return res;
	}
}
