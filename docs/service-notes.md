# Atelier 4 — Notes : couche Service, IoC et injection de dépendances

## A. Modes d'injection comparés

| Critère | Constructeur (`@RequiredArgsConstructor` + `final`) | Attribut (`@Autowired` sur le champ) |
|---|---|---|
| Champ `final` possible ? | Oui | Non |
| Dépendance visible dans la signature de la classe ? | Oui (paramètre du constructeur) | Non (dépendance cachée) |
| Utilisable avec `new` hors conteneur sans dépendance `null` ? | Oui : `new ContratServiceImpl(repo)` ; impossible de l'oublier | Non : le champ reste `null` sans Spring |
| Signalé par SonarQube for IDE ? | Non | Oui (règle S6813, à vérifier dans le panneau) |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Dépendance obligatoire, champ `final`, typée par interface |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Idem |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Idem |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Idem |
| ClientServiceImpl | IClientRepository | Constructeur | Idem |
| ReservationServiceImpl | IReservationRepository | Constructeur | Idem |
| ContratServiceImpl | IContratRepository | Constructeur | Idem ; la suppression d'un contrat supprime ses paiements par cascade |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Idem ; lecture seule (les paiements passent par le Contrat) |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Idem |

## C. Messages d'erreur du conteneur

**Message 1 — `NoSuchBeanDefinitionException` sur `IContratService`**
- Bean concerné : `IContratService`, demandé par le constructeur de `ContratController`.
- Causes probables côté `ContratServiceImpl` : (1) l'annotation `@Service` est oubliée ; (2) la classe est placée hors du package racine `tn.esprit.autoloc`, donc jamais détectée par le scan.
- Correction : ajouter `@Service` et vérifier le package.

**Message 2 — `NoUniqueBeanDefinitionException` (`emailNotificateur`, `smsNotificateur`)**
- Deux beans du même type : Spring ne sait pas lequel injecter dans `NotificationController`.
- Correction 1 : `@Primary` placé **sur la classe** du bean à utiliser par défaut.
- Correction 2 : `@Qualifier("emailNotificateur")` placé **sur le paramètre du constructeur** du contrôleur.

**Message 3 — dépendance circulaire `clientServiceImpl` ↔ `reservationServiceImpl`**
- Avec l'injection par constructeur, Spring doit créer A pour construire B et B pour construire A : aucun des deux ne peut exister en premier, d'où l'échec au démarrage (pas de contournement possible, contrairement à l'injection par attribut qui masque le problème).
- Piste de conception : extraire la responsabilité commune dans un troisième service (ou faire passer l'un des deux par le repository) afin que la dépendance soit à sens unique.

## D. Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Injection par attribut `@Autowired` (ancien code des services) | S6813 : préférer l'injection par constructeur (champ non `final`, dépendance cachée) | `@RequiredArgsConstructor` + champ `final` dans les neuf services |
| `throw new RuntimeException(...)` (expérience de l'étape 4) | S112 : ne pas lever d'exception générique | Retour à `ResourceNotFoundException` |
| Cast inutile `(List<X>) repository.findAll()` | Cast redondant : `JpaRepository.findAll()` renvoie déjà une `List` | Suppression des casts |
| 
| Imports inutilisés | Code mort | Supprimés |

## E. Questions de compréhension

1. **Où est le `new` de `ContratServiceImpl` ?** Dans le conteneur Spring : `SpringApplication.run` détecte `@Service` par le scan puis instancie la classe en lui passant le proxy `IContratRepository` par le constructeur.
2. **Pourquoi dépendre de `IContratService` ?** Le contrôleur dépend d'une abstraction : on peut changer ou remplacer l'implémentation (ou la simuler en test) sans modifier le contrôleur.
3. **Pourquoi un service singleton reste sans état ?** Il est partagé par tous les threads. Exemple de bug : un attribut `contratCourant` modifié par la requête A est écrasé par la requête B, et A enregistre alors le mauvais contrat.
4. **Pourquoi `update` charge avant `save` ?** Passer l'objet reçu à `save` écraserait toutes les colonnes (valeurs `null` comprises) et, avec `orphanRemoval`, pourrait vider la collection de paiements. On charge l'existant, on recopie les attributs modifiables, puis on enregistre.
5. **Différences.**
   - `@Component` se pose sur une classe que l'on possède ; `@Bean` est une méthode d'une classe `@Configuration`, pour une classe non annotable (bibliothèque, JDK) ou une construction particulière.
   - `@Primary` désigne un candidat par défaut (posé sur la classe) ; `@Qualifier` choisit explicitement un bean à l'injection (posé sur le paramètre).
   - `IPaiementService` est en lecture seule car le Paiement est un enfant en composition : on le crée ou le retire uniquement à travers son Contrat (cascade et `orphanRemoval`).
