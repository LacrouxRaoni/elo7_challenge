package com.api.spaceexplorer.model.entities;

import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.enums.ExplorerEnum;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "explorers")
public class ExplorerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "explorer_name", nullable = false)
    private String explorerName;
    @Column(name = "movements")
    private String movement;

    @Column(name = "direction")
    @Enumerated(EnumType.STRING)
    private ExplorerEnum direction;
    @Column(name = "coo_x", nullable = false)
    private int x;
    @Column(name = "coo_y", nullable = false)
    private int y;

    @ManyToOne()
    private PlanetEntity planet;

    public ExplorerEntity() {
    }

    public ExplorerEntity(String explorerName, ExplorerEnum direction, int x, int y) {
        this.explorerName = explorerName;
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public ExplorerEntity(String explorerName, ExplorerEnum direction, int x, int y, PlanetEntity planet) {
        this.explorerName = explorerName;
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.planet = planet;
    }

    public static ExplorerEntity fromExplorerDto(ExplorerDto explorerDto, PlanetEntity planetEntity){
        ExplorerEntity explorer = new ExplorerEntity(
                                    explorerDto.getExplorerName(),
                                    ExplorerEnum.valueOf(explorerDto.getDirection()),
                                    explorerDto.getX(),
                                    explorerDto.getY(),
                                    planetEntity);
        return explorer;
    }

    public void changeExplorerName(String explorerNewName) {
        this.explorerName = explorerNewName;
    }

    public void changeExplorerDirection(ExplorerEnum cardinal) {
        this.direction = cardinal;
    }

    public void axisUpdate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public String getExplorerName() {
        return explorerName;
    }

    public String getMovement() {
        return movement;
    }

    public ExplorerEnum getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PlanetEntity getPlanet() {
        return planet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExplorerEntity that = (ExplorerEntity) o;
        return x == that.x && y == that.y && Objects.equals(id, that.id) && Objects.equals(explorerName, that.explorerName) && Objects.equals(movement, that.movement) && direction == that.direction && Objects.equals(planet, that.planet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, explorerName, movement, direction, x, y, planet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Explorer{");
        sb.append("\nid= ");
        sb.append(id);
        sb.append("\nexplorerName= ");
        sb.append(explorerName);
        sb.append("\naxis x= ");
        sb.append(x);
        sb.append("\naxis y= ");
        sb.append(y);
        sb.append("\ncardinal= ");
        sb.append(direction);
        sb.append("\nplanetName= ");
        sb.append(getPlanet().getPlanetName() + "\n");
        sb.append('}');
        return sb.toString();
    }
}
