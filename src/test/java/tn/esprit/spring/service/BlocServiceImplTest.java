package tn.esprit.spring.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.service.Bloc.BlocService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocService blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @Order(1)
    void testAjouterBloc() {
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(100)
                .chambres(new ArrayList<>()) // ✅ Initialize chambres
                .build();

        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc); // ✅ Fix mock

        Bloc result = blocService.addOrUpdate(bloc);

        assertNotNull(result, "Bloc should not be null");
        assertEquals("Bloc A", result.getNomBloc());
        assertEquals(100, result.getCapaciteBloc());

        verify(blocRepository, times(1)).save(any(Bloc.class)); // Ensure save was called
    }


    @Test
    @Order(2)
    void testRecupererTousLesBlocs() {
        Bloc bloc1 = Bloc.builder().idBloc(1L).nomBloc("Bloc A").capaciteBloc(100).build();
        Bloc bloc2 = Bloc.builder().idBloc(2L).nomBloc("Bloc B").capaciteBloc(50).build();

        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2)); // ✅ Ensure mock returns values

        List<Bloc> blocs = blocService.findAll();

        assertNotNull(blocs, "Bloc list should not be null");
        assertEquals(2, blocs.size(), "Bloc list size should be 2");
        assertEquals("Bloc A", blocs.get(0).getNomBloc());

        verify(blocRepository, times(1)).findAll();
    }


    @Test
    @Order(3)
    void testRecupererBlocParId() {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc A").capaciteBloc(100).build();

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc)); // ✅ Return a non-empty Optional

        Bloc result = blocService.findById(1L);

        assertNotNull(result, "Bloc should not be null");
        assertEquals("Bloc A", result.getNomBloc());

        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    @Order(4)
    void testSupprimerBloc() {
        long idBloc = 1L;

        doNothing().when(blocRepository).deleteById(idBloc); // ✅ Mock deletion

        blocService.deleteById(idBloc); // Call service method

        verify(blocRepository, times(1)).deleteById(idBloc); // Ensure it was called
    }




    @RepeatedTest(3)
    @Order(5)
    void testRepetition() {
        Bloc bloc = Bloc.builder().nomBloc("Bloc Test").capaciteBloc(30).build();

        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc result = blocService.addOrUpdate(bloc);

        assertNotNull(result, "Bloc should not be null");
        assertEquals("Bloc Test", result.getNomBloc());
        assertEquals(30, result.getCapaciteBloc());

        verify(blocRepository, times(1)).save(any(Bloc.class)); // Ensure save is called
    }


    @AfterEach
    void tearDown() {
        System.out.println("Test terminé !");
    }
}
