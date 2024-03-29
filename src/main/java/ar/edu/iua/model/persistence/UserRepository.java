package ar.edu.iua.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findBylegajo(String legajo);
	public User findByusername(String username);
}
