package com.api.spaceexplorer.model.entities;

public class ExplorerEntity {

    private String explorerName;
    private String movement;
    private char direction;
    private int x;
    private int y;

    public ExplorerEntity(String explorerName, String movement, char direction, int x, int y) {
        this.explorerName = explorerName;
        this.movement = movement;
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public String getExplorerName() {
        return explorerName;
    }

    public String getMovement() {
        return movement;
    }

    public char getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
