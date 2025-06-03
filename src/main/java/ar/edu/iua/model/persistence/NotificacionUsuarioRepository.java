package ar.edu.iua.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.NotificacionUsuario;

@Repository
public interface NotificacionUsuarioRepository extends JpaRepository<NotificacionUsuario, Integer>{

	@Query(value = "select * from notificaciones_usuarios where id=?1 and id_usuario=?2", nativeQuery = true)
	Optional<NotificacionUsuario> findByIdNotUser(int idNot, int idUser);

}
