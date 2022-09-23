package com.api.spaceexplorer.model.dtos;

import javax.validation.constraints.NotBlank;

public class ExplorerDto {

    @NotBlank
    private String planetName;
    @NotBlank
    private String explorerName;
    @NotBlank
    private String movement;
    @NotBlank
    private String direction;
    @NotBlank
    private int x;
    @NotBlank
    private int y;

    public String getPlanetName() {
        return planetName;
    }

    public String getExplorerName() {
        return explorerName;
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
