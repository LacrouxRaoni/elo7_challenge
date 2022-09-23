package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import org.springframework.stereotype.Service;

@Service
public class ExplorerService {

    public static ExplorerEntity createExplorerObj(ExplorerDto explorerDto) {

        checkExplorerArgs(explorerDto);
        ExplorerEntity explorerEntity = ExplorerEntity.fromExplorerDto(explorerDto);
        return explorerEntity;
    }

    private static boolean checkExplorerArgs(ExplorerDto explorerDto) {
        if (!explorerDto.getPlanetName().matches("\\W*"))
            return false;
        if (!explorerDto.getExplorerName().matches("\\W*"))
            return false;
        if (explorerDto.getX() < 0 || explorerDto.getY() < 0){
            return false;
        }
        return validDirection(explorerDto);
    }

    private static boolean validDirection(ExplorerDto explorerDto) {
        switch (explorerDto.getDirection()){
            case "North":
                return true;
            case "South":
                return true;
            case "West":
                return true;
            case "East":
                return true;
            default:
                return false;
        }
    }
}
