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
import java.util.List;
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

    private static void validAxisOutOfBounds(ExplorerDto explorerDto, PlanetEntity planetEntity) {

        if (explorerDto.getX() < 0 || explorerDto.getY() < 0)
            throw new ExplorerException("axis x and y can't be less than 0");
        if (explorerDto.getX() > planetEntity.getHeight() - 1)
            throw new ExplorerException("axis x out of planet area");
        if (explorerDto.getY() > planetEntity.getWidth() - 1)
            throw new ExplorerException("axis y out of planet area");
    }

    private static void validAxisInSamePosition(ExplorerDto explorerDto, PlanetEntity planetEntity) {
        List<ExplorerEntity> explorers = planetEntity.getExplorer();
        for (ExplorerEntity c : explorers){
            if (c.getX() == explorerDto.getX() && c.getY() == explorerDto.getY())
                throw new ExplorerException("This explorer can't be registered in this position");
        }
    }

    private static void validCardinal(String cardinal) {

        switch (cardinal){
            case "NORTH":
            case "SOUTH":
            case "WEST":
            case "EAST":
                break ;
            default:
                throw new ExplorerException("Cardinals must be in Uppercase eg:NORTH");
        }
    }

    private static void validDirection(ExplorerDto explorerDto, PlanetEntity planetEntity) {

        validAxisOutOfBounds(explorerDto, planetEntity);
        validAxisInSamePosition(explorerDto, planetEntity);
        validCardinal(explorerDto.getDirection());
    }

    private Optional<PlanetEntity> validPlanetExplorerExistence(ExplorerDto explorerDto) {
        if (existByExplorerName(explorerDto.getExplorerName()))
            throw new ExplorerException("Explorer already exists in Data Base");
        var planetEntity = locatePlanet(explorerDto.getPlanetName());
        return planetEntity;
    }

    private void createExplorerObj(ExplorerDto explorerDto, ExplorerEntity explorerEntity, Optional<PlanetEntity> planetEntity) {

            planetEntity.get().sumExplorerAmount();
            saveExplorer(explorerEntity);
    }
    public void prepareToCreateExplorerObj(ExplorerDto explorerDto) {

        var planetEntity = validPlanetExplorerExistence(explorerDto);
        if (planetEntity.get().getExplorerAmount() <= planetEntity.get().getExplorerAmountLimit()) {
            var explorerEntity = ExplorerEntity.fromExplorerDto(explorerDto, planetEntity.get());
            checkExplorerArgs(explorerDto);
            validDirection(explorerDto, planetEntity.get());
            createExplorerObj(explorerDto, explorerEntity, planetEntity);
        }
    }


    public String findAllExplorers() {

        StringBuilder sb = new StringBuilder();
        List<ExplorerEntity> explorers = explorerRepository.findAll();
        sb.append("Explorers{\n");
        for (ExplorerEntity e : explorers){
            sb.append("ExplorerName: ");
            sb.append(e.getExplorerName() + "\n");
        }
        sb.append('}');
       return sb.toString();
    }

    public ExplorerEntity getExplorerObject(ExplorerDto explorerDto) {
        var explorerEntity = explorerRepository.findExplorerEntityByExplorerName(explorerDto.getExplorerName());
        if (!explorerEntity.isPresent())
            throw new ExplorerException("Explorer not found in data base\"");
        return explorerEntity.get();
    }

    private void validatePutNameArgs(ExplorerDto explorerDto) {

        if (!existByExplorerName(explorerDto.getExplorerName()))
            throw new ExplorerException("Explorer doesn't exist in Data Base");
        if (existByExplorerName(explorerDto.getNewExplorerName()))
            throw new ExplorerException("Explorer already exist in Data Base");
        if (explorerDto.getNewExplorerName().matches("\\W*"))
            throw new ExplorerException("Explorer name should contain AlphaNumeric characters only");
    }

    public ExplorerEntity validAndModifyName(ExplorerDto explorerDto) {

        validatePutNameArgs(explorerDto);
        var explorer = explorerRepository.findExplorerEntityByExplorerName(explorerDto.getExplorerName());
        explorer.get().changeExplorerName(explorerDto.getNewExplorerName());
        saveExplorer(explorer.get());
        return explorer.get();
    }

    public void validAndDeleteExplorer(ExplorerDto explorerDto) {
        checkExplorerArgs(explorerDto);
        var planetEntity = locatePlanet(explorerDto.getPlanetName());
        var explorerEntity = explorerRepository.findExplorerEntityByExplorerName(explorerDto.getExplorerName());
        if (!planetEntity.isPresent())
            throw new ExplorerException("Planet not found in data base");
        if (!explorerEntity.isPresent())
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
           explorerRepository.delete(explorerEntity); }

    public ExplorerEntity validAndMoveExplorer(ExplorerDto explorerDto) {
        validateMoveArgs(explorerDto);
        Optional<ExplorerEntity> explorer = explorerRepository.findExplorerEntityByExplorerName(explorerDto.getExplorerName());
        String planet[][] = PlanetService.drawPlanet(explorer.get());
        moveExplorer(planet, explorerDto.getMovement(), explorer.get());
        saveExplorer(explorer.get());
        return explorer.get();
    }

    private void validateMoveArgs(ExplorerDto explorerDto) {

        checkExplorerArgs(explorerDto);
        if (!explorerDto.getMovement().matches("[LMR]*"))
            throw new ExplorerException("move sequence must be L, M, R in Uppercase");
    }


    private void moveExplorer(String[][] planet, String movement, ExplorerEntity explorer) {

        for (int i = 0; i < movement.length(); i++){
            if(movement.charAt(i) == 'L'){
                ExplorerEnum.turnLeft(explorer);
            }
            else if (movement.charAt(i) == 'R'){
                ExplorerEnum.turnRight(explorer);
            }
            else if (movement.charAt(i) == 'M'){
                moveForward(planet, explorer);
            }
        }
    }

    private void moveForward(String[][] planet, ExplorerEntity explorer) {

        switch (explorer.getDirection()){
            case NORTH:
                moveNorth(planet, explorer);
                break ;
            case WEST:
                moveWest(planet, explorer);
                break ;
            case SOUTH:
                moveSouth(planet, explorer);
                break ;
            case EAST:
                moveEast(planet, explorer);
                break ;
        }
    }

    private void moveNorth(String[][] planet, ExplorerEntity explorer) {

        if (explorer.getX() == 0){
            if (planet[planet.length - 1][explorer.getY()].matches("x|0")){
                explorer.axisUpdate((planet.length - 1), explorer.getY());
            } else {
                throw new ExplorerException("There is an explorer ahead, aborting movement.");
            }
        } else if (planet[explorer.getX() - 1][explorer.getY()].matches("x|0")){
                explorer.axisUpdate(explorer.getX() - 1, explorer.getY());
        } else {
            throw new ExplorerException("There is an explorer ahead, aborting movement.");
        }
    }

    private void moveWest(String[][] planet, ExplorerEntity explorer) {

        if (explorer.getY() == planet[explorer.getX()].length - 1){
            if (planet[explorer.getX()][0].matches(String.valueOf("x|0"))){
                explorer.axisUpdate((explorer.getX()), 0);
            } else {
                throw new ExplorerException("There is an explorer ahead, aborting movement.");
            }
        } else if (planet[explorer.getX()][explorer.getY() + 1].matches("x|0")){
            explorer.axisUpdate(explorer.getX(), explorer.getY() + 1);
        } else {
            throw new ExplorerException("There is an explorer ahead, aborting movement.");
        }
    }

    private void moveSouth(String[][] planet, ExplorerEntity explorer) {

        if (explorer.getX() == planet.length - 1){
            if (planet[0][explorer.getY()].matches("x|0")){
                explorer.axisUpdate(0, explorer.getY());
            } else {
                throw new ExplorerException("There is an explorer ahead, aborting movement.");
            }
        } else if (planet[explorer.getX() + 1][explorer.getY()].matches("x|0")){
            explorer.axisUpdate(explorer.getX() + 1, explorer.getY());
        } else {
            throw new ExplorerException("There is an explorer ahead, aborting movement.");
        }
    }

    private void moveEast(String[][] planet, ExplorerEntity explorer) {

        if (explorer.getY() == 0){
            if (planet[explorer.getX()][planet[explorer.getX()].length - 1].matches("x|0")){
                explorer.axisUpdate((explorer.getX()), planet[explorer.getX()].length - 1);
            } else {
                throw new ExplorerException("There is an explorer ahead, aborting movement.");
            }
        } else if (planet[explorer.getX()][explorer.getY() - 1].matches("x|0")){
            explorer.axisUpdate(explorer.getX(), explorer.getY() - 1);
        } else {
            throw new ExplorerException("There is an explorer ahead, aborting movement.");
        }
    }
}
