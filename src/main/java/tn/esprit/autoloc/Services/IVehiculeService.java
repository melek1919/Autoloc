package tn.esprit.autoloc.Services;

import java.util.List;
import tn.esprit.autoloc.Entities.Vehicules;

public interface IVehiculeService {

    Vehicules create(Vehicules entite);

    Vehicules findById(Long id);

    List<Vehicules> findAll();

    Vehicules update(Long id, Vehicules entite);

    void deleteById(Long id);
}
