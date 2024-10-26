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
	
	@Query(value = "SELECT d.id, m.nombre as destinatario, tipo, titulo, descripcion, es_final, estimulo, estado, "
            + "fecha, usuario, ministerio, rol FROM "
            + "documento as d LEFT JOIN ministerio as m ON d.ministerio = m.id "
            + "ORDER BY fecha DESC", nativeQuery = true)
	public List<Documento> findByIdEstimuloAdmin(int idEstimulo);
	
	@Query(value = "SELECT d.id,m.nombre as destinatario,tipo,titulo,descripcion,es_final,estimulo,estado,"
			+ "	fecha,usuario,ministerio,rol FROM "
			+ "	documento as d LEFT join ministerio as m on d.ministerio=m.id"
			+ " inner join users as u on u.id=d.usuario"
			+ "	where estimulo=?1 and (rol=?2 or u.id_rol_principal=?2)"
			+ " and (ministerio=0 or ministerio=?3 or u.id_ministerio_principal=?3)order by fecha desc", nativeQuery = true)
	public List<Documento> findByIdEstimuloNonAdmin(int idEstimulo, int idRol, String idMinisterio);
	
	// Método principal que elige la consulta en función de esAdmin
	default List<Documento> findByIdEstimulo(int idEstimulo, int idRol, String idMinisterio, boolean esAdmin) {
	    if (esAdmin) {
	        return findByIdEstimuloAdmin(idEstimulo);
	    } else {
	        return findByIdEstimuloNonAdmin(idEstimulo, idRol, idMinisterio);
	    }
	}
	
	@Query(value = "SELECT * FROM documento WHERE ID=?1", nativeQuery = true)
	Optional<Documento> obtenerPorId(int idDocumento);

	@Query(value = "select r.descripcion as label, count(*) as value from documento as d inner join users as u on d.usuario=u.id "
			+ "inner join roles as r on u.id_rol_principal=r.id group by id_rol_principal", nativeQuery = true)
	public List<Object[]> estdisticasPorRol();
	
	@Query(value = "select m.nombre as label,count(*) as value from documento as d inner join ministerio as m "
			+ "on d.ministerio=m.id group by ministerio", nativeQuery = true)
	public List<Object[]> estadisticasPorMinisterio();

}
