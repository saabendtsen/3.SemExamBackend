package entities;

import dtos.BoatDTO;
import dtos.UserDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "boat")
@Entity
public class Boat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String make;
    private String name;
    private String image;


    @ManyToMany(mappedBy = "boats")
    private List<User> owner;

    @ManyToOne
    private Auction auction;



    public Boat() {
    }

    public Boat(String brand, String make, String name, String image) {
        this.brand = brand;
        this.make = make;
        this.name = name;
        this.image = image;
    }

    public Boat(BoatDTO boatDTO) {
        if (boatDTO.getId() != null){
            this.id = boatDTO.getId();
        }
        this.brand = boatDTO.getBrand();
        this.make = boatDTO.getMake();
        this.name = boatDTO.getName();
        this.image = boatDTO.getImage();
        this.owner = new ArrayList<>();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<User> getOwner() {
        return owner;
    }

    public void setOwner(List<User> owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public void addOwner(User u) {
        this.owner.add(u);
        u.addBoat(this);
    }
}