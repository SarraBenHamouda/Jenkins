package tn.esprit.spring.service.Chambre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {
    ChambreRepository repo;
    BlocRepository blocRepository;

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return repo.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return repo.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return repo.findById(id).orElse(null); // Or throw a custom exception if not found

    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        repo.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return repo.countByTypeCAndBlocIdBloc(type, idBloc);
    }

    @Override
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
        LocalDate dateDebutAU = getDateDebutAnneeUniversitaire();
        LocalDate dateFinAU = getDateFinAnneeUniversitaire();

        List<Chambre> chambresDisponibles = new ArrayList<>();

        for (Chambre chambre : repo.findAll()) {
            if (isChambreEligible(chambre, nomFoyer, type)) {
                int nbReservations = countReservationsForCurrentYear(chambre, dateDebutAU, dateFinAU);
                if (hasAvailablePlace(chambre, nbReservations)) {
                    chambresDisponibles.add(chambre);
                }
            }
        }

        return chambresDisponibles;
    }

    private LocalDate getDateDebutAnneeUniversitaire() {
        int year = LocalDate.now().getYear() % 100;
        return LocalDate.now().getMonthValue() <= 7
                ? LocalDate.of(2000 + year - 1, 9, 15)
                : LocalDate.of(2000 + year, 9, 15);
    }

    private LocalDate getDateFinAnneeUniversitaire() {
        int year = LocalDate.now().getYear() % 100;
        return LocalDate.now().getMonthValue() <= 7
                ? LocalDate.of(2000 + year, 6, 30)
                : LocalDate.of(2000 + year + 1, 6, 30);
    }

    private boolean isChambreEligible(Chambre chambre, String nomFoyer, TypeChambre type) {
        return chambre.getTypeC() == type &&
                chambre.getBloc().getFoyer().getNomFoyer().equals(nomFoyer);
    }

    private int countReservationsForCurrentYear(Chambre chambre, LocalDate dateDebutAU, LocalDate dateFinAU) {
        int count = 0;
        for (Reservation reservation : chambre.getReservations()) {
            LocalDate annee = reservation.getAnneeUniversitaire();
            if (!annee.isBefore(dateDebutAU) && !annee.isAfter(dateFinAU)) {
                count++;
            }
        }
        return count;
    }

    private boolean hasAvailablePlace(Chambre chambre, int nbReservations) {
        switch (chambre.getTypeC()) {
            case SIMPLE:
                return nbReservations == 0;
            case DOUBLE:
                return nbReservations < 2;
            case TRIPLE:
                return nbReservations < 3;
            default:
                return false;
        }
    }


    @Override
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() + " ayant une capacité " + b.getCapaciteBloc());
            if (b.getChambres().size() != 0) {
                log.info("La liste des chambres pour ce bloc: ");
                for (Chambre c : b.getChambres()) {
                    log.info("NumChambre: " + c.getNumeroChambre() + " type: " + c.getTypeC());
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambre = repo.count();
        if (totalChambre == 0) {
            log.info("Aucune chambre disponible pour calculer les pourcentages.");
            return;
        }

        double pSimple = (repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100.0) / totalChambre;
        double pDouble = (repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100.0) / totalChambre;
        double pTriple = (repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100.0) / totalChambre;

        log.info("Nombre total des chambres: " + totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égal à " + pSimple + "%");
        log.info("Le pourcentage des chambres pour le type DOUBLE est égal à " + pDouble + "%");
        log.info("Le pourcentage des chambres pour le type TRIPLE est égal à " + pTriple + "%");
    }
    private void logChambreAvailability(Chambre chambre, long nbReservations, int capacity) {
        if (nbReservations < capacity) {
            log.info("Le nombre de places disponibles pour la chambre " + chambre.getTypeC() + " " + chambre.getNumeroChambre() + " est " + (capacity - nbReservations));
        } else {
            log.info("La chambre " + chambre.getTypeC() + " " + chambre.getNumeroChambre() + " est complète");
        }
    }
    private void logAvailablePlacesForChambre(Chambre chambre, long nbReservations) {
        switch (chambre.getTypeC()) {
            case SIMPLE:
                logChambreAvailability(chambre, nbReservations, 1);
                break;
            case DOUBLE:
                logChambreAvailability(chambre, nbReservations, 2);
                break;
            case TRIPLE:
                logChambreAvailability(chambre, nbReservations, 3);
                break;
        }
    }

    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
        LocalDate dateDebutAU = getDateDebutAnneeUniversitaire();
        LocalDate dateFinAU = getDateFinAnneeUniversitaire();

        for (Chambre chambre : repo.findAll()) {
            long nbReservations = repo.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                    chambre.getIdChambre(), true, dateDebutAU, dateFinAU);

            logAvailablePlacesForChambre(chambre, nbReservations);
        }
    }
}
