package tn.esprit.spring.service.Bloc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BlocService implements IBlocService {
    BlocRepository repo;
    ChambreRepository chambreRepository;
    FoyerRepository foyerRepository;

    @Override
    public Bloc addOrUpdate2(Bloc b) {
        b = repo.save(b);  // Sauvegarde le bloc avant d'affecter les chambres
        List<Chambre> chambres = b.getChambres();
        for (Chambre c : chambres) {
            c.setBloc(b);
            chambreRepository.save(c);
        }
        return b;
    }


    @Override
    public Bloc addOrUpdate(Bloc b) {
        List<Chambre> chambres = b.getChambres();

        if (chambres == null) {
            chambres = new ArrayList<>(); // Prevents NullPointerException
        }

        b = repo.save(b); // Save the Bloc first

        for (Chambre chambre : chambres) {
            chambre.setBloc(b);
            chambreRepository.save(chambre);
        }

        return b;
    }


    @Override
    public List<Bloc> findAll() {
        return repo.findAll();
    }

    @Override
    public Bloc findById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloc not found with id: " + id));
    }


    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Bloc b) {
        List<Chambre> chambres= b.getChambres();
        for (Chambre chambre: chambres) {
            chambreRepository.delete(chambre);
        }
        repo.delete(b);
    }

    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
        //1
        Bloc b = repo.findByNomBloc(nomBloc);
        List<Chambre> chambres= new ArrayList<>();
        for (Long nu: numChambre) {
            Chambre chambre=chambreRepository.findByNumeroChambre(nu);
            chambres.add(chambre);
        }
        // Keyword (2ème méthode)
        //chambres=chambreRepository.findAllByNumeroChambre(numChambre);
        //2 Parent==>Chambre  Child==> Bloc
        for (Chambre cha : chambres) {
            //3 On affecte le child au parent
                cha.setBloc(b);
            //4 save du parent
                chambreRepository.save(cha);
        }
        return b;
    }

    @Override
    public Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer) {
        Bloc b = repo.findByNomBloc(nomBloc);
        Foyer f = foyerRepository.findByNomFoyer(nomFoyer);
        b.setFoyer(f);
        return repo.save(b);  // Correction ici
    }





//another try just to make sure about it 

//noooow i sseee the world is goonna beee 

//add Jenkins now

//taw


        


    }

