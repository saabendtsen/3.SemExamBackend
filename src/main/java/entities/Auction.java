package entities;

import dtos.AuctionDTO;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Table(name = "auction")
@Entity
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Basic
    private LocalDate date;
    @Basic
    private LocalTime time;
    private String location;

    @OneToMany(mappedBy = "auction")
    private List<Boat> boatList = new ArrayList<>();


    public Auction() {
    }

    public Auction(AuctionDTO auctionDTO) {

        if(auctionDTO.getId() != null){
            this.id = auctionDTO.getId();
        }
        this.name = auctionDTO.getName();
        this.date = LocalDate.parse(auctionDTO.getDate());
        this.time = LocalTime.parse(auctionDTO.getTime());
        this.location = auctionDTO.getLocation();
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Boat> getBoatList() {
        return boatList;
    }

    public void setBoatList(List<Boat> boatList) {
        this.boatList = boatList;
    }
}
