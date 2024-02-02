package ar.edu.iua.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Orden;

@Repository
public interface EstimuloRepository extends JpaRepository<Estimulo, Integer>{

}
