package ar.edu.iua.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findBylegajo(String legajo);
}
