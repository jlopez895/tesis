package ar.edu.iua.model.persistence;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Camion;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Long>{
	
	
	Optional<Camion> findByPatente(String patente);
	Optional<Camion> findByPatenteAndId(String patente,long id);
	Optional<Camion> findFirstByCodigoexterno(String codigoExterno);
	
}


