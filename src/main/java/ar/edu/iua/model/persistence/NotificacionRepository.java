package ar.edu.iua.model.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Permiso;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{
	
	@Query(value = "select n.* from roles as r inner join notificaciones_roles as nr "
			+ "on r.id=nr.id_rol inner join notificaciones as n on nr.id_notificacion=n.id where n.leida=0 and r.id=?1", nativeQuery = true)
	public List<Notificacion> findByUser(int idUser);

}
