package ar.edu.iua.model.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Permiso;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{
	
	@Query(value = "SELECT descripcion,fecha,tipo, id_asoc,leida,nu.id as id FROM railway.notificaciones as n inner join notificaciones_usuarios nu on n.id=nu.id_notificacion "
			+ "and nu.id_usuario=?1 and nu.leida=0 ORDER BY FECHA DESC", nativeQuery = true)
	public List<Object> findByUser(int idUser);
	
	@Query(value = "select * from notificaciones where id=?", nativeQuery = true)
	public Notificacion obtenerPorId(int id);

	
	

}
