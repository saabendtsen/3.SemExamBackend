package facades;

import dtos.BoatDTO;
import dtos.UserDTO;
import entities.Auction;
import entities.Boat;
import entities.User;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class BoatFacade {

    private static EntityManagerFactory emf;
    private static BoatFacade instance;

    private BoatFacade(){}

    public static BoatFacade getBoatFacade(EntityManagerFactory _emf){
        if (instance == null){
            emf = _emf;
            instance = new BoatFacade();
        }
        return instance;
    }

    public BoatDTO createBoat(BoatDTO boatDTO){
        EntityManager em = emf.createEntityManager();
        Boat boat = new Boat(boatDTO);

        try{
            em.getTransaction().begin();
            for(UserDTO u : boatDTO.getOwners()){
                User owner = em.find(User.class, u.getUsername());
                boat.addOwner(owner);
            }
            em.persist(boat);
            em.getTransaction().commit();
            return new BoatDTO(boat);
        } finally {
            em.close();
        }
    }

    public BoatDTO deleteBoat(String id){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Boat boat = em.find(Boat.class,Long.parseLong(id));
            em.remove(boat);
            em.getTransaction().commit();
            return new BoatDTO(boat);
        } finally {
            em.close();
        }
    }

    public List<BoatDTO> getAllBoatsByUser(String userName){
        EntityManager em = emf.createEntityManager();
        User user;

        try {
            em.getTransaction().begin();
            user = em.find(User.class,userName);
            em.getTransaction().commit();
            return BoatDTO.getDtos(user.getBoats());
        } finally {
            em.close();
        }
    }


    //TODO: Does not handle updated Owners ATM
    public BoatDTO updateBoat (BoatDTO boatDTO){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Boat boat = em.find(Boat.class,boatDTO.getId());
            boat.updateFromDto(boatDTO);
            boat = em.merge(boat);
            em.getTransaction().commit();
            return new BoatDTO(boat);
        } finally {
            em.close();
        }
    }

    public BoatDTO addToAuction(BoatDTO boatDTO){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Auction auction = em.find(Auction.class,boatDTO.getAuction().getId());
            Boat boat = em.find(Boat.class,boatDTO.getId());
            boat.addAuction(auction);
            Boat newBoat = em.merge(boat);
            em.getTransaction().commit();
            System.out.println(newBoat.getAuction().getName() + " from Facade");
            return new BoatDTO(newBoat);
        } finally {
            em.close();
        }
    }

    public BoatDTO deleteFromAution(Long id){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Boat boat = em.find(Boat.class,id);
            boat.removeAuction();
            boat = em.merge(boat);
            em.getTransaction().commit();
            System.out.println(boat.getAuction());
            return new BoatDTO(boat);
        } finally {
            em.close();
        }
    }
}
