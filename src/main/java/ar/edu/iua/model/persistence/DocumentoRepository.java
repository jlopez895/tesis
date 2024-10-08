package ar.edu.iua.model.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.dto.EstadisticaDTO;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer>{
	
	@Query(value = "SELECT d.id,m.nombre as destinatario,tipo,titulo,descripcion,es_final,estimulo,estado,"
			+ "fecha,usuario,ministerio FROM "
			+ "documento as d inner join ministerio as m on d.ministerio=m.id "
			+ "where estimulo=?1", nativeQuery = true)
	public List<Documento> findByIdEstimulo(int idEstimulo);
	
	@Query(value = "SELECT * FROM documento WHERE ID=?1", nativeQuery = true)
	Optional<Documento> obtenerPorId(int idDocumento);

	@Query(value = "select r.descripcion as label, count(*) as value from documento as d inner join users as u on d.usuario=u.id "
			+ "inner join roles as r on u.id_rol_principal=r.id group by id_rol_principal", nativeQuery = true)
	public List<Object[]> estdisticasPorRol();
	
	@Query(value = "select m.nombre as label,count(*) as value from documento as d inner join ministerio as m "
			+ "on d.ministerio=m.id group by ministerio", nativeQuery = true)
	public List<Object[]> estadisticasPorMinisterio();

}
