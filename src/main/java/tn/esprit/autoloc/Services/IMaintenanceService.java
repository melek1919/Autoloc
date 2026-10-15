package tn.esprit.autoloc.Services;

import java.util.List;
import tn.esprit.autoloc.Entities.Maintenance;

public interface IMaintenanceService {

    Maintenance create(Maintenance entite);

    Maintenance findById(Long id);

    List<Maintenance> findAll();

    Maintenance update(Long id, Maintenance entite);

    void deleteById(Long id);
}
