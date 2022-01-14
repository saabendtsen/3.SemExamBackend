package dtos;

import entities.Auction;
import entities.Boat;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class BoatDTO {

    private Long id;
    private String brand;
    private String make;
    private String name;
    private String image;
    private List<UserDTO> owners = new ArrayList();
    private AuctionDTO auction;


    public BoatDTO() {
    }

    public BoatDTO(Boat boat) {
        if(boat.getId() != null){
            this.id = boat.getId();
        }
        this.brand = boat.getBrand();
        this.make = boat.getMake();
        this.name = boat.getName();
        this.image = boat.getImage();
        this.owners = UserDTO.getDtos(boat.getOwner());

        if(boat.getAuction() != null) {
            this.auction = new AuctionDTO(boat.getAuction().getName());
        } else {
            this.auction = null;
        }
    }

    public BoatDTO(String brand, String make, String name, String image) {
        this.brand = brand;
        this.make = make;
        this.name = name;
        this.image = image;
        this.owners = new ArrayList<>();
    }

    public static List<BoatDTO> getDtos(List<Boat> b){
        List<BoatDTO> boatDTOSdtos = new ArrayList();
        b.forEach(um -> boatDTOSdtos.add(new BoatDTO(um)));
        return boatDTOSdtos;
    }

    public AuctionDTO getAuction() {
        return auction;
    }

    public void setAuction(AuctionDTO auction) {
        this.auction = auction;
    }

    public List<UserDTO> getOwners() {
        return owners;
    }

    public void setOwners(List<UserDTO> owners) {
        this.owners = owners;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
