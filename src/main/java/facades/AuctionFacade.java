package facades;

import dtos.AuctionDTO;
import entities.Auction;
import entities.Boat;

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

    //TODO Creates new record from Rest Endpoint, but not from Test.
    public AuctionDTO updateAuction(AuctionDTO auctionDTO){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Auction auction = em.find(Auction.class,auctionDTO.getId());
            auction.updateFromDTO(auctionDTO);
            Auction newAuction = em.merge(auction);
            em.getTransaction().commit();
            System.out.println(newAuction.getId());
            return new AuctionDTO(newAuction);
        } finally {
            em.close();
        }


    }

}
