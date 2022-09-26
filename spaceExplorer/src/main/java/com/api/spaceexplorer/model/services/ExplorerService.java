package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.enums.ExplorerEnum;
import com.api.spaceexplorer.repositories.ExplorerRepository;
import com.api.spaceexplorer.repositories.PlanetRepository;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ExplorerService {

    private final ExplorerRepository explorerRepository;
    private final PlanetRepository planetRepository;
   public ExplorerService(ExplorerRepository explorerRepository, PlanetRepository planetRepository) {
        this.explorerRepository = explorerRepository;
        this.planetRepository = planetRepository;
    }

    public boolean createExplorerObj(ExplorerDto explorerDto) {

        if (!checkExplorerArgs(explorerDto)){
            return false;
        }
        var planetEntity = planetRepository.findPlanetEntityByPlanetName(explorerDto.getPlanetName());
        if (explorerRepository.existsByExplorerName(explorerDto.getExplorerName()))
            return false;
        var explorerEntity = new ExplorerEntity(
                explorerDto.getExplorerName(),
                ExplorerEnum.valueOf(explorerDto.getDirection()),
                explorerDto.getX(),
                explorerDto.getY(),
                planetEntity.get());
        saveExplorer(explorerEntity);
        return true;
    }

    private static boolean checkExplorerArgs(ExplorerDto explorerDto) {
        if (explorerDto.getPlanetName().matches("\\W*")){
            return false;
        }

        if (explorerDto.getExplorerName().matches("\\W*")){
            return false;
        }
        if (explorerDto.getX() < 0 || explorerDto.getY() < 0){
            return false;
        }
        return validDirection(explorerDto);
    }

    private static boolean validDirection(ExplorerDto explorerDto) {

        switch (explorerDto.getDirection()){
            case "NORTH":
            case "SOUTH":
            case "WEST":
            case "EAST":
                return true;
            default:
                return false;
        }
    }

    @Transactional
    public void saveExplorer(ExplorerEntity explorerEntity) {
        explorerRepository.save(explorerEntity);
    }
}
