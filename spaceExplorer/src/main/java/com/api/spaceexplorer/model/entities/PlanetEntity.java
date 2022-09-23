package com.api.spaceexplorer.model.entities;

import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.dtos.PlanetDto;

public class PlanetEntity {
    private String planetName;
    private int width;
    private int height;
    private int explorerAmount;

    public PlanetEntity() {}

    public PlanetEntity(String planetName, int width, int height, int explorerAmount) {
        this.planetName = planetName;
        this.width = width;
        this.height = height;
        this.explorerAmount = explorerAmount;
    }

    public static PlanetEntity fromPlanetDto(PlanetDto planetDto){
        PlanetEntity planet = new PlanetEntity(planetDto.getPlanetName(),
                planetDto.getWidth(),
                planetDto.getHeight(),
                planetDto.getWidth() * planetDto.getHeight());
        return planet;
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

    public int getExplorerAmount() {
        return explorerAmount;
    }
}
