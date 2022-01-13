package facades;

import dtos.AuctionDTO;
import entities.Auction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class AuctionFacade {

    private static EntityManagerFactory emf;
    private static AuctionFacade instance;

    private AuctionFacade(){}

    public static AuctionFacade getAuctionFacade(EntityManagerFactory _emf){
        if (instance == null){
            emf = _emf;
            instance = new AuctionFacade();
        }
        return instance;
    }


    public AuctionDTO createAuction(AuctionDTO auctionDTO){
        EntityManager em = emf.createEntityManager();
        Auction auction = new Auction(auctionDTO);

        try{
            em.getTransaction().begin();
            em.persist(auction);
            em.getTransaction().commit();
            return new AuctionDTO(auction);
        } finally {
            em.close();
        }
    }

    public List<AuctionDTO> getAllAuction(){
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Auction> typedQuery = em.createQuery("SELECT a FROM Auction a ", Auction.class);
            List<Auction> auctions = typedQuery.getResultList();
            return AuctionDTO.getDtos(auctions);
        } finally {
            em.close();
        }
    }

}
