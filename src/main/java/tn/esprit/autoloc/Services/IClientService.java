package tn.esprit.autoloc.Services;

import java.util.List;
import tn.esprit.autoloc.Entities.Client;

public interface IClientService {

    Client create(Client entite);

    Client findById(Long id);

    List<Client> findAll();

    Client update(Long id, Client entite);

    void deleteById(Long id);
}
