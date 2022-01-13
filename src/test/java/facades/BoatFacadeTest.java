package facades;

import dtos.BoatDTO;
import dtos.UserDTO;
import entities.Boat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.SetupTestUsers;

import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BoatFacadeTest {

    private static EntityManagerFactory emf;
    private static BoatFacade instance;

    Boat boat;




    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        instance = BoatFacade.getBoatFacade(emf);
        SetupTestUsers.setupUsers(emf);
    }

    @AfterAll
    public static void CleanDB(){
        //Todo Clear DB;
    }


    @BeforeEach
    public void setUp(){

    }

    @Test
    void createBoat(){
        BoatDTO boatDTO = new BoatDTO("Volvo","Japan","julia","BilledURL");
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user");
        boatDTO.getOwners().add(userDTO);
        boatDTO = instance.createBoat(boatDTO);
        assertTrue(boatDTO.getId() > 0);
    }

    @Test
    void getBoatByUsername(){
        createBoat();
        List<BoatDTO> boatDTOList = instance.getAllBoatsByUser("user");
        assertTrue(!boatDTOList.isEmpty());
    }

}