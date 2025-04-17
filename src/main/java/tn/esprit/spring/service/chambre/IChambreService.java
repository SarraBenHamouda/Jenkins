package tn.esprit.spring.service.chambre;

import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.typeChambre;

import java.util.List;

public interface IChambreService {
    Chambre addOrUpdate(Chambre c);
    List<Chambre> findAll();
    Chambre findById(long id);
    void deleteById(long id);
    void delete(Chambre c);
    List<Chambre>  getChambresParNomBloc( String nomBloc);
    long  nbChambreParTypeEtBloc(typeChambre type, long idBloc);
    List<Chambre>  getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, typeChambre type);
    void listeChambresParBloc();
    void pourcentageChambreParTypeChambre();
    void nbPlacesDisponibleParChambreAnneeEnCours();



}
