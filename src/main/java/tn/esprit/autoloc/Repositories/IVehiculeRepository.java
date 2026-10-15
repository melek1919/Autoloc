package tn.esprit.autoloc.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.Entities.Vehicules;

public interface IVehiculeRepository extends JpaRepository<Vehicules, Long> {
}