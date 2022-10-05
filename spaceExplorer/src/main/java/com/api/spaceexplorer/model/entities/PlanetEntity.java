package com.api.spaceexplorer.model.entities;

import com.api.spaceexplorer.model.dtos.PlanetDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "planets")
public class PlanetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "planet_name")
    private String planetName;
    @Column(name = "size_y")
    private int width;
    @Column(name = "size_x")
    private int height;
    @Column(name = "explorer_amount_limit")
    private Integer explorerAmountLimit;
    @Column(name = "explorer_capacity")
    private Integer explorerAmount;

    @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL)
    private List<ExplorerEntity> explorers = new ArrayList<>();

    public PlanetEntity() {}

    public PlanetEntity(String planetName, int width, int height, int explorerAmount) {
        this.planetName = planetName;
        this.width = width;
        this.height = height;
        this.explorerAmountLimit = explorerAmount;
        this.explorerAmount = 0;
    }

    public static PlanetEntity fromPlanetDto(PlanetDto planetDto){
        PlanetEntity planet = new PlanetEntity(planetDto.getPlanetName(),
                planetDto.getWidth(),
                planetDto.getHeight(),
                planetDto.getWidth() * planetDto.getHeight());
        return planet;
    }

    public void sumExplorerAmount() {
        this.explorerAmount += + 1;
    }

    public void decExplorerAmount() {
        this.explorerAmount += - 1;
    }

    public void changePlanetName(String newPlanetName) {
        this.planetName = newPlanetName;
    }

    public Integer getId() {
        return id;
    }

    public String getPlanetName() {
        return planetName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getExplorerAmountLimit() {
        return explorerAmountLimit;
    }


    public List<ExplorerEntity> getExplorer() {
        return explorers;
    }

    public Integer getExplorerAmount() {
        return explorerAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanetEntity that = (PlanetEntity) o;
        return width == that.width && height == that.height && explorerAmountLimit == that.explorerAmountLimit && Objects.equals(id, that.id) && Objects.equals(planetName, that.planetName) && Objects.equals(explorers, that.explorers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, planetName, width, height, explorerAmountLimit, explorers);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("PlanetInfo{");
        sb.append("\nid= ");
        sb.append(id);
        sb.append("\nplanetName= ");
        sb.append(planetName);
        sb.append("\nwidth= ");
        sb.append(width);
        sb.append("\nheight= ");
        sb.append(height);
        sb.append("\nexplorerAmountInPlanet= ");
        sb.append(explorerAmount);
        sb.append("\nexplorerAmountLimit= ");
        sb.append(explorerAmountLimit);
        sb.append("\nexplorers in planet: \n");
        for (ExplorerEntity c : explorers){
            sb.append("explorer= ");
            sb.append(c.getExplorerName() + "\n");
        }
        sb.append('}');
        return sb.toString();
    }
}
