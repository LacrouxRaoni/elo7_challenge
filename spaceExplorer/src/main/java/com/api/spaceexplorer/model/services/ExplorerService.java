package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import com.api.spaceexplorer.model.enums.ExplorerEnum;
import com.api.spaceexplorer.repositories.ExplorerRepository;
import com.api.spaceexplorer.repositories.PlanetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ExplorerService {

    private final ExplorerRepository explorerRepository;
    private final PlanetRepository planetRepository;
    public ExplorerService(ExplorerRepository explorerRepository, PlanetRepository planetRepository) {
        this.explorerRepository = explorerRepository;
        this.planetRepository = planetRepository;
    }


    private Optional<PlanetEntity> locatePlanet(String planetName) {
        var planetEntity = planetRepository.findPlanetEntityByPlanetName(planetName);
        if (!planetEntity.isPresent()) {
            throw new ExplorerException("Can't find planet in Data Base");
        }
        return planetEntity;
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
    }

    private static void validDirection(ExplorerDto explorerDto) {

        if (explorerDto.getX() < 0 || explorerDto.getY() < 0){
            throw new ExplorerException("axis x and y can't be less than 0");
        }
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

    public void createExplorerObj(ExplorerDto explorerDto) {

        checkExplorerArgs(explorerDto);
        validDirection(explorerDto);
        if (existByExplorerName(explorerDto.getExplorerName()))
            throw new ExplorerException("Explorer already exists in Data Base");
        var planetEntity = locatePlanet(explorerDto.getPlanetName());
        var explorerEntity = new ExplorerEntity(
                explorerDto.getExplorerName(),
                ExplorerEnum.valueOf(explorerDto.getDirection()),
                explorerDto.getX(),
                explorerDto.getY(),
                planetEntity.get());
        planetEntity.get().sumExplorerAmount();
        if (planetEntity.get().getExplorerAmount() <= planetEntity.get().getExplorerAmountLimit())
            saveExplorer(explorerEntity);
        else
            throw new ExplorerException("Planet is full");
    }

    public void validAndDeleteExplorer(ExplorerDto explorerDto) {
        checkExplorerArgs(explorerDto);
        var planetEntity = locatePlanet(explorerDto.getPlanetName());
        var explorerEntity = explorerRepository.findExplorerEntityByExplorerName(explorerDto.getExplorerName());
        if (!planetEntity.isPresent())
            throw new ExplorerException("Planet didn't found in data base");
        if (!explorerEntity.isPresent())
            throw new ExplorerException("Explorer didn't found in data base");
        explorerEntity.get().getPlanet().decExplorerAmount();
        deleteExplorer(explorerEntity.get());
    }

    @Transactional
    public void saveExplorer(ExplorerEntity explorerEntity) {
        explorerRepository.save(explorerEntity);
    }

    @Transactional
    public void deleteExplorer(ExplorerEntity explorerEntity) {
       explorerRepository.delete(explorerEntity); }

}
