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

    public ExplorerDto() {
    }

    public ExplorerDto(String planetName, String explorerName, String direction, int x, int y) {
        this.planetName = planetName;
        this.explorerName = explorerName;
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public ExplorerDto(String explorerName, String newExplorerName) {
        this.explorerName = explorerName;
        this.newExplorerName = newExplorerName;
    }

    public static ExplorerDto movements(String explorerName, String movement) {
        ExplorerDto move = new ExplorerDto();
        move.explorerName = explorerName;
        move.movement = movement;
        return move;
    }

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
