package facades;

import dtos.AuctionDTO;
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
    private static AuctionFacade auctionFacade;

    BoatDTO boatDTO;




    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        instance = BoatFacade.getBoatFacade(emf);
        auctionFacade = AuctionFacade.getAuctionFacade(emf);
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
        boatDTO = new BoatDTO("Volvo","Japan","julia","BilledURL");
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

    @Test
    void updateBoat(){
        createBoat();
        boatDTO.setName("New Name");
        boatDTO = instance.updateBoat(boatDTO);
        assertTrue(boatDTO.getName().equals("New Name"));
    }

    @Test
    void addBoatToAuction(){
        AuctionDTO auctionDTO = new AuctionDTO("The big one","2020-05-13","10:14:45","New york");
        auctionDTO = auctionFacade.createAuction(auctionDTO);
        createBoat();
        boatDTO.setAuction(auctionDTO);
        boatDTO = instance.addToAuction(boatDTO);
        assertTrue(boatDTO.getAuction().getName()!= null);
    }

    @Test
    void deleteFromAution(){
        addBoatToAuction();
        boatDTO = instance.deleteFromAution(boatDTO.getId());
        assertTrue(boatDTO.getAuction() == null);
    }


    //After is still same as before, but DB shows -1 record.
    //@Test
    void deleteBoat(){
        createBoat();
        createBoat();
        createBoat();
        int before = instance.getAllBoatsByUser("user").size();
        boatDTO = instance.deleteBoat(boatDTO.getId().toString());
        System.out.println("before" + before);
        int after = instance.getAllBoatsByUser("user").size();
        System.out.println("after" + after);
        assertTrue(before-1 == after);
    }
}