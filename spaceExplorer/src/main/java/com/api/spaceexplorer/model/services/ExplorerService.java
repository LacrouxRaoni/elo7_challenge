package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.enums.ExplorerEnum;
import com.api.spaceexplorer.repositories.ExplorerRepository;
import com.api.spaceexplorer.repositories.PlanetRepository;
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

    public void createExplorerObj(ExplorerDto explorerDto) {

        checkExplorerArgs(explorerDto);
        if (existByExplorerName(explorerDto.getExplorerName()))
            throw new ExplorerException("Explorer already exists in Data Base");
        var planetEntity = planetRepository.findPlanetEntityByPlanetName(explorerDto.getPlanetName());
        if (planetEntity.isEmpty()) {
            throw new ExplorerException("Can't find planet in Data Base");
        }
        var explorerEntity = new ExplorerEntity(
                explorerDto.getExplorerName(),
                ExplorerEnum.valueOf(explorerDto.getDirection()),
                explorerDto.getX(),
                explorerDto.getY(),
                planetEntity.get());
        saveExplorer(explorerEntity);
    }
    private boolean existByExplorerName(String explorerName) {
        return explorerRepository.existsByExplorerName(explorerName);
    }

    private static void checkExplorerArgs(ExplorerDto explorerDto) {
        if (explorerDto.getPlanetName().matches("\\W*")){
            throw new ExplorerException("Planet name should contains AlphaNumeric characters only");
        }
        if (explorerDto.getExplorerName().matches("\\W*")){
            throw new ExplorerException("Explorer name should contains AlphaNumeric characters only");
        }
        if (explorerDto.getX() < 0 || explorerDto.getY() < 0){
            throw new ExplorerException("axis x and y can't be less than 0");
        }
        validDirection(explorerDto);
    }

    private static void validDirection(ExplorerDto explorerDto) {

        switch (explorerDto.getDirection()){
            case "NORTH":
            case "SOUTH":
            case "WEST":
            case "EAST":
                break ;
            default:
                throw new ExplorerException("Cardinals must be in Uppercase eg:NORTH");
        }
    }
    @Transactional
    public void saveExplorer(ExplorerEntity explorerEntity) {
        explorerRepository.save(explorerEntity);
    }
}
