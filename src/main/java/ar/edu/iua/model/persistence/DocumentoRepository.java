package ar.edu.iua.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.model.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer>{

}
