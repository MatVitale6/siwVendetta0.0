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

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;


@Controller
public class ArtistController {
	
	@Autowired ArtistRepository artistRepository;
	
	@GetMapping("/indexArtist")
	public String getIndexArtist () {
		return "indexArtist.html";
	}
	
	@GetMapping("/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "/artist/formNewArtist.html";
	}
	
	
	@PostMapping("/artists")
	public String newArtist(@ModelAttribute("artist") Artist artist,BindingResult bindingResult, Model model) {
		/* Se la richiesta può essere soddisfatta entro nell'if altrimenti nel blocco else produco
		/* un messaggio di errore */
		
		if (!artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) { 
			this.artistRepository.save(artist);
			model.addAttribute("artist", artist);
			return "/artist/artist.html";
		} 
		else {
			model .addAttribute("messaggioErrore", "Questo film esiste giá"); return "/artist/formNewArtist.html";
		} 
	}
	
	@GetMapping ("/artists/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "/artist/artist.html";
	}
	
	@GetMapping("/artists")
	public String showArtists (Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "/artist/artists.html"; 
	}
	
	@GetMapping("/formSearchArtists") 
	public String formSearchArtists() {
		return "/artist/formSearchArtists.html"; 
	}
	
	@PostMapping("/searchArtists")
	public String searchArtists(Model model, @RequestParam String surname) {
		model.addAttribute("artists", this.artistRepository.findBySurname(surname));
		return "/artist/foundArtists.html"; 
	}
	
}
