package ar.edu.iua.model.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.User;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{
	@Query(value = "select r.* from roles as r inner join ministerios_roles as mr on r.id=mr.id_rol inner join ministerio as m on mr.id_ministerio=m.id where m.id=?1", nativeQuery = true)
	public List<Rol> findByMinisterio(int ministerio);
}
