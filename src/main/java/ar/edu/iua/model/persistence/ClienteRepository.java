package ar.edu.iua.model.persistence;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	Optional<Cliente> findByRazonSocial(String razonSocial);
	Optional<Cliente> findByRazonSocialAndId(String razonSocial,long id);
	Optional<Cliente> findFirstByCodigoexterno(String codigoExterno);
	
}


