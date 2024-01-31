package ar.edu.iua.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.DetalleOrden;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {

	Optional<DetalleOrden> findById(long id);

	@Query(value = "SELECT * from detalle_orden where id_orden = ?1 order by fecha_hora_medicion asc limit 1", nativeQuery = true)
	public DetalleOrden findByFechaHoraMedicionAsc(int idOrden);
	
	
	@Query(value = "SELECT * from detalle_orden where id_orden = ?1 order by fecha_hora_medicion desc limit 1", nativeQuery = true)
	public DetalleOrden findByFechaHoraMedicionDesc(int idOrden);
	
	
	
	

}
