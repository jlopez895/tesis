package ar.edu.iua.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.business.IAlarmaBusiness;
import ar.edu.iua.model.Alarma;

public interface AlarmaRepository extends JpaRepository<Alarma, Long> {

}
