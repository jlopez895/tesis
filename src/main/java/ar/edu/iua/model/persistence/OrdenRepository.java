package ar.edu.iua.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

	Optional<Orden> findByNumeroOrden(int nroOrden);
	public void deleteByNumeroOrden(int numeroOrden);

	@Query(value = "select truncate(avg(densidad),2) from detalle_orden where id_orden = ?1", nativeQuery = true)
	public double promedioDensidad(int idOrden);

	@Query(value = "select truncate(avg(caudal),2) from detalle_orden where id_orden = ?1", nativeQuery = true)
	public double promedioCaudal(int idOrden);

	@Query(value = "select truncate(avg(temperatura),2) from detalle_orden where id_orden = ?1", nativeQuery = true)
	public double promedioTemperatura(int idOrden);

}
