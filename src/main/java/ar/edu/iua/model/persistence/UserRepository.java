package ar.edu.iua.model.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Documento;
import ar.edu.iua.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findBylegajo(String legajo);
	public User findByusername(String username);
	
	
	@Query(value = "select * from users where id_rol_principal=?1", nativeQuery = true)
	public List<User> findByRolOny(int idEstimulo);
	
	@Query(value = "select * from users where id_rol_principal=?1 and id_ministerio_principal=?2", nativeQuery = true)
	public List<User> findByRolMinisterio(int idRol, String idMinisterio);
	
	// Método principal que elige la consulta en función de esAdmin
	default List<User> findByRol(int idRol, String idMinisterio) {
	    if (idMinisterio=="") {
	        return findByRolOny(idRol);
	    } else {
	        return findByRolMinisterio(idRol, idMinisterio);
	    }
	}
	
	@Query(value = "SELECT u.* FROM railway.users as u inner join roles as r on u.id_rol_principal=r.id where r.rol='ROLE_MESA_CONTROL'", nativeQuery = true)
	public List<User> getAdmins();
}
