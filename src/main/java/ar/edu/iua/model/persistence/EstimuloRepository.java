package ar.edu.iua.model.persistence;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Estimulo;


@Repository
public interface EstimuloRepository extends JpaRepository<Estimulo, Integer>{
	
	@Query(value = "SELECT * FROM ESTIMULO WHERE ESTADO=1 ORDER BY FECHA_FIN DESC", 
		       countQuery = "SELECT count(*) FROM ESTIMULO WHERE ESTADO=1", 
		       nativeQuery = true)
		public Page<Estimulo> findByEstado(Pageable pageable);
	
	@Query(value = "UPDATE FROM ESTIMULO SET ESTADO='2' WHERE ID=?1", nativeQuery = true)
	public void cambiarEstado(int id);

}
