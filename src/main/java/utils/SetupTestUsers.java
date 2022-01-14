package utils;


import dtos.AuctionDTO;
import entities.Role;
import entities.User;
import facades.AuctionFacade;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {
  private static EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

  public static void main(String[] args) {
  setupUsers(emf);
  }

  public static void setupUsers(EntityManagerFactory emf) {

    EntityManager em = emf.createEntityManager();

    User user = new User("user", "user1","Niller");
    User user1 = new User("user1", "user1","Kristian");
    User admin = new User("admin", "admin1");
    User both = new User("user_admin", "test");
    AuctionDTO auctionDTO = new AuctionDTO("The big one","2020-05-13","10:14:45","New york");

    try {
      em.getTransaction().begin();
      Role userRole = new Role("user");
      Role adminRole = new Role("admin");
      user.addRole(userRole);
      user1.addRole(userRole);
      admin.addRole(adminRole);
      user.setAddress("Bøgevej");
      user.setPhone("31546576");
      both.addRole(userRole);
      both.addRole(adminRole);
      em.persist(userRole);
      em.persist(adminRole);
      em.persist(user);
      em.persist(user1);
      em.persist(admin);
      em.persist(both);
      em.getTransaction().commit();
      System.out.println("Users Created!");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
