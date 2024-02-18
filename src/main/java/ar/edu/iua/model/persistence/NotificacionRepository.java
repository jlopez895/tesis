package ar.edu.iua.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Permiso;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{

}
