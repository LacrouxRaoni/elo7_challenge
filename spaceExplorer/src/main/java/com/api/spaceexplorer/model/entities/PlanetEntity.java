package com.api.spaceexplorer.model.entities;

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
