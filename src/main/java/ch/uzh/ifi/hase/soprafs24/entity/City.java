package ch.uzh.ifi.hase.soprafs24.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class City {


    @Id
    private Long id;
    private String name;
    private String capital;
    private double longitude;
    private double latitude;

    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

}