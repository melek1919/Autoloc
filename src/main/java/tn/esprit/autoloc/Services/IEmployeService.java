package tn.esprit.autoloc.Services;

import java.util.List;
import tn.esprit.autoloc.Entities.Employe;

public interface IEmployeService {

    Employe create(Employe entite);

    Employe findById(Long id);

    List<Employe> findAll();

    Employe update(Long id, Employe entite);

    void deleteById(Long id);
}
