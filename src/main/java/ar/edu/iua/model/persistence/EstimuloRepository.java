package ar.edu.iua.model.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Estimulo;


@Repository
public interface EstimuloRepository extends JpaRepository<Estimulo, Integer>{
	
	@Query(value = "SELECT * FROM estimulo WHERE ESTADO=1 ORDER BY ID", nativeQuery = true)
	public List<Estimulo> findByEstado();
	
	@Query(value = "SELECT * FROM estimulo WHERE ESTADO=2 ORDER BY ID", nativeQuery = true)
	public List<Estimulo> findCerradas();
	
	@Query(value = "UPDATE estimulo SET ESTADO='2' WHERE ID=?1", nativeQuery = true)
	public void cambiarEstado(int id);

	@Query(value = "SELECT "
			+ "  (SELECT COUNT(*) FROM estimulo WHERE (TIMESTAMPDIFF(HOUR, fecha_inicio, fecha_fin) - tiempo_estmado) < 0) AS label,"
			+ "  (SELECT COUNT(*) FROM estimulo WHERE (TIMESTAMPDIFF(HOUR, fecha_inicio, fecha_fin) - tiempo_estmado) >= 0) AS value", nativeQuery = true)
	public List<Object[]> estadisticasPorEstimulo();

}
