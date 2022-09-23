package com.api.spaceexplorer.model.entities;

import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.enums.ExplorerEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ExplorerEntity {

    private String explorerName;
    private String movement;
    @Enumerated(EnumType.STRING)
    private ExplorerEnum direction;
    private int x;
    private int y;

    public ExplorerEntity(String explorerName, ExplorerEnum direction, int x, int y) {
        this.explorerName = explorerName;
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public static ExplorerEntity fromExplorerDto(ExplorerDto explorerDto){
        ExplorerEntity explorer = new ExplorerEntity(explorerDto.getExplorerName(),
                                                            ExplorerEnum.valueOf(explorerDto.getDirection()),
                                                            explorerDto.getX(),
                                                            explorerDto.getY());
        return explorer;
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
}
