package ar.edu.iua.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Ministerio;


@Repository
public interface MinisterioRepository extends JpaRepository<Ministerio, Integer>{

}
