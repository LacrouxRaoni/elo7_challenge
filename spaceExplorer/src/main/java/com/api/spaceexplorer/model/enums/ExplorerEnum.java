package com.api.spaceexplorer.model.enums;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.model.entities.ExplorerEntity;

public enum ExplorerEnum {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static String validCardinal(String cardinal) {
        String cardinalUpperCase = cardinal.toUpperCase();
        switch (cardinalUpperCase){
            case "NORTH":
            case "SOUTH":
            case "WEST":
            case "EAST":
                break ;
            default:
                throw new ExplorerException("Invalid Cardinal");
        }
        return cardinalUpperCase;
    }

    public static void turnRight(ExplorerEntity explorer) {

        switch (explorer.getDirection()){
            case NORTH:
                explorer.changeExplorerDirection(ExplorerEnum.WEST);
                break ;
            case WEST:
                explorer.changeExplorerDirection(ExplorerEnum.SOUTH);
                break ;
            case SOUTH:
                explorer.changeExplorerDirection(ExplorerEnum.EAST);
                break ;
            case EAST:
                explorer.changeExplorerDirection(ExplorerEnum.NORTH);
                break ;
        }
    }

    public static void turnLeft(ExplorerEntity explorer) {

        switch (explorer.getDirection()){
            case NORTH:
                explorer.changeExplorerDirection(ExplorerEnum.EAST);
                break ;
            case EAST:
                explorer.changeExplorerDirection(ExplorerEnum.SOUTH);
                break ;
            case SOUTH:
                explorer.changeExplorerDirection(ExplorerEnum.WEST);
                break ;
            case WEST:
                explorer.changeExplorerDirection(ExplorerEnum.NORTH);
                break ;
        }
    }
}
