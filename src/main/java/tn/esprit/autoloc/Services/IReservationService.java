package tn.esprit.autoloc.Services;

import java.util.List;
import tn.esprit.autoloc.Entities.Reservation;

public interface IReservationService {

    Reservation create(Reservation entite);

    Reservation findById(Long id);

    List<Reservation> findAll();

    Reservation update(Long id, Reservation entite);

    void deleteById(Long id);
}
