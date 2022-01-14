package dtos;

import entities.Auction;
import java.util.ArrayList;
import java.util.List;

public class AuctionDTO {

    private Long id;
    private String name;
    private String date;
    private String time;
    private String location;
    private List<BoatDTO> boatList;


    public AuctionDTO() {
    }

    public AuctionDTO(String name, String date, String time, String location) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public AuctionDTO(Auction auction) {
        if(auction.getId() != null) {
            this.id = auction.getId();
        }
        this.name = auction.getName();
        this.date = auction.getDate().toString();
        this.time = auction.getTime().toString();
        this.location = auction.getLocation();
        this.boatList = BoatDTO.getDtos(auction.getBoatList());
    }

    public AuctionDTO(String name) {
        this.name = name;
    }

    public static List<AuctionDTO> getDtos(List<Auction> b){
        List<AuctionDTO> auctionDTOSdtos = new ArrayList();
        b.forEach(um -> auctionDTOSdtos.add(new AuctionDTO(um)));
        return auctionDTOSdtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<BoatDTO> getBoatList() {
        return boatList;
    }

    public void setBoatList(List<BoatDTO> boatList) {
        this.boatList = boatList;
    }
}
