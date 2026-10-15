package tn.esprit.autoloc.Entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 50)
    private String ville;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Column(nullable = false, length = 20)
    private String telephone;
    @OneToMany(mappedBy ="agence")
    private List<Employe> employes;
    @OneToMany(mappedBy ="agence")
    private List<Vehicules> vehicules;
}