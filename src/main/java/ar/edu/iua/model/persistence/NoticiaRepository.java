package ar.edu.iua.model.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Noticia;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Integer>{

	@Query(value = "SELECT n.id,n.descripcion,e.titulo,n.fecha FROM railway.noticias n inner join railway.estimulo e on n.estimulo=e.id order by n.id", nativeQuery = true)
	public List<Object>obtenerTodas();
}
