package facades;

import dtos.AuctionDTO;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.SetupTestUsers;

import javax.persistence.EntityManagerFactory;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionFacadeTest {


    private static EntityManagerFactory emf;
    private static AuctionFacade instance;
    AuctionDTO auctionDTO;

    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        instance = AuctionFacade.getAuctionFacade(emf);
        SetupTestUsers.setupUsers(emf);
    }


    @Test
    void createAuction() {
        auctionDTO = new AuctionDTO("The big one","2020-05-13","10:14:45","New york");
        auctionDTO = instance.createAuction(auctionDTO);
        assertTrue(auctionDTO.getId() != null);
    }

    @Test
    void getAllAuctions(){
        createAuction();
        createAuction();
        List<AuctionDTO> auctionDTOS = instance.getAllAuction();
        assertTrue(auctionDTOS.size() > 0);
    }

    @Test
    void updateAuction(){
        createAuction();
        AuctionDTO newAuction = new AuctionDTO("New Auction Name","2020-05-13","10:14:45","New york");
        newAuction.setId(1L);
        AuctionDTO newAuctionDTO = instance.updateAuction(newAuction);
        assertTrue(newAuctionDTO.getName().equals("New Auction Name"));

    }
}