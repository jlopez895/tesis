package ar.edu.iua.model.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer>{
	
	@Query(value = "SELECT d.id,m.nombre as destinatario,tipo,titulo,descripcion,es_final,estimulo,"
			+ "fecha,usuario,ministerio FROM "
			+ "documento as d inner join ministerio as m on d.ministerio=m.id "
			+ "where estimulo=?1", nativeQuery = true)
	public List<Documento> findByIdEstimulo(int idEstimulo);
	
	@Query(value = "SELECT * FROM DOCUMENTO WHERE ID=?1", nativeQuery = true)
	Optional<Documento> obtenerPorId(int idDocumento);

}
