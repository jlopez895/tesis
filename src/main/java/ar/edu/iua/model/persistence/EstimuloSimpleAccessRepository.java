package ar.edu.iua.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.EstimuloSimpleAccess;

@Repository
public interface EstimuloSimpleAccessRepository extends JpaRepository<EstimuloSimpleAccess, Integer>{

}
