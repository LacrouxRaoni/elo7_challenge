package com.api.spaceexplorer.model.entities;

import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.dtos.PlanetDto;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "planet")
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
    private Integer explorerAmountLimit;

    @OneToMany(mappedBy = "planet", cascade = javax.persistence.CascadeType.ALL)
    private List<ExplorerEntity> explorers = new ArrayList<>();

    public PlanetEntity() {}

    public PlanetEntity(String planetName, int width, int height, int explorerAmount) {
        this.planetName = planetName;
        this.width = width;
        this.height = height;
        this.explorerAmountLimit = explorerAmount;
    }

    public static PlanetEntity fromPlanetDto(PlanetDto planetDto){
        PlanetEntity planet = new PlanetEntity(planetDto.getPlanetName(),
                planetDto.getWidth(),
                planetDto.getHeight(),
                planetDto.getWidth() * planetDto.getHeight());
        return planet;
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

    public void setExplorer(List<ExplorerEntity> explorer) {
        this.explorers = explorer;
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
        return "PlanetEntity{" +
                "id=" + id +
                ", planetName='" + planetName + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", explorerAmount=" + explorerAmountLimit +
                '}';
    }
}
