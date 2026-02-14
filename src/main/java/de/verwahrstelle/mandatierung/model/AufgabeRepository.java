package de.verwahrstelle.mandatierung.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AufgabeRepository extends JpaRepository<Aufgabe, Long> {
}
