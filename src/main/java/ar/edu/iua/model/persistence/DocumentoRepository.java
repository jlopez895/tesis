package ar.edu.iua.model.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Orden;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer>{
	
	@Query(value = "SELECT * FROM DOCUMENTO WHERE ID_ESTIMULO=?1", nativeQuery = true)
	public List<Documento> findByIdEstimulo(int idEstimulo);
}
