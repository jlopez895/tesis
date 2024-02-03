package ar.edu.iua.model.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.DetalleOrden;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Orden;

@Repository
public interface EstimuloRepository extends JpaRepository<Estimulo, Integer>{
	
	@Query(value = "SELECT * FROM ESTIMULO WHERE ESTADO=1 ORDER BY ID", nativeQuery = true)
	public List<Estimulo> findByEstado();

}
