package com.api.spaceexplorer.model.dtos;

import javax.validation.constraints.NotBlank;

public class ExplorerDto {

    private String planetName;
    @NotBlank
    private String explorerName;

    private String newExplorerName;

    private String movement;

    private String direction;

    private int x;

    private int y;

    public String getPlanetName() {
        return planetName;
    }

    public String getExplorerName() {
        return explorerName;
    }

    public String getNewExplorerName() {
        return newExplorerName;
    }

    public String getMovement() {
        return movement;
    }

    public String getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
