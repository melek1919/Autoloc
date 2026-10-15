package tn.esprit.autoloc.Services;

import java.util.List;
import tn.esprit.autoloc.Entities.Equipement;

public interface IEquipementService {

    Equipement create(Equipement entite);

    Equipement findById(Long id);

    List<Equipement> findAll();

    Equipement update(Long id, Equipement entite);

    void deleteById(Long id);
}
