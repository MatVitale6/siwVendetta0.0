package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {

	
	@Autowired
	protected UserRepository userRepository;
	
	
	/**
	 * Questo metodo ritorna un User dal DB basandosi sul suo ID
	 * @param id
	 * @return User corrispondente all'ID se esiste o null altrimenti
	 */
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	/**
	 * questo metodo salva un user nel DB
	 * @param user
	 * @return l'user salvato
	 * @throws DataIntefrityViolationExceprion se giá esiste un user con quel username
	 */
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	/**
	 * questo metodo ritorna tutti gli users dal DB 
	 * @return una lista con tutti glu Users
	 */
	@Transactional
	public List<User> getAllUsers() {
		List<User>result = new ArrayList<>();
		Iterable<User> iterable = this.userRepository.findAll();
	    for(User user : iterable)
	    	result.add(user);
		return result;
		
	}
}
