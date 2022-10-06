package com.api.spaceexplorer.model.services;

import com.api.spaceexplorer.controller.exceptions.ExplorerException;
import com.api.spaceexplorer.model.dtos.ExplorerDto;
import com.api.spaceexplorer.model.entities.ExplorerEntity;
import com.api.spaceexplorer.model.entities.PlanetEntity;
import com.api.spaceexplorer.model.enums.ExplorerEnum;
import com.api.spaceexplorer.repositories.ExplorerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ExplorerService {

    private final ExplorerRepository explorerRepository;
    private final PlanetService planetService;

    public ExplorerService(ExplorerRepository explorerRepository, PlanetService planetService) {
        this.explorerRepository = explorerRepository;
        this.planetService = planetService;
    }

    private Optional<ExplorerEntity> checkIfExplorerExist(String explorerName){
        return explorerRepository.findExplorerEntityByExplorerName(explorerName);

    }

    private static void checkExplorerArgs(String args) {
        if (args.matches("\\W*")){
            throw new ExplorerException("Arguments should contains AlphaNumeric characters");
        }
    }

    private static void validAxisOutOfBounds(ExplorerDto explorerDto, PlanetEntity planetEntity) {
        if (explorerDto.getX() < 1 || explorerDto.getY() < 1)
            throw new ExplorerException("axis x and y can't be less than 1");
        if (explorerDto.getX() > planetEntity.getHeight())
            throw new ExplorerException("axis x out of planet area");
        if (explorerDto.getY() > planetEntity.getWidth())
            throw new ExplorerException("axis y out of planet area");
    }

    private static void validAxisInSamePosition(ExplorerDto explorerDto, PlanetEntity planetEntity) {
        List<ExplorerEntity> explorers = planetEntity.getExplorer();
        for (ExplorerEntity c : explorers){
            if (c.getX() == explorerDto.getX() && c.getY() == explorerDto.getY())
                throw new ExplorerException("This explorer can't be registered in this position");
        }
    }

    private static void validDirection(ExplorerDto explorerDto, PlanetEntity planetEntity) {
        validAxisOutOfBounds(explorerDto, planetEntity);
        validAxisInSamePosition(explorerDto, planetEntity);
    }

    private static void validObjArgs(ExplorerDto explorer, PlanetEntity planet){
        checkExplorerArgs(explorer.getExplorerName());
        checkExplorerArgs(explorer.getPlanetName());
        validDirection(explorer, planet);
    }

    public void prepareToCreateExplorerObj(ExplorerDto explorerDto) {
        var planetEntity = planetService.locatePlanet(explorerDto.getPlanetName());
        if(checkIfExplorerExist(explorerDto.getExplorerName()).isPresent())
            throw new ExplorerException("Explorer already exists in Data Base");
        planetService.planetCapacity(planetEntity.get());
        var explorerEntity = ExplorerEntity.fromExplorerDto(explorerDto,
                                                planetEntity.get(),
                                                ExplorerEnum.validCardinal(explorerDto.getDirection()));
        validObjArgs(explorerDto, planetEntity.get());
        planetEntity.get().sumExplorerAmount();
        saveExplorer(explorerEntity);
    }

    public String findAllExplorers() {
        StringBuilder sb = new StringBuilder();
        List<ExplorerEntity> explorers = explorerRepository.findAll();
        sb.append("Explorers{\n");
        for (ExplorerEntity e : explorers){
            sb.append("ExplorerName: ");
            sb.append(e.getExplorerName()).append("\n");
        }
        sb.append('}');
       return sb.toString();
    }

    public ExplorerEntity getExplorerObject(ExplorerDto explorerDto) {
        var explorerEntity = checkIfExplorerExist(explorerDto.getExplorerName());
        if (explorerEntity.isEmpty()){
            throw new ExplorerException("Explorer not found in data base");
        }
        return explorerEntity.get();
    }

    private void validatePutNameArgs(ExplorerDto explorerDto) {

        if (checkIfExplorerExist(explorerDto.getExplorerName()).isEmpty())
            throw new ExplorerException("Explorer not found in data base");
        if (checkIfExplorerExist(explorerDto.getNewExplorerName()).isPresent())
            throw new ExplorerException("Explorer already exist in Data Base");
        if (explorerDto.getNewExplorerName().matches("\\W*"))
            throw new ExplorerException("Explorer name should contain AlphaNumeric characters only");
    }

    public ExplorerEntity validAndModifyName(ExplorerDto explorerDto) {

        validatePutNameArgs(explorerDto);
        var explorer = checkIfExplorerExist(explorerDto.getExplorerName());
        explorer.get().changeExplorerName(explorerDto.getNewExplorerName());
        saveExplorer(explorer.get());
        return explorer.get();
    }

    private void validateMoveArgs(ExplorerDto explorerDto) {
        checkExplorerArgs(explorerDto.getExplorerName());
        if (!explorerDto.getMovement().matches("[LMR]*"))
            throw new ExplorerException("move sequence must be L, M, R in Uppercase");
    }

    private void moveExplorer(String movement, ExplorerEntity explorer) {
        for (int i = 0; i < movement.length(); i++){
            if(movement.charAt(i) == 'L'){
                ExplorerEnum.turnLeft(explorer);
            }
            else if (movement.charAt(i) == 'R'){
                ExplorerEnum.turnRight(explorer);
            }
            else if (movement.charAt(i) == 'M'){
                moveForward(explorer);
            }
        }
    }

    private void moveForward(ExplorerEntity explorer) {

        var planetData = explorer.getPlanet();
        var explorers = planetData.getExplorer();
        switch (explorer.getDirection()){
            case NORTH:
                moveNorth(explorer, explorers, planetData);
                break ;
            case WEST:
                moveWest(explorer, explorers, planetData);
                break ;
            case SOUTH:
                moveSouth(explorer, explorers, planetData);
                break ;
            case EAST:
                moveEast(explorer, explorers, planetData);
                break ;
        }
    }

    private void moveNorth(ExplorerEntity explorer, List<ExplorerEntity> explorers, PlanetEntity planetData) {

        if (explorer.getY() == planetData.getHeight()){
            if (planetService.checkInExplorerList(1, explorer.getX(), explorers)){
                explorer.axisUpdate(1, explorer.getX());
            }
        } else if (planetService.checkInExplorerList(explorer.getY() + 1, explorer.getX(), explorers)){
                explorer.axisUpdate(explorer.getY() + 1, explorer.getX());
        }
    }

    private void moveEast(ExplorerEntity explorer, List<ExplorerEntity> explorers, PlanetEntity planetData) {
        if (explorer.getX() == planetData.getWidth()){
            if (planetService.checkInExplorerList(explorer.getY(), 1, explorers)) {
                explorer.axisUpdate(explorer.getY(), 1);
            }
        } else if (planetService.checkInExplorerList(explorer.getY(), explorer.getX() + 1, explorers)){
            explorer.axisUpdate(explorer.getY(), explorer.getX() + 1);
        }
    }

    private void moveSouth(ExplorerEntity explorer, List<ExplorerEntity> explorers, PlanetEntity planetData) {
        if (explorer.getY() == 1){
            if (planetService.checkInExplorerList(planetData.getHeight(), explorer.getX(), explorers)){
                explorer.axisUpdate(planetData.getHeight(), explorer.getX());
            }
        } else if (planetService.checkInExplorerList(explorer.getY() - 1, explorer.getX(), explorers)){
            explorer.axisUpdate(explorer.getY() - 1, explorer.getX());
        }
    }

    private void moveWest(ExplorerEntity explorer, List<ExplorerEntity> explorers, PlanetEntity planetData) {
        if (explorer.getX() == 1){
            if (planetService.checkInExplorerList(explorer.getY(), planetData.getWidth(), explorers)){
                explorer.axisUpdate(explorer.getY(), planetData.getWidth());
            }
        } else if (planetService.checkInExplorerList(explorer.getY(), explorer.getX() - 1, explorers)){
            explorer.axisUpdate(explorer.getY(), explorer.getX() - 1);
        }
    }

    public ExplorerEntity validAndMoveExplorer(ExplorerDto explorerDto) {
        validateMoveArgs(explorerDto);
        var explorer = checkIfExplorerExist(explorerDto.getExplorerName());
        if (explorer.isEmpty())
            throw new ExplorerException("Explorer not found in data base");
        moveExplorer(explorerDto.getMovement(), explorer.get());
        saveExplorer(explorer.get());
        return explorer.get();
    }

    public void validAndDeleteExplorer(ExplorerDto explorerDto) {
        checkExplorerArgs(explorerDto.getExplorerName());
        var explorerEntity = checkIfExplorerExist(explorerDto.getExplorerName());
        if (explorerEntity.isEmpty())
            throw new ExplorerException("Explorer not found in data base");
        explorerEntity.get().getPlanet().decExplorerAmount();
        deleteExplorer(explorerEntity.get());
    }

    @Transactional
    public void saveExplorer(ExplorerEntity explorerEntity) {
        explorerRepository.save(explorerEntity);
    }

    @Transactional
    public void deleteExplorer(ExplorerEntity explorerEntity) {
        explorerRepository.delete(explorerEntity);
    }
}
