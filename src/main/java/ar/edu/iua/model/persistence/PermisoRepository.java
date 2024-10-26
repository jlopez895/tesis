package ar.edu.iua.model.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Permiso;
import ar.edu.iua.model.User;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer>{
	@Query(value = "select p.* from users as u inner join roles as r on u.id_rol_principal=r.id inner join roles_permisos as rp "
			+ "on u.id_rol_principal=rp.id_rol inner join permiso as p on rp.id_permiso=p.id where u.id=?1", nativeQuery = true)
	public List<Permiso> findByUser(int idUser);
	
	@Query(value = "select * from permiso where id_rol_asoc=?1", nativeQuery = true)
	public Permiso findByRol(int id);
	
	@Query(value = "SELECT p.* FROM tesis.permiso as p inner join roles_permisos as r on r.id_permiso=p.id where r.id_rol=?1", nativeQuery = true)
	public List<Permiso> obtenerTodosRol(int id);


}
